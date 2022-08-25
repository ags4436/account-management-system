package com.barclays.capstone.main.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Aakash Gouri Shankar, Divya Raisinghani, Harsh Das,
 * @Description POJO for Credentails
 * 
 */

@Entity
@Table(name = "bank_credentials")
public class Credentials {

	@Id
	int customerId;
	String password;
	String cookieToken;
	Timestamp cookieExpiry;
	int isNewUser;

	public int getIsNewUser() {
		return isNewUser;
	}

	public void setIsNewUser(int isNewUser) {
		this.isNewUser = isNewUser;
	}

	public String getCookieToken() {
		return cookieToken;
	}

	public Timestamp getCookieExpiry() {
		return cookieExpiry;
	}

	public void setCookieExpiry(Timestamp cookieExpiry) {
		this.cookieExpiry = cookieExpiry;
	}

	public void setCookieToken(String cookieToken) {
		this.cookieToken = cookieToken;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
