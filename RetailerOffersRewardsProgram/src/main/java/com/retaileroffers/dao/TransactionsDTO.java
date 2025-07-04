package com.retaileroffers.dao;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for carrying transaction and reward point data between layers.
 */
public class TransactionsDTO {

	@NotNull(message = "Amount is required")
	@Min(value = 1, message = "Amount must be greater than 0")
	private Long amount;
	@NotNull
	private Long customerId;
	@NotBlank
	private String month;
	private Long totalPoints;

	// Constructor used for reward summary responses
	public TransactionsDTO(Long customerId, String month, Long totalPoints) {
		this.customerId = customerId;
		this.month = month;
		this.totalPoints = totalPoints;
	}

	@Override
	public String toString() {
		return "TransactionsDTO [amount=" + amount + ", customerId=" + customerId + ", month=" + month
				+ ", totalPoints=" + totalPoints + "]";
	}

	// Getters and setters
	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Long getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Long totalPoints) {
		this.totalPoints = totalPoints;
	}
}
