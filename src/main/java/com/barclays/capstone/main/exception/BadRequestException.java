package com.barclays.capstone.main.exception;


/**
 * 
 * @author Aakash Gouri Shankar
 * @Description Generic Bad Request Exception Class
 * 
 */
public class BadRequestException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private String message;

	public BadRequestException() {}

	public BadRequestException(String msg)
	    {
	        super(msg);
	        this.message = msg;
	    }

}
