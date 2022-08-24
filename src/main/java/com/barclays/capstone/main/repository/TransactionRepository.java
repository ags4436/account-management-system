package com.barclays.capstone.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.barclays.capstone.main.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
	
	@Query(value="SELECT * FROM bank_transaction WHERE from_Account_Number=?1 AND to_Account_Number=?2 AND transaction_Date=?3 ", nativeQuery = true)
	public abstract List<Transaction> findByTransaction(String fromAccountNumber,String toAccountNumber, String transactionDate);
}