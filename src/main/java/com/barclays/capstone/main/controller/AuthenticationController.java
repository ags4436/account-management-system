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

	@Autowired
	BankAuthenticationService operations;

	Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@RequestMapping(value = SystemConstants.LOGIN, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, String>> login(@RequestBody Credentials creds) {
		if (creds.getCustomerId() == 0 || creds.getPassword() == "") {
			throw new BadRequestException("Invalid Parameters");
		}
		logger.info("Loging in user..........");
		HashMap<String, String> result = operations.login(creds);

		return new ResponseEntity<HashMap<String, String>>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = SystemConstants.LOGOUT, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, String>> logout(@PathVariable(name = "customerId") int customerId,@PathVariable(name = "cookieToken") String cookieToken) {
	
		HashMap<String, String> result = operations.logout(customerId,cookieToken);

		return new ResponseEntity<HashMap<String, String>>(result, HttpStatus.OK);
	}

	@PostMapping(SystemConstants.CHANGEPASSWORD)
	public ResponseEntity<HashMap<String, String>> changePassword(@RequestBody ChangePassword changePassword) {
		if (changePassword.getCustomerId() == 0 || changePassword.getCurrentpassword() == "" || changePassword.getNewPassword()=="") {
			throw new BadRequestException("Invalid Parameters");
		}
		logger.info("Changing password.....................");
		HashMap<String, String> result = operations.changePassword(changePassword);
		return new ResponseEntity<HashMap<String, String>>(result, HttpStatus.OK);
	}

}
