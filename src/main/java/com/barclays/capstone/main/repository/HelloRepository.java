package com.barclays.capstone.main.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.barclays.capstone.main.model.BankAccount;


import org.springframework.jdbc.core.JdbcTemplate;

public class HelloRepository {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	Logger logger=LoggerFactory.getLogger(HelloRepository.class);
	
	
	public List<BankAccount> viewAccounts(String pancard) {
		List<BankAccount> accounts = new ArrayList<>();
		try {
			logger.info("Fetching list of account linked with pancard "+pancard);
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(
					"Select * from cust_account where customerId in (Select c_id from Customer where pan=?)",
					new Object[] { pancard });

			for(Map<String, Object> row: rows) {
				BankAccount bankAccount=new BankAccount();
				bankAccount.setAccountNumber((long)row.get("AccountNumber"));
				bankAccount.setCustomerId((int)row.get("CustomerId"));
				bankAccount.setCurrentBalance((int)row.get("CurrentBalance"));
				accounts.add(bankAccount);
			}

			logger.info("returning list of account linked with pancard");
			return accounts;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return accounts;
		}
	}
}
