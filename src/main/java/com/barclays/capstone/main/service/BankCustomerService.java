package com.barclays.capstone.main.service;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.barclays.capstone.main.model.BankCustomer;
import com.barclays.capstone.main.model.CustomerRowMapper;

@Service
public  class  BankCustomerService {
	

	   private final JdbcTemplate jdbcTemplate;

	   @Autowired
	   public BankCustomerService(JdbcTemplate jdbcTemplate) {
	       this.jdbcTemplate = jdbcTemplate;
	   }
	   public Optional<BankCustomer> findById(int id) {
		  
	      String sql = "SELECT * FROM bank_customer where customerid = ?" ;
	      return jdbcTemplate.query(sql,new CustomerRowMapper(),id)
	              .stream()
	              .findFirst();
	   }

	public List<BankCustomer> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public HashMap <String, String> deleteBankCustomer(int customerId) {
		// TODO Auto-generated method stub
		
		HashMap<String, String> response = new HashMap<String, String>();
		String status = "False";
		String message = "Failed to Delete the user";
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
		if(bankCustomer.getEmail() != null && bankCustomer.getPostalAddress()!= null ) {
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
		 else if(bankCustomer.getEmail() != null && bankCustomer.getPostalAddress() == null) {
			 sql = " UPDATE bank_customer SET email=? WHERE customerid = ?";
			 if(jdbcTemplate.update(sql,bankCustomer.getEmail(),customerId) == 1 ) {
					status = "True";
					message = "Successfully updated the User Account Email";
					response.put("success", status);
					response.put("message", message);
					}
			return response;
		 }
		 else if(bankCustomer.getEmail() == null && bankCustomer.getPostalAddress() != null) {
			  sql = " UPDATE bank_customer SET postal_address=? WHERE customerid = ?";
			  if(jdbcTemplate.update(sql,bankCustomer.getPostalAddress(),customerId) == 1 ) {
					status = "True";
					message = "Successfully updated the User Postal Address";
					response.put("success", status);
					response.put("message", message);
					}
			return response;
		 }
		 else if(bankCustomer.getEmail() == null && bankCustomer.getPostalAddress() == null){
			 status = "False";
			message = "Failed to update the details. ";
				response.put("success", status);
				response.put("message", message);
			 return response;
		 }
		 else {
			 return response;
		 }
//		 String sql = " UPDATE bank_customer SET email=?, postal_address=? WHERE customerid = ?";
		  
	
	}
}
