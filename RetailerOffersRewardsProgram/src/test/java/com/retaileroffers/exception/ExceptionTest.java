package com.retaileroffers.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ExceptionTest {

	@Test
    void testExceptionMessage() {
        String message = "Customer not found";
        CutomerNotFoundException exception = new CutomerNotFoundException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testExceptionIsInstanceOfRuntimeException() {
        CutomerNotFoundException exception = new CutomerNotFoundException("Test");
        assertTrue(exception instanceof RuntimeException);
    }

}
