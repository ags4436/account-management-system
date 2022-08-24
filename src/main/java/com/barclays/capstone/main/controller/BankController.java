package com.barclays.capstone.main.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barclays.capstone.main.model.BankAccount;
import com.barclays.capstone.main.model.BankCustomer;
import com.barclays.capstone.main.model.ChangePassword;
import com.barclays.capstone.main.model.Login;
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

	
	Logger logger=LoggerFactory.getLogger(BankController.class);
	
	@PostMapping(path = "/login")
	public ResponseEntity<String> login(@RequestBody Login user) {
		logger.info("Loging in user..........");
		BankCustomer customer=operations.login(user);
		String result="";
		if(customer==null) {
			logger.info("Loging failed as user provided wrong credentials");
			result="Wrong credentials. Please try again with correct ID and password";
			return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
		}
		result=customer.getRole();
		logger.info(user.getUserId()+" log in successful and user has a role: "+ result);
		return new ResponseEntity<String>(result,HttpStatus.OK);
	}
	
	@PostMapping("/changePassword")
	public ResponseEntity<String> changePassword(@RequestBody ChangePassword changePassword){
		logger.info("Changing password.....................");
		  String result=operations.changePassword(changePassword);
		  return new ResponseEntity<String>(result,HttpStatus.OK);
	}
	@RequestMapping(value = "/check-customer-pan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean checkCustomerPan(@RequestBody BankCustomer customer) {
		System.out.println(customer.toString());
		return operations.isExistingCustomer(customer.getPanCard());
	}
	
	@RequestMapping(value = "/create-new-account", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String createAccount(@RequestBody BankCustomer customer) {
		System.out.println(customer.toString());
		return operations.createAccount(customer);
	}
	
	@GetMapping("/viewAccounts")
	public ResponseEntity<List<BankAccount>> viewAccounts(@RequestParam String pancard) {
		logger.info("Showing linked accounts of user......................");
		List<BankAccount> Accounts = operations.viewAccount(pancard);
		return new ResponseEntity<List<BankAccount>>(Accounts, HttpStatus.OK);
	}


	

}