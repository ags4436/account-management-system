package com.barclays.capstone.main.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.barclays.capstone.main.model.ChangePassword;
import com.barclays.capstone.main.model.Login;
import com.barclays.capstone.main.repository.BankRepository;

public class BankServices {
	
	@Autowired
	BankRepository repo;
	
	public BankCustomer login(Login user) {
		return repo.login(user);
	}
	
	public String changePassword(ChangePassword password) {
		if(repo.passwordChange(password)) {
			return "Password changed successfully";
		}else {
			return "Password is not changed due to some error please try again";
		}
	}
	
	

}
