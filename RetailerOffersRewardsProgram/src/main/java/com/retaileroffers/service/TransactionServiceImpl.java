package com.retaileroffers.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retaileroffers.entity.Customer;
import com.retaileroffers.entity.Transaction;
import com.retaileroffers.exception.CutomerNotFoundException;
import com.retaileroffers.model.TransactionRequest;
import com.retaileroffers.model.TransactionsResponse;
import com.retaileroffers.repository.CustomerRepository;
import com.retaileroffers.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	public TransactionRepository transactionRepo;

	@Autowired
	public CustomerRepository customerRepo;

	@Override
	public Customer saveTransaction(TransactionRequest txData) {
		Customer customer = customerRepo.findById(txData.getCustomerId())
				.orElseThrow(() -> new CutomerNotFoundException("Customer not found"));

		Transaction tx = new Transaction();
		tx.setAmount(txData.getAmount());
		tx.setDate(LocalDate.now());
		tx.setEarnPoints(calEarnPoints(txData.getAmount()));
		tx.setCustomer(customer);
		transactionRepo.save(tx);

		// Update customer's reward points and total amount
		if (customer.getAmount() == null)
			customer.setPoints(tx.getEarnPoints());
		else
			customer.setPoints(customer.getPoints() + tx.getEarnPoints());

		customer.setAmount(customer.getAmount() + tx.getAmount());
		customer.setTraxDate(tx.getDate());

		return customerRepo.save(customer);
	}

	// Reward calculation logic based on amount thresholds
	private Long calEarnPoints(Long amount) {
		if (amount > 50 && amount <= 100) {
			return (amount - 50);
		} else if (amount > 100) {
			return (amount - 100) * 2 + 50;
		} else {
			return 0L;
		}
	}

	@Override
	public List<TransactionsResponse> getCustomerLast3MonthsPoints(Long customerId) {
		List<TransactionsResponse> allPoints = transactionRepo.getMonthlyPointsSummary();
		LocalDate now = LocalDate.now();
		List<String> last3Months = new ArrayList<>();

		// Generate last 3 months in "yyyy-MM" format
		for (int i = 0; i < 3; i++) {
			LocalDate date = now.minusMonths(i);
			last3Months.add(date.toString().substring(0, 7));
		}

		// Filter transactions for the given customer and last 3 months
		List<TransactionsResponse> filtered = allPoints.stream()
				.filter(tx -> tx.getCustomerId().equals(customerId) && last3Months.contains(tx.getMonth()))
				.collect(Collectors.toList());

		if (filtered.isEmpty()) {
			throw new CutomerNotFoundException("No data found for customer in the last 3 months");
		}
		return filtered;
	}

	// Collecting all customer details
	@Override
	public List<Customer> getAllCustomers() {
		return customerRepo.findAll();
	}
}
