package com.example.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTests {

    @Test
    void testTaskCreation() {
        Task task = new Task("Test task");
        assertEquals("Test task", task.getTaskdescription());
    }
    
    @Test
    void testEmptyConstructor() {
        Task task = new Task();
        assertNull(task.getTaskdescription());
    }
    
    @Test
    void testTaskDescriptionChange() {
        Task task = new Task("Original");
        task.setTaskdescription("Changed");
        assertEquals("Changed", task.getTaskdescription());
    }
    
    @Test
    void testLongDescriptionTruncated() {
        String longDesc = "a".repeat(250);
        Task task = new Task(longDesc);
        assertEquals(200, task.getTaskdescription().length());
        assertEquals("a".repeat(200), task.getTaskdescription());
    }
    
    @Test
    void testSetLongDescriptionTruncated() {
        Task task = new Task("Short");
        String longDesc = "b".repeat(250);
        task.setTaskdescription(longDesc);
        assertEquals(200, task.getTaskdescription().length());
        assertEquals("b".repeat(200), task.getTaskdescription());
    }
    
    @Test
    void testMaxLengthDescription() {
        String maxDesc = "c".repeat(200);
        Task task = new Task(maxDesc);
        assertEquals(maxDesc, task.getTaskdescription());
    }
    
    @Test
    void testNullDescription() {
        Task task = new Task();
        task.setTaskdescription(null);
        assertNull(task.getTaskdescription());
    }
}