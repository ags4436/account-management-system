package com.barclays.capstone.main.service;

import java.util.Random;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barclays.capstone.main.model.BankAccount;
import com.barclays.capstone.main.model.BankCustomer;
import com.barclays.capstone.main.model.ChangePassword;
import com.barclays.capstone.main.model.Login;
import com.barclays.capstone.main.repository.BankRepository;
import com.barclays.capstone.main.repository.EmailSender;

@Service
public class BankServices {

	@Autowired
	BankAccount customerAccount;

	@Autowired
	BankRepository repo;
	
	@Autowired
	EmailSender email;

	public BankCustomer login(Login user) {
		return repo.login(user);
	}

	public String changePassword(ChangePassword password) {
		if (repo.passwordChange(password)) {
			return "Password changed successfully";
		} else {
			return "Password is not changed due to some error please try again";
		}
	}

	public Boolean isExistingCustomer(String pan) {
		if (pan.equals("CVHPA9745N")) {
			return true;
		}
		return false;
	}

	public String generateCustomerId() {
		Random random = new Random();
		return String.format("%06d", random.nextInt(10000));
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

	public String createAccount(BankCustomer customer) {
		validateCustomerDetails(customer);
		customer.setCustomerID(generateCustomerId());
		String accountNumber = "2663" + String.format("%06d", 1); //use count(*) + 1 
		customerAccount.setAccountNumber(accountNumber);
		//email.sendEmail("va4436@srmist.edu.in","TestMail","Email Testing!");
		return accountNumber;
	}

}
