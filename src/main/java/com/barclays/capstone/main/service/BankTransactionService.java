package com.barclays.capstone.main.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barclays.capstone.main.controller.ControllerUtility;
import com.barclays.capstone.main.model.BankAccount;
import com.barclays.capstone.main.model.Transaction;
import com.barclays.capstone.main.repository.AccountRepository;
import com.barclays.capstone.main.repository.CustomerRepository;
import com.barclays.capstone.main.repository.TransactionRepository;

/**
 * 
 * @author Akash Salave, Shubham Chokhani, Aakash Gouri Shankar, Rameshwari Joshi

 * @Description Business logic for Transactions.
 * 
 */

@Service
public class BankTransactionService {

	@Autowired
	AccountRepository accountRepo;

	@Autowired
	TransactionRepository transactionRepo;

	@Autowired
	CustomerRepository customerRepo;

	@Autowired
	ServiceUtility serviceUtility;

	@Autowired
	EmailSender email;

	ControllerUtility controllerUtility = new ControllerUtility();

	Logger logger = LoggerFactory.getLogger(BankTransactionService.class);

	/**
	 * 
	 * @param accountNumber
	 * @param amount
	 * @param userId
	 * @param cookieToken
	 */
	public HashMap<String, String> deposit(String accountNumber, int amount, int userId, String cookieToken) {
		String status = "True";
		String message = "Deposit Successfull";
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
			if (accountRepo.findById(accountNumber).get().getCustomerId() != userId) {
				status = "False";
				message = "Forbidden";
				response.put("success", status);
				response.put("message", message);
				response.put("statusCode", "403");
				return response;
			}
		}

		try {
			logger.info("deposit started");
			BankAccount customerAccount = accountRepo.findById(accountNumber).get();
			customerAccount.setCurrentBalance(customerAccount.getCurrentBalance() + amount);
			String transactionId = generateTransactionId();

			accountRepo.save(customerAccount);

			logger.info("balance updated");

			long millis = System.currentTimeMillis();
			java.sql.Date transactionTimestamp = new java.sql.Date(millis);

			logger.info("Updating transactions...............");
			Transaction newTransaction = new Transaction();
			newTransaction.setAmount(amount);
			newTransaction.setToAccountNumber(accountNumber);
			newTransaction.setFromAccountNumber("Cash");
			newTransaction.setTransactionReferenceNumber(transactionId);
			newTransaction.setTransactionDate(transactionTimestamp);

			transactionRepo.save(newTransaction);

			String subject = "Deposit Receipt Transaction Id [" + transactionId + "]";
			String body = "Dear Customer, \n\n " + "₹" + amount + " is deposited to your Bank Account. "
					+ "\n\n Account Number: " + accountNumber + "\n Transaction Id: " + transactionId
					+ "\n Available Balance: ₹" + customerAccount.getCurrentBalance() + "\n\nHappy Banking! "
					+ "\nRegards, \nTeam BBI Pune";

			int customerid = customerAccount.getCustomerId();

			String mail = customerRepo.findById(customerid).get().getEmail();

			email.sendEmail(mail, subject, body);
			logger.info("Mail sent to user");

			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "200");

