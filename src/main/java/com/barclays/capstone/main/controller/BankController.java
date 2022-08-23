package com.barclays.capstone.main.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
		BankCustomers customer=operations.login(user);
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
	
	

}
