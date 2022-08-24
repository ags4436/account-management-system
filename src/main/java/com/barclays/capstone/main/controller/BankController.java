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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.barclays.capstone.main.constants.SystemConstants;
import com.barclays.capstone.main.model.BankAccount;
import com.barclays.capstone.main.model.BankCustomer;
import com.barclays.capstone.main.model.ChangePassword;
import com.barclays.capstone.main.model.Credentials;
import com.barclays.capstone.main.service.BankCustomerService;
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
	
	@Autowired 
	BankCustomerService bankCustomerService;

	Logger logger = LoggerFactory.getLogger(BankController.class);

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
	@RequestMapping(value = SystemConstants.LOGIN, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, String>> login(@RequestBody Credentials creds) {
		logger.info("Loging in user..........");
		HashMap<String, String> result = operations.login(creds);

		return new ResponseEntity<HashMap<String, String>>(result, HttpStatus.OK);
	}

	@PostMapping(SystemConstants.CHANGEPASSWORD)
	public ResponseEntity<HashMap<String, String>> changePassword(@RequestBody ChangePassword changePassword) {
		logger.info("Changing password.....................");
		HashMap<String,String> result  = operations.changePassword(changePassword);
		return new ResponseEntity<HashMap<String,String>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = SystemConstants.CHECKCUSTOMERPAN, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, String>> checkCustomerPan(@RequestBody BankCustomer customer,@PathVariable(name = "customerId") int customerId,@PathVariable(name = "cookieToken") String cookieToken) {
		System.out.println(customer.toString());
		HashMap<String,String> result  = operations.isExistingCustomer(customer.getPanCard(),customerId,cookieToken);
		return new ResponseEntity<HashMap<String,String>>(result, getHttpResponseStatus(result.get("statusCode")));
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
		HashMap<String,String> result  = operations.addNewCustomer(customer,multipartFile,customerId,cookieToken);
		return new ResponseEntity<HashMap<String,String>>(result, getHttpResponseStatus(result.get("statusCode")));
	}
	
	@RequestMapping(value = SystemConstants.ADDNEWACCOUNT, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, String>>  addNewAccount(@RequestBody BankAccount customer, @PathVariable(name = "customerId") int customerId,@PathVariable(name = "cookieToken") String cookieToken) {
		//return operations.isExistingCustomer(customer.getPanCard());
		HashMap<String,String> result  = operations.createAccount(customer,customerId,cookieToken);
		return new ResponseEntity<HashMap<String,String>>(result, getHttpResponseStatus(result.get("statusCode")));
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