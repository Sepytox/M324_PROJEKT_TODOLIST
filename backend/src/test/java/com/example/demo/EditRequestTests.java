package com.example.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EditRequestTests {

    @Test
    void testDefaultConstructor() {
        EditRequest request = new EditRequest();
        assertEquals(0, request.getId());
        assertNull(request.getNewDescription());
    }

    @Test
    void testConstructorWithParameters() {
        EditRequest request = new EditRequest(3, "Neue Beschreibung");
        assertEquals(3, request.getId());
        assertEquals("Neue Beschreibung", request.getNewDescription());
    }

    @Test
    void testSetAndGetId() {
        EditRequest request = new EditRequest();
        request.setId(7);
        assertEquals(7, request.getId());
    }

    @Test
    void testSetAndGetNewDescription() {
        EditRequest request = new EditRequest();
        request.setNewDescription("Test Description");
        assertEquals("Test Description", request.getNewDescription());
    }
}
