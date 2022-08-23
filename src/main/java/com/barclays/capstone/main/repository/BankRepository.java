package com.barclays.capstone.main.repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;



import org.springframework.jdbc.core.JdbcTemplate;

@Repository
public class BankRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	EmailSender mail;
	
	Logger logger=LoggerFactory.getLogger(BankRepository.class);
	

	public boolean deposit(String accountNumber, int amount, String transactionId) {
		try {
			logger.info("deposit started");
			int newBalance=getbalance(accountNumber)+ amount;
			
			String query="UPDATE cust_account SET balance=? where Account_id=?";
			jdbcTemplate.update(query, new Object[] {newBalance, accountNumber});

			logger.info("balance updated");
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			
			logger.info("Updating transactions...............");
			
			jdbcTemplate.update("insert into Transactions (trans_id, trans_amount,transaction_type,trans_from,trans_to,trans_date) values (?,?,?,?,?,?)",
					new Object[] { transactionId, amount, "deposit", "Cash", accountNumber, sqlDate });

			String subject="Money deposit";
			String body=amount+" is deposited to your bank account "+accountNumber+" with transaction id: "+transactionId;
			
			String query1="Select customer_id from cust_account where Account_id="+accountNumber;
			String customerid=jdbcTemplate.queryForObject(query1, String.class);
			
			String query2="Select email from Customer where c_id="+customerid;
			String email=jdbcTemplate.queryForObject(query2, String.class);
			mail.sendEmail(email, subject, body);
			logger.info("Mail sent to user");
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public boolean cashWithdrawal(String accountNumber, int amount, String transactionId) {
		try {
			logger.info("checking current balance");
			int currentBalance = getbalance(accountNumber);
			if (currentBalance > amount) {
				jdbcTemplate.update("UPDATE cust_account SET balance=balance-? where Account_id=?",
						new Object[] { amount, accountNumber });

				logger.info("Withdrawal successfull......");
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
				Date date = new Date();
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				
				jdbcTemplate.update("insert into Transactions (trans_id, trans_amount,transaction_type,trans_from,trans_to,trans_date) values (?,?,?,?,?,?)",
						new Object[] { transactionId, amount, "withdraw", accountNumber, "cash", sqlDate });

				logger.info("Updating Transactions.......");
				
				String subject="Cash Withdraw";
				String body=amount+" is withdraw from your bank account "+accountNumber+" with transaction id: "+transactionId;
				
				String query1="Select customer_id from cust_account where Account_id="+accountNumber;
				String customerid=jdbcTemplate.queryForObject(query1, String.class);
				
				String query2="Select email from Customer where c_id="+customerid;
				String email=jdbcTemplate.queryForObject(query2, String.class);
				
				mail.sendEmail(email, subject, body);
				logger.info("Mail sent to user");
				return true;
			} else {
				logger.info("Insucficcient balance......");
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public int getbalance(String accountNumber) {
		try {
			logger.info("checking balance......");
			String query = "Select balance from cust_account where Account_id="+accountNumber;
			return jdbcTemplate.queryForObject(query, Integer.class);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return -1;
		}

	}

	public boolean transfer(String fromAccount, String toAccount, int amount, String transactionId) {
		try {
			logger.info("initiating transfer................");
			String queryFrom = "UPDATE cust_account SET balance=balance-? WHERE Account_id=?";
			jdbcTemplate.update(queryFrom, new Object[] { amount, fromAccount });

			logger.info("Amount debited from account "+ fromAccount);
			
			
			String queryTo = "UPDATE cust_account SET balance=balance+? WHERE Account_id=?";
			jdbcTemplate.update(queryTo, new Object[] { amount, toAccount });
			logger.info("Amount credited to account: "+toAccount );

			String subject="Amount debited";
			String body=amount+" is sent from your bank account "+fromAccount+" to bank account "+toAccount+" with transaction id: "+transactionId;
			
			String query1="Select customer_id from cust_account where Account_id="+fromAccount;
			String customerid=jdbcTemplate.queryForObject(query1, String.class);
			
			String query2="Select email from Customer where c_id="+customerid;
			String email=jdbcTemplate.queryForObject(query2, String.class);
			
			mail.sendEmail(email, subject, body);
			

			logger.info("Mail sent to users");
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			jdbcTemplate.update("insert into Transactions (trans_id, trans_amount, transaction_type, trans_from, trans_to, trans_date) values (?,?,?,?,?,?)",
					new Object[] { transactionId, amount, "transfer", fromAccount, toAccount, sqlDate });

			logger.info("updating transactions......");
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

}
