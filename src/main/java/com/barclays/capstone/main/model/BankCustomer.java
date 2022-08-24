package com.barclays.capstone.main.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bank_customer")
public class BankCustomer {

	@Id
	int customerID;
	String aadharNumber;
	String customerNname;
	String dob;
	String email;
	String panCard;
	String postalAddress;
	String role;
	



	public BankCustomer(int customerID, String aadharNumber,String customerNname,
			String dob, String email, String panCard,   String postalAddress,String role) {
		super();
		this.customerID = customerID;
		this.aadharNumber = aadharNumber;
		this.customerNname = customerNname;
		this.dob = dob;
		this.email = email;
		this.panCard = panCard;
		this.postalAddress = postalAddress;
		this.role = role;
	
		
		
		
		
		
		
		
		
		
//		this.customerID = customerID;
//		this.panCard = panCard;
//		this.aadharNumber = aadharNumber;
//		this.customerNname = customerNname;
//		this.postalAddress = postalAddress;
//		this.email = email;
//		this.dob = dob;
//		this.role = role;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public String getPanCard() {
		return panCard;
	}

	public void setPanCard(String panCard) {
		this.panCard = panCard;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getCustomerName() {
		return customerNname;
	}

	public void setCustomerName(String name) {
		this.customerNname = name;
	}

	public String getPostalAddress() {
		return postalAddress;
	}

	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "BankCustomer [customerID=" + customerID + ", panCard=" + panCard + ", aadharNumber=" + aadharNumber
				+ ", Name=" + customerNname + ", postalAddress=" + postalAddress + ", email=" + email + ", dob=" + dob
				+ ", role=" + role + "]";
	}
}
