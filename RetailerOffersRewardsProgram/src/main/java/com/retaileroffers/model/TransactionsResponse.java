package com.retaileroffers.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for carrying transaction and reward point data between layers.
 */
public class TransactionsResponse {

	@NotNull
	private Long customerId;
	@NotBlank
	private String month;
	private Long totalPoints;

	// Constructor used for reward summary responses
	public TransactionsResponse(Long customerId, String month, Long totalPoints) {
		this.customerId = customerId;
		this.month = month;
		this.totalPoints = totalPoints;
	}

	@Override
	public String toString() {
		return "TransactionsResponse [customerId=" + customerId + ", month=" + month + ", totalPoints=" + totalPoints
				+ "]";
	}

	// Getters and setters
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
