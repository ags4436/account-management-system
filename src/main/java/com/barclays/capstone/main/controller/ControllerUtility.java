package com.barclays.capstone.main.controller;

import org.springframework.http.HttpStatus;

public class ControllerUtility {
	
	public HttpStatus getHttpResponseStatus(String statusCode) {
		if(statusCode=="200")
			return HttpStatus.OK;
		else if(statusCode=="201")
			return HttpStatus.CREATED;
		else if(statusCode=="401")
			return HttpStatus.UNAUTHORIZED;
		else if(statusCode=="403")
			return HttpStatus.FORBIDDEN;
		
		return HttpStatus.NOT_FOUND;
	}

}
