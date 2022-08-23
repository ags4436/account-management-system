package com.barclays.capstone.main.model;

public class BankCustomer {

	String customerID;
	String panCard;
	String aadharNumber;
	String customerNname;
	String postalAddress;
	String email;
	String dob;
	String role;

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
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
