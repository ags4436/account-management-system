package com.barclays.capstone.main.exception;

public class InvalidCustomerDataException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private String message;

	public InvalidCustomerDataException() {}

	public InvalidCustomerDataException(String msg)
	    {
	        super(msg);
	        this.message = msg;
	    }
}
