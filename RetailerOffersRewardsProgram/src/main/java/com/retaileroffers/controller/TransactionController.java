package com.retaileroffers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retaileroffers.entity.Customer;
import com.retaileroffers.exception.CutomerNotFoundException;
import com.retaileroffers.model.TransactionRequest;
import com.retaileroffers.model.TransactionsResponse;
import com.retaileroffers.service.TransactionService;
import jakarta.validation.Valid;

/**
 * Controller for handling transaction-related operations such as saving transactions
 * and retrieving reward points for customers.
 */
@RestController
@RequestMapping("/transaction")
@Validated
public class TransactionController {

    @Autowired
    private TransactionService txService;

    /**
     * Saves a transaction for a given customer ID.
     */
    @PutMapping("/save")
    public ResponseEntity<?> saveTransaction(@Valid @RequestBody TransactionRequest txData) {
        try {
            Customer save = txService.saveTransaction(txData);
            return ResponseEntity.ok(save);
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Transaction failed.");
        }
    }
    
    /**
     * Get all customer details
     */
    @GetMapping("/allCustomerLast3monthsPoints")
    public ResponseEntity<?> allCustomerDetails() {
        try {
            List<Customer> last3MonthsAllDetails = txService.getLast3MonthsAllCustomers();
            return ResponseEntity.ok(last3MonthsAllDetails);
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Not able to find customer list.");
        }
    }

    /**
     * Retrieves reward points earned by a customer in the last 3 months.
     */
    @GetMapping("/customerLast3monthsPoints")
    public ResponseEntity<?> getCustomerLast3MonthsPoints(@RequestParam Long customerId) {
        try {
            List<TransactionsResponse> results = txService.getCustomerLast3MonthsPoints(customerId);
            return ResponseEntity.ok(results);
        } catch (CutomerNotFoundException e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer or data not found.");
        }
    }
}
