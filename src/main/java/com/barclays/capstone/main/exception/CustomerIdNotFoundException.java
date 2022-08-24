package com.barclays.capstone.main.exception;

public class CustomerIdNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 8291648835672204821L;

	public CustomerIdNotFoundException() {
		super();
	}

	public CustomerIdNotFoundException(String message) {
	
		super(message);
	}
}

