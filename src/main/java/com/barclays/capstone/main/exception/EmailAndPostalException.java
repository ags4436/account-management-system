package com.barclays.capstone.main.exception;

public class EmailAndPostalException extends RuntimeException{

	private static final long serialVersionUID = 8291648835672204821L;

	public EmailAndPostalException() {
		super();
	}

	public EmailAndPostalException(String message) {
		super(message);
	}
}
