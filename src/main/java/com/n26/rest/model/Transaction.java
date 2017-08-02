package com.n26.rest.model;

/**
 * Transaction model/bean, which is the input to transactions API 
 *
 * @author Harish Kumar Govindaswamy Saravanam
 * @version 1.0
 * @since 2017-08-01
 */
public class Transaction {
	
	double amount;
	long timestamp;
	
	public Transaction() {
		super();
	}
	
	public Transaction(double amount, long timestamp) {
		super();
		this.amount = amount;
		this.timestamp = timestamp;
	}
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
