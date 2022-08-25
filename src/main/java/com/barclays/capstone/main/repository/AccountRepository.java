package com.barclays.capstone.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.barclays.capstone.main.model.BankAccount;

/**
 * 
 * @author Aakash Gouri Shankar
 * @Description AccountRepository interface
 * 
 */
@Repository
public interface AccountRepository extends JpaRepository<BankAccount, String> {

	@Query(value = "SELECT account_number from bank_Account ORDER BY account_number DESC LIMIT 1 ", nativeQuery = true)
	public abstract  String getAccountNumber();
}
