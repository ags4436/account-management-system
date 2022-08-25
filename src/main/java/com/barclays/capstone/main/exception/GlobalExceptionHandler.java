package com.barclays.capstone.main.exception;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 
 * @author Aakash Gouri Shankar, Roopa Amrutha, Shipra Saini
 * @Description handler of Global Exceptions
 * 
 */

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidCustomerDataException.class)
	public ResponseEntity<HashMap<String, String>> handleEmployeeNotFoundException(InvalidCustomerDataException e) {
		HashMap<String, String> response = new HashMap<String, String>();
		response.put("success", "false");
		response.put("message", e.getMessage());
		return new ResponseEntity<HashMap<String, String>>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<HashMap<String, String>> handleBadRequestException(BadRequestException e) {
		HashMap<String, String> response = new HashMap<String, String>();
		response.put("success", "false");
		response.put("message", e.getMessage());
		return new ResponseEntity<HashMap<String, String>>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<HashMap<String, String>> handleException(MissingServletRequestParameterException e) {
		HashMap<String, String> response = new HashMap<String, String>();
		response.put("success", "false");
		response.put("message", "Invalid Request Parameters");
		return new ResponseEntity<HashMap<String, String>>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<HashMap<String, String>> handleHttpMediaTypeNotSupportedException(
			HttpMediaTypeNotSupportedException e) {
		HashMap<String, String> response = new HashMap<String, String>();
		response.put("success", "false");
		response.put("message", "Invalid Request Body");
		return new ResponseEntity<HashMap<String, String>>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<HashMap<String, String>> handleHttpMessageNotReadableException(
			HttpMessageNotReadableException e) {
		HashMap<String, String> response = new HashMap<String, String>();
		response.put("success", "false");
		response.put("message", "Invalid Request Body");
		return new ResponseEntity<HashMap<String, String>>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EmailAndPostalException.class)
	public ResponseEntity<HashMap<String, String>> handleEmailAndPostalException(EmailAndPostalException e) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("message", e.getMessage());
		HashMap<String, String> response = new HashMap<String, String>();
		response.put("success", "false");
		response.put("message", e.getMessage());
		return new ResponseEntity<HashMap<String, String>>(response, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<HashMap<String, String>> handle(Exception ex, HttpServletRequest request,
			HttpServletResponse response) {

		HashMap<String, String> res = new HashMap<String, String>();
		if (ex instanceof NullPointerException) {
			res.put("success", "false");
			res.put("message", "Bad Request");
			return new ResponseEntity<HashMap<String, String>>(res, HttpStatus.BAD_REQUEST);
		}
		res.put("success", "false");
		res.put("message", "Internal Server Error Contact Your System Administrator");
		return new ResponseEntity<HashMap<String, String>>(res, HttpStatus.BAD_REQUEST);
	}
}
