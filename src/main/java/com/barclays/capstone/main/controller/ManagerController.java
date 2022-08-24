package com.barclays.capstone.main.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.barclays.capstone.main.constants.SystemConstants;
import com.barclays.capstone.main.model.BankAccount;
import com.barclays.capstone.main.model.BankCustomer;
import com.barclays.capstone.main.service.BankAccountService;
import com.barclays.capstone.main.service.BankCustomerService;

@RestController
@RequestMapping("/account")
public class ManagerController {
	
	@Autowired
	BankCustomerService bankCustomerService;
	
	@Autowired
	BankAccountService accountService;
	
	ControllerUtility controllerUtility = new ControllerUtility();
	
	@RequestMapping(value = SystemConstants.CHECKCUSTOMERPAN, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, String>> checkCustomerPan(@RequestBody BankCustomer customer,@PathVariable(name = "customerId") int customerId,@PathVariable(name = "cookieToken") String cookieToken) {
		System.out.println(customer.toString());
		HashMap<String,String> result  = accountService.isExistingCustomer(customer.getPanCard(),customerId,cookieToken);
		return new ResponseEntity<HashMap<String,String>>(result, controllerUtility.getHttpResponseStatus(result.get("statusCode")));
	}

	@RequestMapping(value = SystemConstants.ADDNEWCUSTOMER, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = "multipart/form-data")
	public ResponseEntity<HashMap<String, String>> createAccount( 
			@RequestParam(value = "panCard") String panCard,
			@RequestParam(value = "aadharNumber") String aadharNumber,
			@RequestParam(value = "customerName") String customerName,
			@RequestParam(value = "postalAddress") String postalAddress,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "dob") String dob,
			@RequestParam(value = "role") String role,
			@RequestParam("image") MultipartFile multipartFile,
			@PathVariable(name = "customerId") int customerId,
			@PathVariable(name = "cookieToken") String cookieToken
			) {
		
		BankCustomer customer = new BankCustomer(panCard,aadharNumber,customerName,postalAddress,email,dob,role);
		System.out.println(customer.toString());
		HashMap<String,String> result  = accountService.addNewCustomer(customer,multipartFile,customerId,cookieToken);
		return new ResponseEntity<HashMap<String,String>>(result, controllerUtility.getHttpResponseStatus(result.get("statusCode")));
	}
	
	@RequestMapping(value = SystemConstants.ADDNEWACCOUNT, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, String>>  addNewAccount(@RequestBody BankAccount customer, @PathVariable(name = "customerId") int customerId,@PathVariable(name = "cookieToken") String cookieToken) {
		//return operations.isExistingCustomer(customer.getPanCard());
		HashMap<String,String> result  = accountService.createAccount(customer,customerId,cookieToken);
		return new ResponseEntity<HashMap<String,String>>(result, controllerUtility.getHttpResponseStatus(result.get("statusCode")));
	}
	
	@RequestMapping(value = SystemConstants.VIEWCUSTOMERDETAILS, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = {"*/*"})
	public BankCustomer findById(@PathVariable int customerid){
	   return bankCustomerService.findById(customerid)
	           .orElseThrow(() -> new RuntimeException("Customer not found"));
	}
	
	@RequestMapping(value = SystemConstants.DETELECUSTOMERDETAILS, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,consumes = {"*/*"})
	public ResponseEntity<HashMap<String, String>> deleteBankCustomer(@PathVariable int customerid){
		HashMap<String,String> result = bankCustomerService.deleteBankCustomer(customerid);
		return new ResponseEntity<HashMap<String,String>>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = SystemConstants.UPDATECUSTOMERDETAILS, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,consumes = {"*/*"})
	public ResponseEntity<HashMap<String, String>> updateBankCustomer(@RequestBody BankCustomer bankCustomer,@PathVariable int customerid) {
		HashMap<String,String> result = bankCustomerService.updateBankCustomer(customerid,bankCustomer);
		return new ResponseEntity<HashMap<String,String>>(result, HttpStatus.OK);
	}


}
