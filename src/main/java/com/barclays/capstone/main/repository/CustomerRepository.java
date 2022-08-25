package com.barclays.capstone.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.barclays.capstone.main.model.BankCustomer;

/**
 * 
 * @author Aakash Gouri Shankar
 * @Description CustomerRepository interface
 * 
 */

@Repository
public interface CustomerRepository extends JpaRepository<BankCustomer, Integer> {

	public abstract BankCustomer findBypanCard(String panCard);
}
