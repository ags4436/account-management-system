package com.barclays.capstone.main.repository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.barclays.capstone.main.model.BankCustomer;
import com.barclays.capstone.main.model.ChangePassword;
import com.barclays.capstone.main.model.Login;

@Service
public class BankRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	Logger logger=LoggerFactory.getLogger(BankRepository.class);
	
	@SuppressWarnings("deprecation")
	public BankCustomer login(Login user) {
		BankCustomer customer = null;
		try {
			String query = "Select * from Customer where email=? and password=?";
//			customer = jdbcTemplate.queryForObject(query, new Object[] { user.getUserId(), user.getPassword() },
//					new BankCustomerMapper());
			logger.info("User found with given id and password");
			return customer;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return customer;
		}
	}
	
	public boolean passwordChange(ChangePassword password) {
		String query="UPDATE Customer SET password=? where c_id=?";
		try {
			jdbcTemplate.update(query,new Object[] {password.getNewPassword(),password.getUserId()});
			logger.info("password changes successfully");
			return true;
			}catch (Exception e) {
				logger.error(e.getMessage());
				return false;
			}
		}
	

	
	
	
}
