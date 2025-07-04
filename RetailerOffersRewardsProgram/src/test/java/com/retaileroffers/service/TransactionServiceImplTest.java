package com.retaileroffers.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.retaileroffers.dao.TransactionsDTO;
import com.retaileroffers.entity.Customer;
import com.retaileroffers.entity.Transaction;
import com.retaileroffers.globalexception.CutomerNotFoundException;
import com.retaileroffers.repo.CustomerRepository;
import com.retaileroffers.repo.TransactionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class TransactionServiceImplTest {

	// Injects the mocked dependencies into the service implementation
	@InjectMocks
	private TransactionServiceImpl transactionService;

	// Mocks the CustomerRepository dependency
	@Mock
	private CustomerRepository customerRepo;

	// Mocks the TransactionRepository dependency
	@Mock
	private TransactionRepository transactionRepo;

	private Customer customer;

	// Initializes mocks and a sample customer before each test
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this); // Initializes @Mock and @InjectMocks
		customer = new Customer();
		customer.setCustomerId(1L);
		customer.setPoints(0L);
		customer.setAmount(0L);
	}

	// Test saving a transaction for a new customer (no previous amount or points)
	@Test
	void testSaveTransaction_NewCustomer_Success() {
		Long customerId = 1L;

		// Create a valid TransactionsDTO with amount = 120 (should earn 90 points)
		TransactionsDTO txDto = new TransactionsDTO(null, null, null);
		txDto.setCustomerId(1L);
		txDto.setMonth("2025-06");
		txDto.setAmount(120L); // Valid amount

		// Create a new customer with no previous transactions
		Customer customer = new Customer();
		customer.setAmount(0L);
		customer.setPoints(0L);

		// Mock repository behavior
		when(customerRepo.findById(customerId)).thenReturn(Optional.of(customer));
		when(transactionRepo.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));
		when(customerRepo.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

		// Execute service method
		Customer updated = transactionService.saveTransaction(txDto);

		// Verify updated values
		assertEquals(90L, updated.getPoints()); // (120 - 100) * 2 + 50 = 90
		assertEquals(120L, updated.getAmount());
		assertEquals(LocalDate.now(), updated.getTraxDate());
	}

	// Test saving a transaction for an existing customer with previous amount and
	// points
	@Test
	void testSaveTransaction_ExistingCustomer_Success() {
		Long customerId = 1L;

		// Create a valid TransactionsDTO with amount = 80 (should earn 30 points)
		TransactionsDTO txDto = new TransactionsDTO(1L, "2025-06", 20L); // Constructor values ignored
		txDto.setCustomerId(1L);
		txDto.setMonth("2025-06");
		txDto.setAmount(80L); // (80 - 50) = 30 points

		// Set up existing customer with previous amount and points
		customer.setAmount(100L);
		customer.setPoints(20L);

		// Mock repository behavior
		when(customerRepo.findById(customerId)).thenReturn(Optional.of(customer));
		when(transactionRepo.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));
		when(customerRepo.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

		// Call the service method
		Customer updated = transactionService.saveTransaction(txDto);

		// Validate updated customer data
		assertEquals(50L, updated.getPoints()); // 20 existing + 30 earned = 50
		assertEquals(180L, updated.getAmount()); // 100 existing + 80 new = 180
	}

	// Test behavior when customer is not found in the repository
	@Test
	void testSaveTransaction_CustomerNotFound() {
		Long customerId = 1L;
		TransactionsDTO txDto = new TransactionsDTO(1L, "2025-06", 100L);

		// Simulate customer not found
		when(customerRepo.findById(customerId)).thenReturn(Optional.empty());

		// Expect exception to be thrown
		assertThrows(CutomerNotFoundException.class, () -> {
			transactionService.saveTransaction(txDto);
		});
	}

	// Test retrieving last 3 months' points for a customer with valid data
	@Test
	void testGetCustomerLast3MonthsPoints_Success() {
		Long customerId = 1L;
		String thisMonth = LocalDate.now().toString().substring(0, 7);

		// Create one matching and one non-matching transaction
		TransactionsDTO tx1 = new TransactionsDTO(1L, thisMonth, 120L);
		tx1.setCustomerId(customerId);

		TransactionsDTO tx2 = new TransactionsDTO(1L, thisMonth, 150L);
		tx2.setCustomerId(2L); // Different customer

		// Mock repository to return both transactions
		when(transactionRepo.getMonthlyPointsSummary()).thenReturn(List.of(tx1, tx2));

		// Call the service method
		List<TransactionsDTO> result = transactionService.getCustomerLast3MonthsPoints(customerId);

		// Validate only matching transaction is returned
		assertEquals(1, result.size());
		assertEquals(customerId, result.get(0).getCustomerId());
	}

	// Test when no matching transactions are found for the customer
	@Test
	void testGetCustomerLast3MonthsPoints_NoDataFound() {
		Long customerId = 1L;

		// Create a transaction for a different customer
		TransactionsDTO tx = new TransactionsDTO(1L, LocalDate.now().toString().substring(0, 7), 110L);
		tx.setCustomerId(2L); // Different customer

		// Mock repository to return only non-matching transaction
		when(transactionRepo.getMonthlyPointsSummary()).thenReturn(List.of(tx));

		// Expect exception due to no matching data
		assertThrows(CutomerNotFoundException.class, () -> {
			transactionService.getCustomerLast3MonthsPoints(customerId);
		});
	}

	// Test when the repository returns an empty list
	@Test
	void testGetCustomerLast3MonthsPoints_EmptyRepoList() {
		// Mock repository to return empty list
		when(transactionRepo.getMonthlyPointsSummary()).thenReturn(Collections.emptyList());

		// Expect exception due to no data
		assertThrows(CutomerNotFoundException.class, () -> {
			transactionService.getCustomerLast3MonthsPoints(1L);
		});
	}
}
