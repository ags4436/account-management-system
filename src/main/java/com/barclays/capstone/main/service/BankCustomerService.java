package com.barclays.capstone.main.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.barclays.capstone.main.model.BankCustomer;

public interface BankCustomerService {
	  
	  
	   Optional<BankCustomer> findById(int customerId);
	  
	   public HashMap<String,String> deleteBankCustomer(int customerId);
	   public HashMap<String,String> updateBankCustomer(int customerId, BankCustomer bankCustomer);
}
