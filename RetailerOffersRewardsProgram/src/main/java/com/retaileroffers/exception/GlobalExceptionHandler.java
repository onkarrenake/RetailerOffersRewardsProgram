package com.retaileroffers.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Centralized exception handler for REST controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	// Handles customer not found exceptions
	@ExceptionHandler(CutomerNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFound(CutomerNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	// Handles validation errors for request bodies
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidation(MethodArgumentNotValidException ex) {
		return ResponseEntity.badRequest().body("Validation failed: " + ex.getMessage());
	}

	// Handles database constraint violations (e.g., duplicate entries)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> handleDuplicate(DataIntegrityViolationException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate entry: " + ex.getMessage());
	}
}
