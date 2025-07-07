package com.retaileroffers.service;

import java.util.List;

import com.retaileroffers.entity.Customer;
import com.retaileroffers.model.TransactionRequest;
import com.retaileroffers.model.TransactionsResponse;

/**
 * Service interface for handling customer transactions and reward point calculations.
 */
public interface TransactionService {

    /**
     * Saves a transaction for the specified customer and updates their reward points.
     */
    public Customer saveTransaction(TransactionRequest txData);

    /**
     * Retrieves reward points earned by a customer in the last 3 months.
     */
    public List<TransactionsResponse> getCustomerLast3MonthsPoints(Long customerId);
    
    /**
     * Retrieves all registered customers.
     */
	public List<Customer> getAllCustomers();

	
}
