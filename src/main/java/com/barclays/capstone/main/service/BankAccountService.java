package com.barclays.capstone.main.service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.barclays.capstone.main.exception.InvalidCustomerDataException;
import com.barclays.capstone.main.model.BankAccount;
import com.barclays.capstone.main.model.BankCustomer;
import com.barclays.capstone.main.model.Credentials;
import com.barclays.capstone.main.model.ImageUpload;
import com.barclays.capstone.main.repository.AccountRepository;
import com.barclays.capstone.main.repository.CredentialsRepository;
import com.barclays.capstone.main.repository.CustomerRepository;
import com.barclays.capstone.main.repository.EmailSender;
import com.barclays.capstone.main.repository.ImageRepository;
import com.google.common.hash.Hashing;

@Service
public class BankAccountService {
	
	@Autowired
	AccountRepository accountRepo;

	@Autowired
	CredentialsRepository credentialsRepo;

	@Autowired
	ImageRepository imgRepo;
	
	@Autowired
	CustomerRepository customerRepo;
	
	@Autowired
	ServiceUtility serviceUtility;
	
	@Autowired
	BankAccount customerAccount;

	@Autowired
	Credentials creds;


	@Autowired
	EmailSender email;

	@Autowired
	ImageUpload imgUpload;

	
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

		if(ErrorMessage!="") {
			throw new InvalidCustomerDataException(ErrorMessage);
		}
		
		
	}
	
	public HashMap<String, String> isExistingCustomer(String panCard, int userId, String cookieToken) {

		String status = "True";
		String message = "No Customer Found with PAN: " + panCard;
		HashMap<String, String> response = new HashMap<String, String>();

		if (!serviceUtility.checkSession(userId, cookieToken)) {

			status = "False";
			message = "Session Expired!";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "401");
			return response;
		}

		if (!serviceUtility.isAdmin(userId, cookieToken)) {
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

	
	public HashMap<String, String> addNewCustomer(BankCustomer customer, MultipartFile multipartFile, int userId,
			String cookieToken) {

		HashMap<String, String> response = new HashMap<String, String>();
		String status = "True";
		String message = "Account Created Successfully!";
		
		if (!serviceUtility.checkSession(userId, cookieToken)) {

			status = "False";
			message = "Session Expired!";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "401");
			return response;
		}
		
		if (isExistingCustomer(customer.getPanCard(), userId, cookieToken).containsKey("Customer Id")) {
			status = "False";
			message = "Customer PAN Exits!";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "200");
			return response;
		}

		

		if (!serviceUtility.isAdmin(userId, cookieToken)) {
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

		try {
			imgUpload.setId(customer.getCustomerID());
			imgUpload.setName(multipartFile.getOriginalFilename());
			imgUpload.setType(multipartFile.getContentType());
			imgUpload.setImage(multipartFile.getBytes());
			
			imgRepo.save(imgUpload);
			
		} catch (Exception e) {
			System.out.println(e);
		}

		String TempPassword = serviceUtility.generateRandomPassword(20);

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

		if (!serviceUtility.checkSession(userId, cookieToken)) {
			status = "False";
			message = "Session Expired!";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "401");
			return response;
		}

		if (!serviceUtility.isAdmin(userId, cookieToken)) {
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
