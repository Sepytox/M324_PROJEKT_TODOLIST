package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DemoApplication.class)
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DemoApplication demoApplication;

    @BeforeEach
    void setUp() {
        demoApplication.getTasks().clear();
    }

    @Test
    void addTask() throws Exception {
        Task task = new Task("JUnit lernen");

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].taskdescription", is("JUnit lernen")));
    }

    @Test
    void testNoDuplicates() throws Exception {
        Task task = new Task("Doppelt?");

        // zwei mal hinzufügen
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testDeleteTask() throws Exception {
        Task task = new Task("Lösche mich");

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testEmptyTask() throws Exception {
        Task task = new Task("");
        
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(content().string("error"));
    }

    @Test
    void testDeleteNotFound() throws Exception {
        Task task = new Task("gibts nicht");
        
        mockMvc.perform(post("/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(content().string("not_found"));
    }

    @Test
    void testMultipleTasks() throws Exception {
        Task task1 = new Task("Task 1");
        Task task2 = new Task("Task 2");
        
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task1)))
                .andExpect(status().isOk());
                
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task2)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testEditTask() throws Exception {
        Task task = new Task("Original Task");
        
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].taskdescription", is("Original Task")));

        EditRequest editRequest = new EditRequest(1, "Bearbeitete Task");
        
        mockMvc.perform(post("/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].taskdescription", is("Bearbeitete Task")));
    }

    @Test
    void testEditTaskNotFound() throws Exception {
        EditRequest editRequest = new EditRequest(999, "Gibt es nicht");
        
        mockMvc.perform(post("/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("not_found"));
    }

    @Test
    void testEditTaskEmptyDescription() throws Exception {
        Task task = new Task("Test Task");
        
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk());

        EditRequest editRequest = new EditRequest(1, "");
        
        mockMvc.perform(post("/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("error"));
    }

    @Test
    void testAddTaskWithNullDescription() throws Exception {
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isOk())
                .andExpect(content().string("error"));
    }

    @Test
    void testAddTaskWithInvalidJson() throws Exception {
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json"))
                .andExpect(status().isOk())
                .andExpect(content().string("error"));
    }

    @Test
    void testDeleteTaskWithNullDescription() throws Exception {
        mockMvc.perform(post("/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isOk())
                .andExpect(content().string("error"));
    }

    @Test
    void testDeleteTaskWithInvalidJson() throws Exception {
        mockMvc.perform(post("/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json"))
                .andExpect(status().isOk())
                .andExpect(content().string("error"));
    }

    @Test
    void testEditTaskWithNullBody() throws Exception {
        mockMvc.perform(post("/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isOk())
                .andExpect(content().string("error"));
    }

    @Test
    void testEditTaskWithInvalidJson() throws Exception {
        mockMvc.perform(post("/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json"))
                .andExpect(status().isOk())
                .andExpect(content().string("error"));
    }

    @Test
    void testEditTaskWithInvalidId() throws Exception {
        EditRequest editRequest = new EditRequest(-1, "Test");
        
        mockMvc.perform(post("/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("error"));
    }

    @Test
    void testEditTaskWithNullDescription() throws Exception {
        Task task = new Task("Test Task");
        
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk());

        EditRequest editRequest = new EditRequest(1, null);
        
        mockMvc.perform(post("/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("error"));
    }
}
