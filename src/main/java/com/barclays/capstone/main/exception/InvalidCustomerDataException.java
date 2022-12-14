package com.barclays.capstone.main.exception;

/**
 * 
 * @author Aakash Gouri Shankar
 * @Description Invalid Format of Cusomter Data Exception
 * 
 */
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
