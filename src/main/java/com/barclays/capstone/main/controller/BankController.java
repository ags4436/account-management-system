package com.barclays.capstone.main.controller;



import java.util.List;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barclays.capstone.main.service.Operations;





@RestController
@RequestMapping("/account")
public class BankController {
	
	
	@Autowired
	Operations operations;
	
	Logger logger=LoggerFactory.getLogger(BankController.class);
	
	
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
