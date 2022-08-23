package com.barclays.capstone.main.service;

import com.barclays.capstone.main.model.ChangePassword;
import com.barclays.capstone.main.model.Login;

public class BankServices {
	
	public BankCustomers login(Login user) {
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
