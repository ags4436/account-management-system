package com.barclays.capstone.main.controller;

import java.sql.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barclays.capstone.main.constants.SystemConstants;
import com.barclays.capstone.main.exception.BadRequestException;
import com.barclays.capstone.main.service.BankTransactionService;

/**
 * @author Akash Salave, Shubham Chokhani, Aakash Gouri Shankar
 * @Description REST API Controller for all Transaction related APIs
 *
 */

@RestController
@RequestMapping("/account")
public class TransactionController {

	Logger logger = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	BankTransactionService operations;

	ControllerUtility controllerUtility = new ControllerUtility();

	/**
	 * @param customerId
	 * @param cookieToken
	 * @param accountNumber
	 * @param amount
	 */
	@RequestMapping(value = SystemConstants.DEPOSIT, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = {
			"*/*" })
	public ResponseEntity<HashMap<String, String>> cashDeposit(@PathVariable(name = "customerId") int customerId,
			@PathVariable(name = "cookieToken") String cookieToken, @RequestParam String accountNumber,
			@RequestParam int amount) {
		if (accountNumber == "" || amount == 0) {
			throw new BadRequestException("Invalid Request Params");
		}
		HashMap<String, String> result = operations.deposit(accountNumber, amount, customerId, cookieToken);
		return new ResponseEntity<HashMap<String, String>>(result,
				controllerUtility.getHttpResponseStatus(result.get("statusCode")));

	}

	/**
	 * @param customerId
	 * @param cookieToken
	 * @param accountNumber
	 * @param amount
	 */
	@RequestMapping(value = SystemConstants.WITHDRAW, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = {
			"*/*" })
	public ResponseEntity<HashMap<String, String>> cashWithdrawal(@PathVariable(name = "customerId") int customerId,
			@PathVariable(name = "cookieToken") String cookieToken, @RequestParam String accountNumber,
			@RequestParam int amount) {
		if (accountNumber == "" || amount == 0) {
			throw new BadRequestException("Invalid Request Params");
		}
		HashMap<String, String> result = operations.cashWithdrawal(accountNumber, amount, customerId, cookieToken);

		return new ResponseEntity<HashMap<String, String>>(result,
				controllerUtility.getHttpResponseStatus(result.get("statusCode")));

	}

	/**
	 * @param customerId
	 * @param cookieToken
	 * @param fromAccountNumber
	 * @param toAccountNumber
	 * @param amount
	 */
	@RequestMapping(value = SystemConstants.TRANSFER, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = {
			"*/*" })
	public ResponseEntity<HashMap<String, String>> transfer(@PathVariable(name = "customerId") int customerId,
			@PathVariable(name = "cookieToken") String cookieToken, @RequestParam String fromAccountNumber,
			@RequestParam String toAccountNumber, @RequestParam int amount) {
		if (fromAccountNumber == "" || toAccountNumber == "" || amount == 0) {
			throw new BadRequestException("Invalid Request Params");
		}

		logger.info("Transferring....................");
		HashMap<String, String> result = operations.transfer(fromAccountNumber, toAccountNumber, amount, customerId,
				cookieToken);
		return new ResponseEntity<HashMap<String, String>>(result,
				controllerUtility.getHttpResponseStatus(result.get("statusCode")));
	}

	/**
	 * @param customerId
	 * @param cookieToken
	 * @param accountNumber
	 * @param fromTransactionDate
	 * @param toTransactionDate
	 */
	@RequestMapping(value = SystemConstants.EXPORT, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = {
			"*/*" })
	public ResponseEntity<HashMap<String, String>> exportTransaction(@PathVariable(name = "customerId") int customerId,
			@PathVariable(name = "cookieToken") String cookieToken, @RequestParam String accountNumber,
			@RequestParam Date fromTransactionDate, @RequestParam Date toTransactionDate) {

		if (accountNumber == "" || fromTransactionDate == null || toTransactionDate == null) {
			throw new BadRequestException("Invalid Request Params");
		}

		logger.info("Transferring....................");
		HashMap<String, String> result = operations.exportTransaction(accountNumber, fromTransactionDate,
				toTransactionDate, customerId, cookieToken);
		return new ResponseEntity<HashMap<String, String>>(result,
				controllerUtility.getHttpResponseStatus(result.get("statusCode")));
	}

	/**
	 * @param customerId
	 * @param cookieToken
	 * @param accountNumber
	 */
	@RequestMapping(value = SystemConstants.MINISTATEMENT, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = {
			"*/*" })
	public ResponseEntity<HashMap<String, String>> miniStatement(@PathVariable(name = "customerId") int customerId,
			@PathVariable(name = "cookieToken") String cookieToken, @RequestParam String accountNumber) {

		if (accountNumber == "") {
			throw new BadRequestException("Invalid Request Params");
		}
		logger.info("Transferring....................");
		HashMap<String, String> result = operations.miniStatement(accountNumber, customerId, cookieToken);
		return new ResponseEntity<HashMap<String, String>>(result,
				controllerUtility.getHttpResponseStatus(result.get("statusCode")));
	}

}
