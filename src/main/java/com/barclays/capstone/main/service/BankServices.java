package com.barclays.capstone.main.service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barclays.capstone.main.model.BankAccount;
import com.barclays.capstone.main.model.BankCustomer;
import com.barclays.capstone.main.model.ChangePassword;
import com.barclays.capstone.main.model.Credentials;
import com.barclays.capstone.main.repository.AccountRepository;
import com.barclays.capstone.main.repository.BankRepository;
import com.barclays.capstone.main.repository.CredentialsRepository;
import com.barclays.capstone.main.repository.CustomerRepository;
import com.barclays.capstone.main.repository.EmailSender;
import com.google.common.hash.Hashing;

@Service
public class BankServices {

	@Autowired
	BankAccount customerAccount;

	@Autowired
	Credentials creds;

	@Autowired
	BankRepository repo;

	@Autowired
	EmailSender email;

	@Autowired
	CustomerRepository customerRepo;

	@Autowired
	AccountRepository accountRepo;

	@Autowired
	CredentialsRepository credentialsRepo;

	public HashMap<String, String> login(Credentials creds) {
		HashMap<String, String> response = new HashMap<String, String>();
		String status = "False";
		String message = "Login Failed, Incorrect Username or Password!";
		Credentials user = credentialsRepo.findBycustomerIdAndPassword(creds.getCustomerId(),
				Hashing.sha256().hashString(creds.getPassword(), StandardCharsets.UTF_8).toString());
		if (user != null) {
			if (user.getIsNewUser() == 1) {
				status = "True";
				message = "Change your Temporary Password and Login Again!";
				response.put("success", status);
				response.put("message", message);
				return response;
			}
			String cookieToken = Hashing.sha256().hashString(generateRandomPassword(20), StandardCharsets.UTF_8)
					.toString();
			creds.setCookieToken(cookieToken);
			creds.setPassword(Hashing.sha256().hashString(creds.getPassword(), StandardCharsets.UTF_8).toString());
			Timestamp now = Timestamp.from(Instant.now());
			creds.setCookieExpiry(now);
			credentialsRepo.save(creds);
			status = "True";
			message = "Logged in successfully";
			response.put("token", cookieToken);
		}
		response.put("success", status);
		response.put("message", message);

		return response;
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

	public HashMap<String, String> changePassword(ChangePassword password) {

		String status = "False";
		String message = "Falied to Change Password!";
		HashMap<String, String> response = new HashMap<String, String>();
		Credentials user = credentialsRepo.findBycustomerIdAndPassword(password.getCustomerId(),
				Hashing.sha256().hashString(password.getCurrentpassword(), StandardCharsets.UTF_8).toString());
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
		}
		response.put("success", status);
		response.put("message", message);
		return response;
	}

	public HashMap<String, String> isExistingCustomer(String panCard,int userId, String cookieToken) {
		
		String status = "True";
		String message = "No Customer Found with PAN: " + panCard;
		HashMap<String, String> response = new HashMap<String, String>();
		
		if (!checkSession(userId, cookieToken)) {

			status = "False";
			message = "Session Expired!";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "401");
			return response;
		}

