package com.barclays.capstone.main.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.barclays.capstone.main.constants.SystemConstants;
import com.barclays.capstone.main.model.ChangePassword;
import com.barclays.capstone.main.model.Credentials;
import com.barclays.capstone.main.service.BankAuthenticationService;

@RestController
@RequestMapping("/account")
public class AuthenticationController {
	
	@Autowired
	BankAuthenticationService operations;
	
	Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	
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

}
