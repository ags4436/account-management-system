package com.barclays.capstone.main.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.barclays.capstone.main.model.Transaction;

/**
 * 
 * @author Akash Salave, Shubham Chokhani
 * @Description Repository for Transaction Class
 * 
 */

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

	@Query(value = "SELECT * FROM bank_transaction WHERE from_Account_Number=?1 AND to_Account_Number=?2 AND transaction_Date=?3 ", nativeQuery = true)
	public abstract List<Transaction> findByTransaction(String fromAccountNumber, String fromTransactionDate,
			Date toTransactionDate);

	@Query(value = "SELECT * FROM bank_transaction WHERE from_Account_Number=?1 or to_account_number=?1 AND transaction_date BETWEEN ?2 AND ?3 ", nativeQuery = true)
	public abstract List<Transaction> findTransactionByDate(String fromAccountNumber, Date fromTransactionDate,
			Date toTransactionDate);

	@Query(value = "SELECT * FROM bank_transaction WHERE from_Account_Number=?1 or to_account_number=?1 ORDER BY transaction_date DESC LIMIT 5  ", nativeQuery = true)
	public abstract List<Transaction> miniStatement(String fromAccountNumber);

}