package com.barclays.capstone.main.service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barclays.capstone.main.model.BankAccount;
import com.barclays.capstone.main.model.BankCustomer;
import com.barclays.capstone.main.model.ChangePassword;
import com.barclays.capstone.main.model.Credentials;
import com.barclays.capstone.main.model.Transaction;
import com.barclays.capstone.main.repository.AccountRepository;
import com.barclays.capstone.main.repository.BankRepository;
import com.barclays.capstone.main.repository.CredentialsRepository;
import com.barclays.capstone.main.repository.CustomerRepository;
import com.barclays.capstone.main.repository.EmailSender;
import com.barclays.capstone.main.repository.TransactionRepository;
import com.google.common.hash.Hashing;

@Service
public class BankServices {

	@Autowired
	BankAccount customerAccount;

	@Autowired
	Credentials creds;

	@Autowired
	BankRepository repo;

	@Autowired
	EmailSender email;

	@Autowired
	CustomerRepository customerRepo;

	@Autowired
	AccountRepository accountRepo;

	@Autowired
	CredentialsRepository credentialsRepo;
	
	@Autowired
	TransactionRepository transactionRepo;
	

	Logger logger=LoggerFactory.getLogger(BankRepository.class);

	public HashMap<String, String> login(Credentials creds) {
		HashMap<String, String> response = new HashMap<String, String>();
		String status = "False";
		String message = "Login Failed, Incorrect Username or Password!";
		Credentials user = credentialsRepo.findBycustomerIdAndPassword(creds.getCustomerId(),
				Hashing.sha256().hashString(creds.getPassword(), StandardCharsets.UTF_8).toString());
		if (user != null) {
			if (user.getIsNewUser() == 1) {
				status = "True";
				message = "Change your Temporary Password and Login Again!";
				response.put("success", status);
				response.put("message", message);
				return response;
			}
			String cookieToken = Hashing.sha256().hashString(generateRandomPassword(20), StandardCharsets.UTF_8)
					.toString();
			creds.setCookieToken(cookieToken);
			creds.setPassword(Hashing.sha256().hashString(creds.getPassword(), StandardCharsets.UTF_8).toString());
			Timestamp now = Timestamp.from(Instant.now());
			creds.setCookieExpiry(now);
			credentialsRepo.save(creds);
			status = "True";
			message = "Logged in successfully";
			response.put("token", cookieToken);
		}
		response.put("success", status);
		response.put("message", message);

		return response;
	}

	public Boolean checkSession(int customerId, String cookieToken) {

		Credentials user = credentialsRepo.findByCustomerIdAndCookieToken(customerId, cookieToken);
		if (user != null) {
			long timeDiff = (Timestamp.from(Instant.now()).getTime() - user.getCookieExpiry().getTime()) / 60000;
			System.out.println(timeDiff);
			if (timeDiff > 10) {
				System.out.println((Timestamp.from(Instant.now()).getTime() - user.getCookieExpiry().getTime()));
				return false;
			}
		} else {
			return false;
		}

		return true;

	}

	public HashMap<String, String> changePassword(ChangePassword password) {

		String status = "False";
		String message = "Falied to Change Password!";
		HashMap<String, String> response = new HashMap<String, String>();
		Credentials user = credentialsRepo.findBycustomerIdAndPassword(password.getCustomerId(),
				Hashing.sha256().hashString(password.getCurrentpassword(), StandardCharsets.UTF_8).toString());
		if (user != null) {
			if (user.getIsNewUser() == 1) {
				creds.setIsNewUser(0);
			}
			String cookieToken = null;
			creds.setCustomerId(password.getCustomerId());
			creds.setCookieToken(cookieToken);
			creds.setPassword(
					Hashing.sha256().hashString(password.getNewPassword(), StandardCharsets.UTF_8).toString());
			credentialsRepo.save(creds);
			status = "True";
			message = "Successfully Changed Password! Login Again for Security Purpose";
		}
		response.put("success", status);
		response.put("message", message);
		return response;
	}

