package com.barclays.capstone.main.controller;

import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.barclays.capstone.main.exception.CustomerIdNotFoundException;
import com.barclays.capstone.main.model.BankCustomer;
import com.barclays.capstone.main.model.ChangePassword;
import com.barclays.capstone.main.model.Login;
import com.barclays.capstone.main.service.BankCustomerService;
import com.barclays.capstone.main.service.BankServices;



@RestController
@RequestMapping("/account")
public class BankController {
	
//	private final BankCustomerService bankCustomerService;
//	
//	
//	@Autowired
//	 public BankController(BankCustomerService bankCustomerService) {
//	       this.bankCustomerService = bankCustomerService;
//	}
	@Autowired
	BankCustomerService bankCustomerService;
	
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
	@RequestMapping(value = "/viewDetails/{customerid}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BankCustomer findById(@PathVariable int customerid){
	   return bankCustomerService.findById(customerid)
	           .orElseThrow(() -> new CustomerIdNotFoundException("Customer not found"));
	}
	
	@RequestMapping(value = "/delete/{customerid}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, String>> deleteBankCustomer(@PathVariable int customerid){
		HashMap<String,String> result = bankCustomerService.deleteBankCustomer(customerid);
		return new ResponseEntity<HashMap<String,String>>(result, HttpStatus.OK);
	}
	

//	@RequestMapping(value = "/update/{customerid}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping("/update/{customerid}")
	public ResponseEntity<HashMap<String, String>> updateBankCustomer(@RequestBody BankCustomer bankCustomer,@PathVariable int customerid) {
		if(bankCustomer.equals(null)) {
			throw new CustomerIdNotFoundException("Invalid object entry");
		}
		HashMap<String,String> result = bankCustomerService.updateBankCustomer(customerid,bankCustomer);
		return new ResponseEntity<HashMap<String,String>>(result, HttpStatus.OK);
	}

}