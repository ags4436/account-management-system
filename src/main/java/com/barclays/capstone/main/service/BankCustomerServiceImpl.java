package com.barclays.capstone.main.service;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.barclays.capstone.main.exception.EmailAndPostalException;
import com.barclays.capstone.main.exception.CustomerIdNotFoundException;
import com.barclays.capstone.main.model.BankCustomer;
import com.barclays.capstone.main.model.CustomerRowMapper;


@Repository
public  class BankCustomerServiceImpl implements BankCustomerService {
	

	   private final JdbcTemplate jdbcTemplate;

	   @Autowired
	   public BankCustomerServiceImpl(JdbcTemplate jdbcTemplate) {
	       this.jdbcTemplate = jdbcTemplate;
	   }
	   
	   @Override
	   public Optional<BankCustomer> findById(int id) {
//		 if(Integer.valueOf(id)!=null) {
			 
		 
	      String sql = "SELECT * FROM bank_customer where customerid = ?" ;
	      return jdbcTemplate.query(sql,new CustomerRowMapper(),id)
	              .stream()
	              .findFirst();
		 
		 
		  
	   }
	
	@Override
	public HashMap <String, String> deleteBankCustomer(int customerId) {
		// TODO Auto-generated method stub
		
		HashMap<String, String> response = new HashMap<String, String>();
		String status = "False";
		String message = "CustomerId is invalid";
		response.put("success", status);
		response.put("message", message);
		String sql = "DELETE FROM bank_customer WHERE customerid = ?";
		if(jdbcTemplate.update(sql,customerId) == 1 ) {
		status = "True";
		message = "Successfully Deleted the Account";
		response.put("success", status);
		response.put("message", message);
		}
		return response;
	}
	public HashMap <String, String> updateBankCustomer(int customerId, BankCustomer bankCustomer) {
		// TODO Auto-generated method stub
		String sql = null;
		HashMap<String, String> response = new HashMap<String, String>();
		String status = "False";
		String message = "User account with the given customerId is not found";
		response.put("success", status);
		response.put("message", message);
		String errorMessage = "";
		if(bankCustomer.getEmail() == null && bankCustomer.getPostalAddress() == null){
			 status = "False";
			message = "Failed to update the details. ";
				response.put("success", status);
				response.put("message", message);
			 return response;
		 }
		if (!Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
				.matcher(bankCustomer.getEmail()).matches()) {
			errorMessage += "Invalid Email Id\n";
		}
		/**
		 When email and Postal Address is not null
		 */
		
		if(bankCustomer.getEmail() != null && bankCustomer.getPostalAddress()!= null ) {
			if(bankCustomer.getEmail().isEmpty() && bankCustomer.getPostalAddress().isEmpty()) {
			throw new EmailAndPostalException("Input is invalid");
			}
			else if ((bankCustomer.getEmail().isEmpty()== false )&& bankCustomer.getPostalAddress().isEmpty() ) {
				if(errorMessage == "") {
				sql = " UPDATE bank_customer SET email=? WHERE customerid = ?";
				 if(jdbcTemplate.update(sql,bankCustomer.getEmail(),customerId) == 1 ) {
						status = "True";
						message = "Successfully updated the User email but postal address is invalid";
						response.put("success", status);
						response.put("message", message);
						}
				return response;
			}
				else {
					throw new EmailAndPostalException("Email is invalid");
				}
			}
			else if ((bankCustomer.getPostalAddress().isEmpty()== false ) && bankCustomer.getEmail().isEmpty() ) {
				 sql = " UPDATE bank_customer SET postal_address=? WHERE customerid = ?";
				 if(jdbcTemplate.update(sql,bankCustomer.getPostalAddress(),customerId) == 1 ) {
						status = "True";
						message = "Successfully updated the postal address but email is invalid";
						response.put("success", status);
						response.put("message", message);
						}
				return response;
			}
			
			else {
				if(errorMessage== "") {
			  sql = " UPDATE bank_customer SET email=?, postal_address=? WHERE customerid = ?";
			  if(jdbcTemplate.update(sql,bankCustomer.getEmail(),
			           bankCustomer.getPostalAddress(),customerId) == 1 ) {
					status = "True";
					message = "Successfully updated the User Account Email and Postal Address";
					response.put("success", status);
					response.put("message", message);
					}
			  return response;
			}
				else {
					sql = " UPDATE bank_customer SET postal_address=? WHERE customerid = ?";
					 if(jdbcTemplate.update(sql,bankCustomer.getPostalAddress(),customerId) == 1 ) {
							status = "True";
							message = "Successfully updated the postal address but email is invalid";
							response.put("success", status);
							response.put("message", message);
							}
					return response;
					
				}
			}
		 }
		 else if(bankCustomer.getEmail() != null && bankCustomer.getPostalAddress() == null) {
			 if(bankCustomer.getEmail().isEmpty() || errorMessage != "") {
				 throw new EmailAndPostalException("Email is invalid");
			 }
			 else {
			 sql = " UPDATE bank_customer SET email=? WHERE customerid = ?";
			 if(jdbcTemplate.update(sql,bankCustomer.getEmail(),customerId) == 1 ) {
					status = "True";
					message = "Successfully updated the User Account Email";
					response.put("success", status);
					response.put("message", message);
					}
			return response;
			 }
		 }
		 else if(bankCustomer.getEmail() == null && bankCustomer.getPostalAddress() != null) {
			 if(bankCustomer.getPostalAddress().isEmpty()) {
				 throw new EmailAndPostalException("Postal Address is invalid");
			 }
			 else {
			  sql = " UPDATE bank_customer SET postal_address=? WHERE customerid = ?";
			  if(jdbcTemplate.update(sql,bankCustomer.getPostalAddress(),customerId) == 1 ) {
					status = "True";
					message = "Successfully updated the User Postal Address";
					response.put("success", status);
					response.put("message", message);
					}
			 }
			return response;
		 }
		 
		 else {
			 return response;
		 }
//		 String sql = " UPDATE bank_customer SET email=?, postal_address=? WHERE customerid = ?";
		  
	
	}
}
