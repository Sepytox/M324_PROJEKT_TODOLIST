package com.example.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeleteRequestTests {

    @Test
    void testDefaultConstructor() {
        DeleteRequest request = new DeleteRequest();
        assertEquals(0, request.getId());
    }

    @Test
    void testConstructorWithId() {
        DeleteRequest request = new DeleteRequest(5);
        assertEquals(5, request.getId());
    }

    @Test
    void testSetAndGetId() {
        DeleteRequest request = new DeleteRequest();
        request.setId(10);
        assertEquals(10, request.getId());
    }
}
