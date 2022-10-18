package com.barclays.capstone.main.service;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barclays.capstone.main.model.ChangePassword;
import com.barclays.capstone.main.model.Credentials;

import com.barclays.capstone.main.repository.CredentialsRepository;
import com.google.common.hash.Hashing;

/**
 * 
 * @author Divya Raisinghani, Harsh Das, Aakash Gouri Shankar
 * @Description BankAuthenticationService Business Logic
 * 
 */

@Service
public class BankAuthenticationService {

	@Autowired
	ServiceUtility serviceUtility;

	@Autowired
	CredentialsRepository credentialsRepo;

	public HashMap<String, String> login(Credentials creds) {
		HashMap<String, String> response = new HashMap<String, String>();
		String status = "False";
		String message = "Login Failed, Incorrect Username or Password!";
		String statusCode="403";
		Credentials user = credentialsRepo.findBycustomerIdAndPassword(creds.getCustomerId(),
				Hashing.sha256().hashString(creds.getPassword(), StandardCharsets.UTF_8).toString());
		if (user != null) {
			if (user.getIsNewUser() == 1) {
				status = "True";
				message = "Change your Temporary Password and Login Again!";
				response.put("success", status);
				response.put("message", message);
				response.put("statusCode", "401");
				return response;
			}
			String cookieToken = Hashing.sha256()
					.hashString(serviceUtility.generateRandomPassword(20), StandardCharsets.UTF_8).toString();
			creds.setCookieToken(cookieToken);
			creds.setPassword(Hashing.sha256().hashString(creds.getPassword(), StandardCharsets.UTF_8).toString());
			Timestamp now = Timestamp.from(Instant.now());
			creds.setCookieExpiry(now);
			credentialsRepo.save(creds);
			status = "True";
			message = "Logged in successfully";
			statusCode="200";
			response.put("token", cookieToken);
		}
		response.put("success", status);
		response.put("message", message);
		response.put("statusCode", statusCode);

		return response;
	}

	public HashMap<String, String> changePassword(ChangePassword password) {

		String status = "False";
		String message = "Falied to Change Password!";
		String statusCode="403";
		HashMap<String, String> response = new HashMap<String, String>();
		Credentials user = credentialsRepo.findBycustomerIdAndPassword(password.getCustomerId(),
				Hashing.sha256().hashString(password.getCurrentpassword(), StandardCharsets.UTF_8).toString());
		Credentials creds = new Credentials();
		if (user != null) {
			if (user.getIsNewUser() == 1) {
				creds.setIsNewUser(0);
			}
			String cookieToken = null;
			creds.setCustomerId(password.getCustomerId());
			creds.setCookieToken(cookieToken);
			creds.setPassword(
					Hashing.sha256().hashString(password.getNewPassword(), StandardCharsets.UTF_8).toString());
			credentialsRepo.save(creds);
			status = "True";
			message = "Successfully Changed Password! Login Again for Security Purpose";
			statusCode="200";
		}
		response.put("success", status);
		response.put("message", message);
		response.put("statusCode", statusCode);
		return response;
	}

	public HashMap<String, String> logout(int customerid, String cookieToken) {

		String status = "False";
		String message = "Falied to logout!";
		HashMap<String, String> response = new HashMap<String, String>();

		if (!serviceUtility.checkSession(customerid, cookieToken)) {
			status = "False";
			message = "Session Expired!";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "401");
			return response;
		} else {
			Credentials creds = credentialsRepo.findById(customerid).get();
			creds.setCookieToken(null);
			credentialsRepo.save(creds);
			message = "Logout Successful!";
			status = "True";
			response.put("statusCode", "200");
		}
		response.put("success", status);
		response.put("message", message);
		return response;
	}

}