		if (!isAdmin(userId, cookieToken)) {
			status = "False";
			message = "Forbidden";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "403");
			return response;
		}
		
		BankCustomer customer = customerRepo.findBypanCard(panCard);
		if (customer != null) {
			status = "true";
			message = "Customer Found with PAN: " + panCard;
			response.put("Customer Name", customer.getCustomerName());
			response.put("Aadhar Number", customer.getAadharNumber());
			response.put("Customer Id", String.valueOf(customer.getCustomerID()));
		}
		response.put("success", status);
		response.put("message", message);
		response.put("statusCode", "200");
		return response;
	}

	public int generateCustomerId() {
		Random random = new Random();
		while (true) {
			int CustomerId = Integer.parseInt("1" + String.format("%05d", random.nextInt(10000)));
			if (!customerRepo.findById(CustomerId).isPresent())
				return CustomerId;
		}

	}

	public void validateCustomerDetails(BankCustomer customer) {

		String ErrorMessage = "";

		if (!Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}").matcher(customer.getPanCard()).matches())
			ErrorMessage += "Invalid PAN\n";
		if (!Pattern.compile("[0-9]{12}").matcher(customer.getAadharNumber()).matches())
			ErrorMessage += "Invalid Aadhar Number\n";
		if (!Pattern.compile("^[a-zA-Z\\s]+").matcher(customer.getCustomerName()).matches())
			ErrorMessage += "Invalid Name\n";
		if (!Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
				.matcher(customer.getEmail()).matches())
			ErrorMessage += "Invalid Email Id\n";
		if (!Pattern.compile("\\d\\d/\\d\\d/\\d\\d\\d\\d").matcher(customer.getDob()).matches())
			ErrorMessage += "Invalid DOB :: Format DD/MM/YYYY\n";

		System.out.println(ErrorMessage);
	}

	public static String generateRandomPassword(int len) {
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

	public Boolean isAdmin(int userId, String cookieToken) {
		Optional<BankCustomer> user = customerRepo.findById(userId);
		if (user.get().getRole().equalsIgnoreCase("manager"))
			return true;
		return false;
	}

	public HashMap<String, String> addNewCustomer(BankCustomer customer, int userId, String cookieToken) {

		HashMap<String, String> response = new HashMap<String, String>();
		String status = "True";
		String message = "Account Created Successfully!";

		if (isExistingCustomer(customer.getPanCard(),userId,cookieToken).containsKey("Customer Id")) {
			status = "False";
			message = "Customer PAN Exits!";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "200");
			return response;
		}

		if (!checkSession(userId, cookieToken)) {

			status = "False";
			message = "Session Expired!";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "401");
			return response;
		}

		if (!isAdmin(userId, cookieToken)) {
			status = "False";
			message = "Forbidden";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "403");
			return response;
		}

		validateCustomerDetails(customer);
		customer.setCustomerID(generateCustomerId());
		String accountNumber = "2663" + String.format("%06d", accountRepo.count() + 1);
		customerAccount.setAccountNumber(accountNumber);
		customerAccount.setCustomerId(customer.getCustomerID());
		customerAccount.setCurrentBalance(0);
		customerRepo.save(customer);
		accountRepo.save(customerAccount);
		String TempPassword = generateRandomPassword(20);

		String mailBody = "Hello " + customer.getCustomerName() + "!\n"
				+ "Welcome to BBI, Please find your Account Details.\n" + "\n Account Number: "
				+ customerAccount.getAccountNumber() + "\n Customer Id: " + customer.getCustomerID()
				+ "\n Temporary Password: " + TempPassword + "\n\nHappy Banking! "
				+ "\nWith Regards, \nBranch Manager\nBBI Pune ";

		creds.setIsNewUser(1);
		creds.setCustomerId(customer.getCustomerID());
		creds.setPassword(Hashing.sha256().hashString(TempPassword, StandardCharsets.UTF_8).toString());
		credentialsRepo.save(creds);

		email.sendEmail(customer.getEmail(), "BBI Pune Welcomes you!", mailBody);

		response.put("success", status);
		response.put("message", message);
		response.put("account Number", accountNumber);
		response.put("statusCode", "201");
		return response;
	}

	public HashMap<String, String> createAccount(BankAccount customer, int userId, String cookieToken) {

		HashMap<String, String> response = new HashMap<String, String>();
		String status = "True";
		String message = "Account Created Successfully!";

		if (!checkSession(userId, cookieToken)) {
			status = "False";
			message = "Session Expired!";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "401");
			return response;
		}

		if (!isAdmin(userId, cookieToken)) {
			status = "False";
			message = "Forbidden";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "403");
			return response;
		}

		if (!customerRepo.findById(customer.getCustomerId()).isPresent()) {
			status = "False";
			message = "Invaild CustomerId";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "403");
			return response;
		}

		String accountNumber = "2663" + String.format("%06d", accountRepo.count() + 1);
		customerAccount.setAccountNumber(accountNumber);
		customerAccount.setCustomerId(customer.getCustomerId());
		customerAccount.setCurrentBalance(0);
		accountRepo.save(customerAccount);

		response.put("success", status);
		response.put("message", message);
		response.put("account Number", accountNumber);
		response.put("statusCode", "201");
		return response;
	}

}
