package com.retaileroffers.globalexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a customer is not found. Triggers a 404 NOT FOUND
 * response in controllers.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CutomerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5260266909466037897L;

	public CutomerNotFoundException(String message) {
		super(message);
	}
}
