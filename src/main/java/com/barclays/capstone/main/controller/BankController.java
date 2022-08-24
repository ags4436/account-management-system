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

import com.barclays.capstone.main.constants.SystemConstants;
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

	@RequestMapping(value = SystemConstants.ADDNEWCUSTOMER, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, String>> createAccount(@RequestBody BankCustomer customer, @PathVariable(name = "customerId") int customerId,@PathVariable(name = "cookieToken") String cookieToken) {
		System.out.println(customer.toString());
		HashMap<String,String> result  = operations.addNewCustomer(customer,customerId,cookieToken);
		return new ResponseEntity<HashMap<String,String>>(result, getHttpResponseStatus(result.get("statusCode")));
	}
	
	@RequestMapping(value = SystemConstants.ADDNEWACCOUNT, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, String>>  addNewAccount(@RequestBody BankAccount customer, @PathVariable(name = "customerId") int customerId,@PathVariable(name = "cookieToken") String cookieToken) {
		//return operations.isExistingCustomer(customer.getPanCard());
		HashMap<String,String> result  = operations.createAccount(customer,customerId,cookieToken);
		return new ResponseEntity<HashMap<String,String>>(result, getHttpResponseStatus(result.get("statusCode")));
	}
	
	@PutMapping("/deposit")
	public ResponseEntity<String> cashDeposit(@RequestParam String accountNumber, @RequestParam int amount) {
		String result;
		ResponseEntity<String> response;
		logger.info("depositing.................");
		if (operations.deposit(accountNumber, amount)) {
			result = amount + " is successfully deposited to account " + accountNumber;
			logger.info(amount+" is deposited to account: "+accountNumber);
			response = new ResponseEntity<String>(result, HttpStatus.OK);
		} else {
			logger.info("Error.....................");
			result = "Amount is not deposited due to some error please try again";
			response = new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
		}
		return response;
	}
	@PutMapping("/withdrawal")
	public ResponseEntity<String> cashWithdrawal(@RequestParam String accountNumber, @RequestParam int amount) {
		String result;
		ResponseEntity<String> response;
		logger.info("Withdrawing the money.....................");
		if(amount>10000) {
			logger.info("Cannot withdraw more than 10000");
			result="Limit for withdraw in single day is 10000. More than 10000 cannot be withdraw in single day";
			response=new ResponseEntity<String>(result,HttpStatus.BAD_REQUEST);
		}
		if (operations.cashWithdrawal(accountNumber, amount)) {
			result = amount + " withdraw from account " + accountNumber;
			logger.info(amount+" has been withdraw from acoount: "+accountNumber);
			response = new ResponseEntity<String>(result, HttpStatus.OK);
		} else {
			logger.info("Error......cannot withdraw");
			result = "cash Withdrawal can not be done due to some error. Please try again";
			response = new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
		}
		return response;

	}
	@PutMapping("/transfer")
	public ResponseEntity<String> transfer(@RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
			@RequestParam int amount) {
		logger.info("Transferring....................");
		String result= operations.transfer(fromAccountNumber, toAccountNumber, amount);
		if(result.contains("successfull")) {
			logger.info("Funds are transferred successfull");
			return new ResponseEntity<String>(result,HttpStatus.OK);
		}else {
			logger.info("Funds are not transfered successfully. Please try again");
			return new ResponseEntity<String>(result,HttpStatus.BAD_REQUEST);
		}
	}

}