package com.barclays.capstone.main.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barclays.capstone.main.service.BankCustomerService;
import com.barclays.capstone.main.service.BankAuthenticationService;

@RestController
@RequestMapping("/account")
public class BankController {

	@Autowired
	BankAuthenticationService operations;
	
	@Autowired 
	BankCustomerService bankCustomerService;

	Logger logger = LoggerFactory.getLogger(BankController.class);

	
	

	
}