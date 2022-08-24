package com.barclays.capstone.main.service;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barclays.capstone.main.model.BankCustomer;
import com.barclays.capstone.main.model.Credentials;
import com.barclays.capstone.main.repository.CredentialsRepository;
import com.barclays.capstone.main.repository.CustomerRepository;

@Service
public class ServiceUtility {

	@Autowired
	CredentialsRepository credentialsRepo;

	@Autowired
	CustomerRepository customerRepo;

	public Boolean isAdmin(int userId, String cookieToken) {
		Optional<BankCustomer> user = customerRepo.findById(userId);
		if (user.get().getRole().equalsIgnoreCase("manager"))
			return true;
		return false;
	}

	public Boolean checkSession(int customerId, String cookieToken) {

		Credentials user = credentialsRepo.findByCustomerIdAndCookieToken(customerId, cookieToken);
		if (user != null) {
			long timeDiff = (Timestamp.from(Instant.now()).getTime() - user.getCookieExpiry().getTime()) / 60000;
			System.out.println(timeDiff);
			if (timeDiff > 10) {
				System.out.println((Timestamp.from(Instant.now()).getTime() - user.getCookieExpiry().getTime()));
				return false;
			}
		} else {
			return false;
		}

		return true;

	}

	public String generateRandomPassword(int len) {
		// ASCII range – alphanumeric (0-9, a-z, A-Z)
		final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < len; i++) {
			int randomIndex = random.nextInt(chars.length());
			sb.append(chars.charAt(randomIndex));
		}

		return sb.toString();
	}

}
