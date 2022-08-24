package com.barclays.capstone.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.barclays.capstone.main.model.BankAccount;

@Repository
public interface AccountRepository extends JpaRepository<BankAccount, Integer> {

}
