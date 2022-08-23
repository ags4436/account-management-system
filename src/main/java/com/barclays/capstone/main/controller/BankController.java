package com.barclays.capstone.main.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/account")
public class BankController {
	
	
	
	@RequestMapping(value = "/hello", method = RequestMethod.GET, produces = { "application/json" })
	public String Hello() {
		return "Hello";
	}
	
	

}
