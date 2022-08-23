package com.barclays.capstone.main.service;

import java.lang.Math;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barclays.capstone.main.repository.BankRepository;



@Service
public class Operations {


	@Autowired
	BankRepository repo;
	
	public boolean deposit(String accountNumber, int amount) {
		if (repo.deposit(accountNumber, amount, generateTransactionId())) {
			return true;
		} else {
			return false;
		}
	}

	public boolean cashWithdrawal(String accountNumber, int amount) {
		if (repo.cashWithdrawal(accountNumber, amount, generateTransactionId())) {
			return true;
		} else {
			return false;
		}
	}
	public String transfer(String fromAccount, String toAccount, int amount) {
		String transactionId=generateTransactionId();
		String result="";
		if(repo.transfer(fromAccount, toAccount, amount,transactionId )) {
			result="Transfering funds is successfull. TransactionId "+ transactionId;
		}else {
			result="Transfering fund is not done due to some error.Please try again";
		}
		return result;
		
	}
	public String generateTransactionId() {
        int min = 10000;
        int max = 99999;

        int partTransactionId = (int) (Math.random() * (max - min + 1) + min);
        int SecondPartTransactionId = (int) (Math.random() * (max - min + 1) + min);

        return String.valueOf(partTransactionId)+String.valueOf(SecondPartTransactionId);
    }
}