package com.barclays.capstone.main.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Akash Salave, Shubham Chokhani, Aakash Gouri Shankar
 * @Description POJO for Transactions
 * 
 */

@Entity
@Table(name = "bank_transaction")
public class Transaction {

	String fromAccountNumber;
	String toAccountNumber;
	float amount;
	@Id
	String transactionReferenceNumber;
	Date transactionDate;

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public String getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getTransactionReferenceNumber() {
		return transactionReferenceNumber;
	}

	public void setTransactionReferenceNumber(String transactionReferenceNumber) {
		this.transactionReferenceNumber = transactionReferenceNumber;
	}

	@Override
	public String toString() {
		return transactionReferenceNumber + "," + amount + "," + fromAccountNumber + "," + toAccountNumber + ","
				+ transactionDate;
	}

}