			return response;

		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 
	 * @param accountNumber
	 * @param amount
	 * @param userId
	 * @param cookieToken
	 * @return
	 */
	public HashMap<String, String> cashWithdrawal(String accountNumber, int amount, int userId, String cookieToken) {
		try {

			String status = "True";
			String message = "cash Withdrawal Successfull";
			HashMap<String, String> response = new HashMap<String, String>();

			if (!serviceUtility.checkSession(userId, cookieToken)) {
				status = "False";
				message = "Session Expired!";
				response.put("success", status);
				response.put("message", message);
				response.put("statusCode", "401");
				return response;
			}
			
			
			if (accountRepo.findById(accountNumber).isPresent()!=true || accountRepo.findById(accountNumber).get().getCustomerId() != userId) {
				status = "False";
				message = "Invalid Account Number";
				response.put("success", status);
				response.put("message", message);
				response.put("statusCode", "403");
				return response;
			}

			logger.info("checking current balance");

			long millis = System.currentTimeMillis();
			java.sql.Date transactionTimestamp = new java.sql.Date(millis);

			BankAccount customerAccount = accountRepo.findById(accountNumber).get();
			int currentBalance = customerAccount.getCurrentBalance();
			System.out.println("Check1");
			List<Transaction> customersTodaysTransactions = transactionRepo.findByTransaction(accountNumber, "cash",
					transactionTimestamp);

			int numberOfTodaysWithdrawal = customersTodaysTransactions.size();
			int totalWithdrawal = 0;

			for (int i = 0; i < numberOfTodaysWithdrawal; i++)
				totalWithdrawal += customersTodaysTransactions.get(i).getAmount();
			System.out.println("Check2");

			if (currentBalance > amount && totalWithdrawal + amount <= 10000) {
				customerAccount.setCurrentBalance(customerAccount.getCurrentBalance() - amount);
				accountRepo.save(customerAccount);
				String transactionId = generateTransactionId();

				logger.info("Withdrawal successfull......");
				logger.info("Updating Transactions.......");

				Transaction newTransaction = new Transaction();
				newTransaction.setAmount(amount);
				newTransaction.setFromAccountNumber(accountNumber);
				newTransaction.setToAccountNumber("Cash");
				newTransaction.setTransactionReferenceNumber(transactionId);
				newTransaction.setTransactionDate(transactionTimestamp);

				transactionRepo.save(newTransaction);

				String subject = "Cash Withdrawn Transaction Id: [" + transactionId + "]";
				String body = "Dear Customer, \n\n " + "₹" + amount + " is withdraw from your Bank Account. "
						+ "\n\n Account Number: " + accountNumber + "\n Transaction Id: " + transactionId
						+ "\n Available Balance: ₹" + customerAccount.getCurrentBalance() + "\n\nHappy Banking! "
						+ "\nRegards, \nTeam BBI Pune";

				int customerid = customerAccount.getCustomerId();

				String mail = customerRepo.findById(customerid).get().getEmail();

				email.sendEmail(mail, subject, body);
				logger.info("Mail sent to user");

				response.put("success", status);
				response.put("message", message);
				response.put("statusCode", "200");
				return response;
			} else {
				message = "Insucficcient balance or you have reached today's limit";
				status = "False";
				response.put("success", status);
				response.put("message", message);
				response.put("statusCode", "200");
				return response;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 
	 * @param fromAccount
	 * @param toAccount
	 * @param amount
	 * @param userId
	 * @param cookieToken
	 * @return
	 */
	public HashMap<String, String> transfer(String fromAccount, String toAccount, int amount, int userId,
			String cookieToken) {
		try {

			String status = "True";
			String message = "Transferd Successfully";
			HashMap<String, String> response = new HashMap<String, String>();

			if (!serviceUtility.checkSession(userId, cookieToken)) {

				status = "False";
				message = "Session Expired!";
				response.put("success", status);
				response.put("message", message);
				response.put("statusCode", "401");
				return response;
			}

			if (accountRepo.findById(fromAccount).isPresent()!=true || accountRepo.findById(fromAccount).get().getCustomerId() != userId) {
				status = "False";
				message = "Your Account Number is Invaild ";
				response.put("success", status);
				response.put("message", message);
				response.put("statusCode", "403");
				return response;
			}

			logger.info("initiating transfer................");

			BankAccount customerAccountSender = accountRepo.findById(fromAccount).get();
			int currentBalanceSender = customerAccountSender.getCurrentBalance();
			if (currentBalanceSender >= amount) {
				customerAccountSender.setCurrentBalance(currentBalanceSender - amount);
				accountRepo.save(customerAccountSender);
				String transactionId = generateTransactionId();

				BankAccount customerAccountReciever = accountRepo.findById(toAccount).get();
				int currentBalanceReciever = customerAccountReciever.getCurrentBalance();
				customerAccountReciever.setCurrentBalance(currentBalanceReciever + amount);
				accountRepo.save(customerAccountReciever);

				logger.info("Amount debited from account " + fromAccount);
				logger.info("Amount credited to account: " + toAccount);

				long millis = System.currentTimeMillis();
				java.sql.Date date = new java.sql.Date(millis);

				logger.info("Updating transactions...............");
				Transaction newTransaction = new Transaction();
				newTransaction.setAmount(amount);
				newTransaction.setToAccountNumber(toAccount);
				newTransaction.setFromAccountNumber(fromAccount);
				newTransaction.setTransactionReferenceNumber(transactionId);
				newTransaction.setTransactionDate(date);

				transactionRepo.save(newTransaction);

				String subjectDebit = "Bank Transfer Transaction Id: [" + transactionId + "]";

				String bodySender = "Dear Customer, \n\n " + "₹" + amount + " is Transfered from your Bank Account."
						+ "\n\n Beneficiary Account Number: " + toAccount + "\n Transaction Id: " + transactionId
						+ "\n Your Available Balance: ₹" + customerAccountSender.getCurrentBalance()
						+ "\n\nHappy Banking! " + "\nRegards, \nTeam BBI Pune";

				int customeridSender = customerAccountSender.getCustomerId();
				String mailSender = customerRepo.findById(customeridSender).get().getEmail();

				int customeridReciever = customerAccountReciever.getCustomerId();
				String mailReciever = customerRepo.findById(customeridReciever).get().getEmail();

				String subjectCredit = "Credit Details Bank Transfer Transaction Id: [" + transactionId + "]";

				String bodyReciever = "Dear Customer, \n\n " + "₹" + amount + " is Credited to your bank account"
						+ "\n\n Account Number: " + toAccount + "\n Transaction Id: " + transactionId
						+ "\n Your Available Balance: ₹" + customerAccountReciever.getCurrentBalance()
						+ "\n\nHappy Banking! " + "\nRegards, \nTeam BBI Pune";

				email.sendEmail(mailSender, subjectDebit, bodySender);

				email.sendEmail(mailReciever, subjectCredit, bodyReciever);

				logger.info("Mail sent to users");

				message = "Transfering funds is successfull. TransactionId " + transactionId;
			} else {
				status = "False";
				message = "Transfering fund is not done due to insufficient funds";
			}

			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "200");

			return response;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 
	 * @param exportData
	 * @param header
	 * @param accountNumber
	 * @param fromdate
	 * @param toDate
	 * @throws IOException
	 */
	public void writeToCsv(List<Transaction> exportData, String header, String accountNumber, String fromdate,
			String toDate) throws IOException {
		String Path = "H:\\Barclays Training\\" + accountNumber;
		String fileName = serviceUtility.generateRandomPassword(10) + " " + fromdate + " to " + toDate + ".csv";
		File theDir = new File(Path);
		if (!theDir.exists()) {
			theDir.mkdirs();
		}

		PrintWriter writer = new PrintWriter(Path + "\\" + fileName);
		writer.println(header);

		for (Transaction data : exportData) {
			writer.println(data.toString());
		}
		writer.close();
	}

	/**
	 * 
	 * @param accountNumber
	 * @param fromTransactionDate
	 * @param toTransactionDate
	 * @param userId
	 * @param cookieToken
	 * @return
	 */
	public HashMap<String, String> exportTransaction(String accountNumber, Date fromTransactionDate,
			Date toTransactionDate, int userId, String cookieToken) {
		try {

			String status = "True";
			String message = "Exported Successfully";
			HashMap<String, String> response = new HashMap<String, String>();

			if (!serviceUtility.checkSession(userId, cookieToken)) {

				status = "False";
				message = "Session Expired!";
				response.put("success", status);
				response.put("message", message);
				response.put("statusCode", "401");
				return response;
			}

			if (accountRepo.findById(accountNumber).get().getCustomerId() != userId) {
				status = "False";
				message = "Your Account Number is Invaild ";
				response.put("success", status);
				response.put("message", message);
				response.put("statusCode", "403");
				return response;
			}

			List<Transaction> exportData = transactionRepo.findTransactionByDate(accountNumber, fromTransactionDate,
					toTransactionDate);
			writeToCsv(exportData,
					"transaction_reference_number,amount,from_account_number,to_account_number,transaction_date",
					accountNumber, String.valueOf(fromTransactionDate), String.valueOf(toTransactionDate));
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "200");

			return response;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	/**
	 * 
	 * @param accountNumber
	 * @param userId
	 * @param cookieToken
	 * @return
	 */
	public HashMap<String, String> miniStatement(String accountNumber, int userId, String cookieToken) {
		try {

			String status = "True";
			String message = "MiniStatemenet Generated Successfully!";
			HashMap<String, String> response = new HashMap<String, String>();

			if (!serviceUtility.checkSession(userId, cookieToken)) {

				status = "False";
				message = "Session Expired!";
				response.put("success", status);
				response.put("message", message);
				response.put("statusCode", "401");
				return response;
			}

			if (accountRepo.findById(accountNumber).get().getCustomerId() != userId) {
				status = "False";
				message = "Your Account Number is Invaild ";
				response.put("success", status);
				response.put("message", message);
				response.put("statusCode", "403");
				return response;
			}

			String miniStatement = "";
			List<Transaction> trans = transactionRepo.miniStatement(accountNumber);

			for (Transaction transObj : trans) {
				miniStatement += transObj.toString() + " || ";
			}

			response.put("miniStatement", miniStatement);
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "200");

			return response;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 
	 * @return
	 */
	public String generateTransactionId() {
		while (true) {
			String transactionId = "BBIPUNE" + String.valueOf((int) (Math.random() * (99999 - 10000 + 1) + 10000))
					+ Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE).replace("-", "");
			if (!transactionRepo.findById(transactionId).isPresent()) {
				return transactionId;
			}
		}

	}

}
