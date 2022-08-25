package com.barclays.capstone.main.service;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.barclays.capstone.main.exception.EmailAndPostalException;
import com.barclays.capstone.main.model.BankCustomer;
import com.barclays.capstone.main.model.CustomerRowMapper;

/**
 * @author Roopa Amrutha, Shipra Saini, Rutuja Shukla
 * @Description Business logic for View, Update and Delete Customers Details.
 * 
 *
 */

@Service
public class BankCustomerService {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	ServiceUtility serviceUtility;

	@Autowired
	public BankCustomerService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 
	 * @param customerId
	 * @param userId
	 * @param cookieToken
	 * @return
	 */
	public HashMap<String, String> findById(int customerId, int userId, String cookieToken) {

		HashMap<String, String> response = new HashMap<String, String>();
		String status = "True";
		String message = "Account Created Successfully!";

		if (!serviceUtility.checkSession(customerId, cookieToken)) {
			status = "False";
			message = "Session Expired!";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "401");
			return response;
		}

		if (!serviceUtility.isAdmin(customerId, cookieToken)) {
			if (userId != customerId) {
				status = "False";
				message = "Forbidden";
				response.put("success", status);
				response.put("message", message);
				response.put("statusCode", "403");
				return response;
			}
		}

		String sql = "SELECT * FROM bank_customer where customerid = ?";
		response.put("Customer Data:",
				jdbcTemplate.query(sql, new CustomerRowMapper(), customerId).stream().findFirst().get().toString());

		return response;
	}

	public List<BankCustomer> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public HashMap<String, String> deleteBankCustomer(int customerId, int userId, String cookieToken) {
		// TODO Auto-generated method stub

		HashMap<String, String> response = new HashMap<String, String>();
		String status = "False";
		String message = "Failed to Delete the user";

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

		String sqlCustomer = "DELETE FROM bank_customer WHERE customerid = ?";
		String sqlAccount = "DELETE FROM bank_account WHERE customer_id = ?";
		if (jdbcTemplate.update(sqlCustomer, customerId) == 1 || jdbcTemplate.update(sqlAccount, customerId) == 1) {
			status = "True";
			message = "Successfully Deleted the Account";
			response.put("success", status);
			response.put("message", message);
		}
		response.put("success", status);
		response.put("message", message);
		return response;
	}

	public HashMap<String, String> updateBankCustomer(BankCustomer bankCustomer, int customerId, String cookieToken) {
		// TODO Auto-generated method stub
		String sql = null;
		HashMap<String, String> response = new HashMap<String, String>();
		String status = "False";
		String message = "User account with the given customerId is not found";
		response.put("success", status);
		response.put("message", message);

		if (!serviceUtility.checkSession(customerId, cookieToken)) {
			status = "False";
			message = "Session Expired!";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "401");
			return response;
		}

		if (!serviceUtility.isAdmin(customerId, cookieToken)) {
			if (bankCustomer.getCustomerID() != customerId) {
				status = "False";
				message = "Forbidden";
				response.put("success", status);
				response.put("message", message);
				response.put("statusCode", "403");
				return response;
			}
		}

		if (bankCustomer.getPostalAddress() == "") {
			throw new EmailAndPostalException("Invalid Postal Address");
		}

		if (!Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
				.matcher(bankCustomer.getEmail()).matches()) {
			throw new EmailAndPostalException("Invalid Email Address");
		}
		
		if (bankCustomer.getEmail() != null && bankCustomer.getPostalAddress() != null) {
			sql = " UPDATE bank_customer SET email=?, postal_address=? WHERE customerid = ?";
			if (jdbcTemplate.update(sql, bankCustomer.getEmail(), bankCustomer.getPostalAddress(),
					bankCustomer.getCustomerID()) == 1) {
				status = "True";
				message = "Successfully updated the User Account Email and Postal Address";
				response.put("success", status);
				response.put("message", message);
			}
			return response;
		} else if (bankCustomer.getEmail() != null && bankCustomer.getPostalAddress() == null) {
			sql = " UPDATE bank_customer SET email=? WHERE customerid = ?";
			if (jdbcTemplate.update(sql, bankCustomer.getEmail(), bankCustomer.getCustomerID()) == 1) {
				status = "True";
				message = "Successfully updated the User Account Email";
				response.put("success", status);
				response.put("message", message);
			}
			return response;
		} else if (bankCustomer.getEmail() == null && bankCustomer.getPostalAddress() != null) {
			sql = " UPDATE bank_customer SET postal_address=? WHERE customerid = ?";
			if (jdbcTemplate.update(sql, bankCustomer.getPostalAddress(), bankCustomer.getCustomerID()) == 1) {
				status = "True";
				message = "Successfully updated the User Postal Address";
				response.put("success", status);
				response.put("message", message);
			}
			return response;
		} else if (bankCustomer.getEmail() == null && bankCustomer.getPostalAddress() == null) {
			status = "False";
			message = "Failed to update the details. ";
			response.put("success", status);
			response.put("message", message);
			return response;
		} else {
			return response;
		}

	}
}
