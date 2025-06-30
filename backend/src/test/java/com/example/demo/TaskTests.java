package com.example.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTests {

    @Test
    void testDefaultConstructor() {
        Task task = new Task();
        assertNull(task.getTaskdescription());
    }

    @Test
    void testParameterConstructor() {
        Task task = new Task("Test task");
        assertEquals("Test task", task.getTaskdescription());
    }

    @Test
    void testSetTaskdescription() {
        Task task = new Task();
        task.setTaskdescription("New task");
        assertEquals("New task", task.getTaskdescription());
    }

    @Test
    void testTaskTooLong() {
        String longText = "a".repeat(250);
        Task task = new Task(longText);
        assertEquals(200, task.getTaskdescription().length());
        assertEquals("a".repeat(200), task.getTaskdescription());
    }

    @Test
    void testNullTaskdescription() {
        Task task = new Task();
        task.setTaskdescription(null);
        assertNull(task.getTaskdescription());
    }

    @Test
    void testExactly200Characters() {
        String text200 = "a".repeat(200);
        Task task = new Task(text200);
        assertEquals(200, task.getTaskdescription().length());
        assertEquals(text200, task.getTaskdescription());
    }
}
