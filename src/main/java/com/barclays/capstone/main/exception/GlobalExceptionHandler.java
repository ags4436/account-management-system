package com.barclays.capstone.main.exception;


import java.util.HashMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidCustomerDataException.class)
	public ResponseEntity<HashMap<String, String>> handleEmployeeNotFoundException(InvalidCustomerDataException e) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("message", e.getMessage());
		HashMap<String, String> response = new HashMap<String, String>();
		response.put("success", "false");
		response.put("message", e.getMessage());
		return new ResponseEntity<HashMap<String, String>>(response, HttpStatus.BAD_REQUEST);
	}

	// more exception handlers

}

