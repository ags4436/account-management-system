package com.barclays.capstone.main.model;

import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;


public class CustomerRowMapper implements RowMapper<BankCustomer> {
	  @Override
	   public BankCustomer mapRow(ResultSet rs, int rowNum) throws SQLException {
	       return new BankCustomer(
	               rs.getInt("customerid"),
	               rs.getString("aadhar_number"),
	               rs.getString("customer_nname"),
	               rs.getString("dob"),
	               rs.getString("email"),
	               rs.getString("pan_card"),
	               rs.getString("postal_address"),
	               rs.getString("role")
	           
	        
	       
       
	       );
	   }
}
