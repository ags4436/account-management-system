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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.barclays.capstone.main.constants.SystemConstants;
import com.barclays.capstone.main.exception.BadRequestException;
import com.barclays.capstone.main.model.ChangePassword;
import com.barclays.capstone.main.model.Credentials;
import com.barclays.capstone.main.service.BankAuthenticationService;

/**
 * 
 * @author Divya Raisinghani, Harsh Das, Aakash Gouri Shankar
 * @Description Controllers for Authentication
 * 
 */

@RestController
@RequestMapping("/account")
public class AuthenticationController {

	Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	BankAuthenticationService operations;

	ControllerUtility controllerUtility = new ControllerUtility();

	@RequestMapping(value = SystemConstants.LOGIN, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, String>> login(@RequestBody Credentials creds) {
		logger.info("Inside login Contoller");
		if (creds.getCustomerId() == 0 || creds.getPassword() == "") {
			logger.info("Bad Request Throwing Exception");
			throw new BadRequestException("Invalid Parameters");
		}
		logger.info("Invoking Loing Method Service");
		HashMap<String, String> result = operations.login(creds);

		return new ResponseEntity<HashMap<String, String>>(result,
				controllerUtility.getHttpResponseStatus(result.get("statusCode")));
	}

	@RequestMapping(value = SystemConstants.LOGOUT, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, String>> logout(@PathVariable(name = "customerId") int customerId,
			@PathVariable(name = "cookieToken") String cookieToken) {
		logger.info("Inside Logout Contoller");
		logger.info("Invoking Loing Logout Service");
		HashMap<String, String> result = operations.logout(customerId, cookieToken);

		return new ResponseEntity<HashMap<String, String>>(result,controllerUtility.getHttpResponseStatus(result.get("statusCode")));
	}

	@PostMapping(SystemConstants.CHANGEPASSWORD)
	public ResponseEntity<HashMap<String, String>> changePassword(@RequestBody ChangePassword changePassword) {
		if (changePassword.getCustomerId() == 0 || changePassword.getCurrentpassword() == ""
				|| changePassword.getNewPassword() == "") {
			throw new BadRequestException("Invalid Parameters");
		}
		logger.info("Inside ChangePassword Contoller");
		logger.info("Invoking Loing changePassword Service");
		HashMap<String, String> result = operations.changePassword(changePassword);
		return new ResponseEntity<HashMap<String, String>>(result, controllerUtility.getHttpResponseStatus(result.get("statusCode")));
	}

}
