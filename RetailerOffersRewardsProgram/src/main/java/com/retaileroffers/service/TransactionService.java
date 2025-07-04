package com.retaileroffers.service;

import java.util.List;
import com.retaileroffers.dao.TransactionsDTO;
import com.retaileroffers.entity.Customer;

/**
 * Service interface for handling customer transactions and reward point calculations.
 */
public interface TransactionService {

    /**
     * Saves a transaction for the specified customer and updates their reward points.
     */
    public Customer saveTransaction(TransactionsDTO txData);

    /**
     * Retrieves reward points earned by a customer in the last 3 months.
     */
    public List<TransactionsDTO> getCustomerLast3MonthsPoints(Long customerId);
}
