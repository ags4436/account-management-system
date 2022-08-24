package com.barclays.capstone.main.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bank_transaction")
public class Transaction {
	
	String fromAccountNumber;
    String toAccountNumber;
    float amount;
    @Id
    String transactionReferenceNumber;                                
    String transactionDate;

    public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
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
	

}