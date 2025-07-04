package com.retaileroffers.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Entity representing a customer in the system.
 */
@Entity
@Table(name = "customer", uniqueConstraints = { @UniqueConstraint(columnNames = "email_id") })
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long customerId;

	@NotBlank
	private String name;

	@Email
	@NotBlank
	@Column(name = "email_id", nullable = false, unique = true)
	private String emailId;

	private Long amount = 0L;
	private Long points = 0L;

	private LocalDate date = LocalDate.now();
	private LocalDate traxDate;

	// One-to-many relationship with transactions
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Transaction> tranx = new ArrayList<>();

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", name=" + name + ", emailId=" + emailId + ", amount=" + amount
				+ ", points=" + points + ", date=" + date + ", traxDate=" + traxDate + "]";
	}

	// Getters and setters
	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDate getTraxDate() {
		return traxDate;
	}

	public void setTraxDate(LocalDate traxDate) {
		this.traxDate = traxDate;
	}

	public List<Transaction> getTranx() {
		return tranx;
	}

	public void setTranx(List<Transaction> tranx) {
		this.tranx = tranx;
	}
}
