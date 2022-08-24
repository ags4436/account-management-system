package com.barclays.capstone.main.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barclays.capstone.main.constants.SystemConstants;
import com.barclays.capstone.main.service.BankTransactionService;

@RestController
@RequestMapping("/account")
public class TransactionController {

	Logger logger = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	BankTransactionService operations;

	ControllerUtility controllerUtility = new ControllerUtility();

	@RequestMapping(value = SystemConstants.DEPOSIT, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = {
			"*/*" })
	public ResponseEntity<HashMap<String, String>> cashDeposit(@PathVariable(name = "customerId") int customerId,
			@PathVariable(name = "cookieToken") String cookieToken, @RequestParam String accountNumber,
			@RequestParam int amount) {
		HashMap<String, String> result = operations.deposit(accountNumber, amount, customerId, cookieToken);
		return new ResponseEntity<HashMap<String, String>>(result,
				controllerUtility.getHttpResponseStatus(result.get("statusCode")));

	}

	@RequestMapping(value = SystemConstants.WITHDRAW, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = {
			"*/*" })
	public ResponseEntity<HashMap<String, String>> cashWithdrawal(@PathVariable(name = "customerId") int customerId,
			@PathVariable(name = "cookieToken") String cookieToken, @RequestParam String accountNumber,
			@RequestParam int amount) {

		HashMap<String, String> result = operations.cashWithdrawal(accountNumber, amount,customerId, cookieToken);
		
		return new ResponseEntity<HashMap<String, String>>(result,
				controllerUtility.getHttpResponseStatus(result.get("statusCode")));

	}

	@RequestMapping(value = SystemConstants.TRANSFER, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = {
			"*/*" })
	public ResponseEntity<HashMap<String, String>> transfer(@PathVariable(name = "customerId") int customerId,
			@PathVariable(name = "cookieToken") String cookieToken, @RequestParam String fromAccountNumber,
			@RequestParam String toAccountNumber, @RequestParam int amount) {
		logger.info("Transferring....................");
		HashMap<String, String> result = operations.transfer(fromAccountNumber, toAccountNumber, amount, customerId,
				cookieToken);
		return new ResponseEntity<HashMap<String, String>>(result,
				controllerUtility.getHttpResponseStatus(result.get("statusCode")));
	}

}
