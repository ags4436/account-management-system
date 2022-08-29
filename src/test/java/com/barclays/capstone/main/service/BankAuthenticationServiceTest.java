
package com.barclays.capstone.main.service;

import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import com.barclays.capstone.main.model.Credentials;

public class BankAuthenticationServiceTest {
	
	@InjectMocks
	@Autowired 
	BankAuthenticationService authService;
	
	
	static Credentials cred;
	
	@BeforeAll
	static void initialize() {
		cred = new Credentials();
		cred.setCustomerId(107840);
		cred.setPassword("root");
	}
	
	@Test
	@Order(1)
	void authTest()  {
		System.out.println(cred);
		HashMap<String, String> actual =  authService.login(cred);
		System.out.println(actual);
		Assertions.assertEquals("True", actual.get("success"));
		
	}

}
