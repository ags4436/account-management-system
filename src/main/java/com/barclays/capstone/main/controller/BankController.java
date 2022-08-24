package com.barclays.capstone.main.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.barclays.capstone.main.model.BankAccount;
import com.barclays.capstone.main.model.BankCustomer;
import com.barclays.capstone.main.model.ChangePassword;
import com.barclays.capstone.main.model.Credentials;
import com.barclays.capstone.main.service.BankServices;

@RestController
@RequestMapping("/account")
public class BankController {

//	@RequestMapping(value = "/hello", method = RequestMethod.GET, produces = { "application/json" })
//	public String Hello() {
//		return "Hello";
//	}

	@Autowired
	BankServices operations;

	Logger logger = LoggerFactory.getLogger(BankController.class);

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, String>> login(@RequestBody Credentials creds) {
		logger.info("Loging in user..........");
		HashMap<String, String> result = operations.login(creds);

		return new ResponseEntity<HashMap<String, String>>(result, HttpStatus.OK);
	}

	@PostMapping("/changePassword")
	public ResponseEntity<HashMap<String, String>> changePassword(@RequestBody ChangePassword changePassword) {
		logger.info("Changing password.....................");
		HashMap<String,String> result  = operations.changePassword(changePassword);
		return new ResponseEntity<HashMap<String,String>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/check-customer-pan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean checkCustomerPan(@RequestBody BankCustomer customer) {
		System.out.println(customer.toString());
		return operations.isExistingCustomer(customer.getPanCard());
	}

	@RequestMapping(value = "/create-new-account/{customerId}/{cookieToken}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, String>> createAccount(@RequestBody BankCustomer customer, @PathVariable(name = "customerId") int customerId,@PathVariable(name = "cookieToken") String cookieToken) {
		System.out.println(customer.toString());
		HashMap<String,String> result  = operations.createAccount(customer,customerId,cookieToken);
		return new ResponseEntity<HashMap<String,String>>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/add-new-account", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean addNewAccount(@RequestBody BankAccount account) {
		//return operations.isExistingCustomer(customer.getPanCard());
		
		return true;
	}

}