	public HashMap<String, String> isExistingCustomer(String panCard,int userId, String cookieToken) {
		
		String status = "True";
		String message = "No Customer Found with PAN: " + panCard;
		HashMap<String, String> response = new HashMap<String, String>();
		
		if (!checkSession(userId, cookieToken)) {

			status = "False";
			message = "Session Expired!";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "401");
			return response;
		}

		if (!isAdmin(userId, cookieToken)) {
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

	public static String generateRandomPassword(int len) {
		// ASCII range – alphanumeric (0-9, a-z, A-Z)
		final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < len; i++) {
			int randomIndex = random.nextInt(chars.length());
			sb.append(chars.charAt(randomIndex));
		}

		return sb.toString();
	}

	public Boolean isAdmin(int userId, String cookieToken) {
		Optional<BankCustomer> user = customerRepo.findById(userId);
		if (user.get().getRole().equalsIgnoreCase("manager"))
			return true;
		return false;
	}

	public HashMap<String, String> addNewCustomer(BankCustomer customer, int userId, String cookieToken) {

		HashMap<String, String> response = new HashMap<String, String>();
		String status = "True";
		String message = "Account Created Successfully!";

		if (isExistingCustomer(customer.getPanCard(),userId,cookieToken).containsKey("Customer Id")) {
			status = "False";
			message = "Customer PAN Exits!";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "200");
			return response;
		}

