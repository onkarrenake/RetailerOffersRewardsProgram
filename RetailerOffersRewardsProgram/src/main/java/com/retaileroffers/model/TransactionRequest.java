package com.retaileroffers.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class TransactionRequest {
	@NotNull(message = "Amount is required")
	@Min(value = 1, message = "Amount must be greater than 0")
	private Long amount;
	@NotNull
	private Long customerId;

	public TransactionRequest(
			@NotNull(message = "Amount is required") @Min(value = 1, message = "Amount must be greater than 0") Long amount,
			@NotNull Long customerId) {
		super();
		this.amount = amount;
		this.customerId = customerId;
	}

	@Override
	public String toString() {
		return "TransactionRequest [amount=" + amount + ", customerId=" + customerId + "]";
	}

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

}
