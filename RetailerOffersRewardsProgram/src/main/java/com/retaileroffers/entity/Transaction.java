package com.retaileroffers.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * Entity representing a transaction made by a customer.
 */
@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long txId;

	@NotNull
	private Long amount;

	private LocalDate date;
	private Long earnPoints;

	// Many-to-one relationship with Customer
	@ManyToOne
	@JoinColumn(name = "customer_id")
	@JsonBackReference
	private Customer customer;

	@Override
	public String toString() {
		return "Transaction [txId=" + txId + ", amount=" + amount + ", date=" + date + ", earnPoints=" + earnPoints
				+ ", customer=" + customer + "]";
	}

	// Getters and setters
	public Long getTxId() {
		return txId;
	}

	public void setTxId(Long txId) {
		this.txId = txId;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Long getEarnPoints() {
		return earnPoints;
	}

	public void setEarnPoints(Long earnPoints) {
		this.earnPoints = earnPoints;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
