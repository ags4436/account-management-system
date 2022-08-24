package com.barclays.capstone.main.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.barclays.capstone.main.model.Credentials;

@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Integer> {
	
	public abstract Credentials findBycustomerIdAndPassword(int customerId,String password);
	public abstract Credentials findByCustomerIdAndCookieToken(int customerId,String cookieToken);
	
}
