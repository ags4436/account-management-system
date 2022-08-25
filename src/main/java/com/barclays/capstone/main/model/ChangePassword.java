package com.barclays.capstone.main.model;

/**
 * 
 * @author Divya Raisinghani, Harsh Das, Aakash Gouri Shankar
 * @Description POJO for Change Password 
 * 
 */

public class ChangePassword {
	
	int customerId;
    String currentPassword;
    String newPassword;
    public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int userId) {
		this.customerId = userId;
	}
	public String getCurrentpassword() {
		return currentPassword;
	}
	public void setCurrentpassword(String currentpassword) {
		this.currentPassword = currentpassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
