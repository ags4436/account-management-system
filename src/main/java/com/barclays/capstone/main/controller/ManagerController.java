package com.barclays.capstone.main.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.barclays.capstone.main.exception.BadRequestException;
import com.barclays.capstone.main.model.BankAccount;
import com.barclays.capstone.main.model.BankCustomer;
import com.barclays.capstone.main.service.BankAccountService;
import com.barclays.capstone.main.service.BankCustomerService;

/**
 * @author Roopa Amrutha, Shipra Saini, Aakash Gouri SHankar
 * @Description REST API Controller for Manager and Customer Service related
 *              APIs
 *
 */

@RestController
@RequestMapping("/account")
public class ManagerController {
	
	@Autowired
	BankCustomerService bankCustomerService;
	
	@Autowired
	BankAccountService accountService;
	
	ControllerUtility controllerUtility = new ControllerUtility();
	
	Logger logger = LoggerFactory.getLogger(ManagerController.class);

	@RequestMapping(value = SystemConstants.CHECKCUSTOMERPAN, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, String>> checkCustomerPan(@RequestBody BankCustomer customer,@PathVariable(name = "customerId") int customerId,@PathVariable(name = "cookieToken") String cookieToken) {
		logger.info("Inside CheckCustomerPAN Contoller");
		if(customer.getPanCard()=="") {
			logger.info("Bad Request Throwing Exception");
			throw new BadRequestException("Invalid PAN");
		}
		logger.info("Invoking CheckCustomerPAN Service");
		HashMap<String,String> result  = accountService.isExistingCustomer(customer.getPanCard(),customerId,cookieToken);
		return new ResponseEntity<HashMap<String,String>>(result, controllerUtility.getHttpResponseStatus(result.get("statusCode")));
	}

	@RequestMapping(value = SystemConstants.ADDNEWCUSTOMER, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = "multipart/form-data")
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
		logger.info("Inside AddNewCustomer Contoller");
		BankCustomer customer = new BankCustomer(panCard,aadharNumber,customerName,postalAddress,email,dob,role);
		logger.info("Invoking AddNewCustomer Service");
		HashMap<String,String> result  = accountService.addNewCustomer(customer,multipartFile,customerId,cookieToken);
		return new ResponseEntity<HashMap<String,String>>(result, controllerUtility.getHttpResponseStatus(result.get("statusCode")));
	}
	
	@RequestMapping(value = SystemConstants.ADDNEWACCOUNT, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, String>>  addNewAccount(@RequestBody BankAccount customer, @PathVariable(name = "customerId") int customerId,@PathVariable(name = "cookieToken") String cookieToken) {
		logger.info("Inside AddNewAccountContoller");
		if(customer.getCustomerId()==0) {
			logger.info("Bad Request Throwing Exception");
			throw new BadRequestException("Invalid Customer Id");
		}
		logger.info("Invoking createAccount Service");
		HashMap<String,String> result  = accountService.createAccount(customer,customerId,cookieToken);
		return new ResponseEntity<HashMap<String,String>>(result, controllerUtility.getHttpResponseStatus(result.get("statusCode")));
	}
	
	@RequestMapping(value = SystemConstants.VIEWCUSTOMERDETAILS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = {"*/*"})
	public ResponseEntity<HashMap<String, String>> findById(@RequestParam(value = "userId") int userId,@PathVariable(name = "customerId") int customerId,@PathVariable(name = "cookieToken") String cookieToken){
		logger.info("Inside ViewCustomerDetails");
		if(userId==0) {
			logger.info("Bad Request Throwing Exception");
	    	throw new BadRequestException("Invalid user Id");
	    }
		logger.info("Invoking ViewCustomerDetails Service");
		HashMap<String,String> result  = bankCustomerService.findById(userId,customerId,cookieToken);
		return new ResponseEntity<HashMap<String,String>>(result, controllerUtility.getHttpResponseStatus(result.get("statusCode")));
	}
	
	@RequestMapping(value = SystemConstants.DETELECUSTOMERDETAILS, method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE,consumes = {"*/*"})
	public ResponseEntity<HashMap<String, String>> deleteBankCustomer(@RequestParam(value = "userId") int userId,@PathVariable(name = "customerId") int customerId,@PathVariable(name = "cookieToken") String cookieToken){
		logger.info("Inside DeleteCustomerDetails");
		if(userId==0) {
			logger.info("Bad Request Throwing Exception");
	    	throw new BadRequestException("Invalid user Id");
	    }
		logger.info("Invoking DeleteCustomerDetails Service");
		HashMap<String,String> result = bankCustomerService.deleteBankCustomer(userId,customerId,cookieToken);
		return new ResponseEntity<HashMap<String,String>>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = SystemConstants.UPDATECUSTOMERDETAILS, method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE,consumes = {"*/*"})
	public ResponseEntity<HashMap<String, String>> updateBankCustomer(@RequestBody BankCustomer bankCustomer,@PathVariable(name = "customerId") int customerId,@PathVariable(name = "cookieToken") String cookieToken) {
		logger.info("Inside UpdateCustomerDetails");
		if(bankCustomer.getCustomerID()==0) {
			logger.info("Bad Request Throwing Exception");
	    	throw new BadRequestException("Invalid Customer Id");
	    }
		logger.info("Invoking UpdateCustomerDetails Service");
		HashMap<String,String> result = bankCustomerService.updateBankCustomer(bankCustomer,customerId,cookieToken);
		return new ResponseEntity<HashMap<String,String>>(result, HttpStatus.OK);
	}

}