		if (!checkSession(userId, cookieToken)) {

			status = "False";
			message = "Session Expired!";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "401");
			return response;
		}

		if (!isAdmin(userId, cookieToken)) {
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
		String TempPassword = generateRandomPassword(20);

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

		if (!checkSession(userId, cookieToken)) {
			status = "False";
			message = "Session Expired!";
			response.put("success", status);
			response.put("message", message);
			response.put("statusCode", "401");
			return response;
		}

		if (!isAdmin(userId, cookieToken)) {
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
	
	public boolean deposit(String accountNumber, int amount) {
		try {
			logger.info("deposit started");
			BankAccount customerAccount=accountRepo.findById(accountNumber).get();
			customerAccount.setCurrentBalance(customerAccount.getCurrentBalance()+amount);
			String transactionId = generateTransactionId();
			
			accountRepo.save(customerAccount);

			logger.info("balance updated");
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
		    Date date = new Date();
		    String transactionTimestamp=formatter.format(date);
		    
			
			logger.info("Updating transactions...............");
			Transaction newTransaction= new Transaction();
			newTransaction.setAmount(amount);
			newTransaction.setToAccountNumber(accountNumber);
			newTransaction.setFromAccountNumber("Cash");
			newTransaction.setTransactionReferenceNumber(transactionId);
			newTransaction.setTransactionDate(transactionTimestamp);
			
			transactionRepo.save(newTransaction);

			String subject="Money deposit";
			String body=amount+" is deposited to your bank account "+accountNumber+" with transaction id: "+transactionId;
			
			int customerid=customerAccount.getCustomerId();
			
			String mail = customerRepo.findById(customerid).get().getEmail();
			
			email.sendEmail(mail, subject, body);
			logger.info("Mail sent to user");
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}
	

	public boolean cashWithdrawal(String accountNumber, int amount) {
		try {
			logger.info("checking current balance");
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
		    Date date = new Date();
		    String transactionTimestamp=formatter.format(date);
			
			BankAccount customerAccount=accountRepo.findById(accountNumber).get();
			int currentBalance = customerAccount.getCurrentBalance();
			System.out.println("Check1");
			List<Transaction> customersTodaysTransactions = transactionRepo.findByTransaction(accountNumber,"cash",transactionTimestamp);
			
			int numberOfTodaysWithdrawal = customersTodaysTransactions.size();
			int totalWithdrawal = 0;
			
			for(int i=0;i<numberOfTodaysWithdrawal;i++) 
				totalWithdrawal+=customersTodaysTransactions.get(i).getAmount();
			System.out.println("Check2");
			
			if (currentBalance > amount && totalWithdrawal+amount <= 10000 ) {
				customerAccount.setCurrentBalance(customerAccount.getCurrentBalance()- amount);
				accountRepo.save(customerAccount);
				String transactionId = generateTransactionId();

				logger.info("Withdrawal successfull......");
				logger.info("Updating Transactions.......");
				
				Transaction newTransaction= new Transaction();
				newTransaction.setAmount(amount);
				newTransaction.setFromAccountNumber(accountNumber);
				newTransaction.setToAccountNumber("Cash");
				newTransaction.setTransactionReferenceNumber(transactionId);
				newTransaction.setTransactionDate(transactionTimestamp);
				
				transactionRepo.save(newTransaction);
				
				String subject="Cash Withdraw";
				String body=amount+" is withdraw from your bank account "+accountNumber+" with transaction id: "+transactionId;
				int customerid=customerAccount.getCustomerId();
				
				String mail = customerRepo.findById(customerid).get().getEmail();
				
				email.sendEmail(mail, subject, body);
				logger.info("Mail sent to user");
				return true;
			} else {
				logger.info("Insucficcient balance or you have reached today's limit");
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}
	public String transfer(String fromAccount, String toAccount, int amount) {
		try {
			String result="";
			logger.info("initiating transfer................");
			
			BankAccount customerAccountSender=accountRepo.findById(fromAccount).get();
			int  currentBalanceSender=customerAccountSender.getCurrentBalance();
			if(currentBalanceSender>=amount) {
				customerAccountSender.setCurrentBalance(currentBalanceSender-amount);
				accountRepo.save(customerAccountSender);
				String transactionId = generateTransactionId();
				
				
				BankAccount customerAccountReciever=accountRepo.findById(toAccount).get();
				int  currentBalanceReciever=customerAccountReciever.getCurrentBalance();
				customerAccountReciever.setCurrentBalance(currentBalanceReciever+amount);
				accountRepo.save(customerAccountReciever);
				
	
				logger.info("Amount debited from account "+ fromAccount);
				logger.info("Amount credited to account: "+toAccount );
				
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
			    Date date = new Date();
			    String transactionTimestamp=formatter.format(date);
			    
				
				logger.info("Updating transactions...............");
				Transaction newTransaction= new Transaction();
				newTransaction.setAmount(amount);
				newTransaction.setToAccountNumber(toAccount);
				newTransaction.setFromAccountNumber(fromAccount);
				newTransaction.setTransactionReferenceNumber(transactionId);
				newTransaction.setTransactionDate(transactionTimestamp);
				
				transactionRepo.save(newTransaction);
	
				String subjectDebit="Amount debited";
				String bodySender=amount+" is sent from your bank account "+fromAccount+" to bank account "+toAccount+" with transaction id: "+transactionId;
				
				
				
				int customeridSender=customerAccountSender.getCustomerId();
				String mailSender = customerRepo.findById(customeridSender).get().getEmail();
				
				int customeridReciever=customerAccountReciever.getCustomerId();
				String mailReciever = customerRepo.findById(customeridReciever).get().getEmail();
				
				String subjectCredit="Amount credited";
				String bodyReciever = amount+" is deposited to your bank account "+toAccount+" with transaction id: "+transactionId;
				
				email.sendEmail(mailSender, subjectDebit, bodySender);
				
				email.sendEmail(mailReciever, subjectCredit, bodyReciever);
	
				logger.info("Mail sent to users");
				
				
				result="Transfering funds is successfull. TransactionId "+ transactionId;
			}
			else {
				result="Transfering fund is not done due to insufficient funds";
			}
			return result;
		}catch (Exception e) {
			logger.error(e.getMessage());
			return "Transfering fund is not done due to some issues";
		}
	}
		
		
		
		
	public String generateTransactionId() {
        int min = 10000;
        int max = 99999;

        int partTransactionId = (int) (Math.random() * (max - min + 1) + min);
        int SecondPartTransactionId = (int) (Math.random() * (max - min + 1) + min);

        return String.valueOf(partTransactionId)+String.valueOf(SecondPartTransactionId);
    }


}
