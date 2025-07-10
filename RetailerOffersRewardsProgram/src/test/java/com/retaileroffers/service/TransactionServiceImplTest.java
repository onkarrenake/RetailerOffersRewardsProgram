package com.retaileroffers.service;

import com.retaileroffers.entity.Customer;
import com.retaileroffers.entity.Transaction;
import com.retaileroffers.exception.CutomerNotFoundException;
import com.retaileroffers.model.TransactionRequest;
import com.retaileroffers.model.TransactionsResponse;
import com.retaileroffers.repository.CustomerRepository;
import com.retaileroffers.repository.TransactionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepo;

    @Mock
    private CustomerRepository customerRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveTransaction_Success() {
        TransactionRequest request = new TransactionRequest(null, null);
        request.setCustomerId(1L);
        request.setAmount(120L);

        Customer customer = new Customer();
        customer.setAmount(100L);
        customer.setPoints(30L);

        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(transactionRepo.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));
        when(customerRepo.save(any(Customer.class))).thenAnswer(i -> i.getArgument(0));

        Customer updatedCustomer = transactionService.saveTransaction(request);

        assertEquals(100L + 120L, updatedCustomer.getAmount());
        assertEquals(30L + 90L, updatedCustomer.getPoints()); // 90 points for 120 amount
        verify(transactionRepo, times(1)).save(any(Transaction.class));
        verify(customerRepo, times(1)).save(any(Customer.class));
    }

    @Test
    void testSaveTransaction_CustomerNotFound() {
        TransactionRequest request = new TransactionRequest(null, null);
        request.setCustomerId(99L);
        request.setAmount(80L);

        when(customerRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CutomerNotFoundException.class, () -> transactionService.saveTransaction(request));
    }

    @Test
    void testGetCustomerLast3MonthsPoints_Success() {
        Long customerId = 1L;
        LocalDate now = LocalDate.now();
        String currentMonth = now.toString().substring(0, 7);

        TransactionsResponse tx = new TransactionsResponse(customerId, currentMonth, customerId);
        tx.setCustomerId(customerId);
        tx.setMonth(currentMonth);
        tx.setTotalPoints(100L);

        when(transactionRepo.getMonthlyPointsSummary()).thenReturn(List.of(tx));

        List<TransactionsResponse> result = transactionService.getCustomerLast3MonthsPoints(customerId);

        assertEquals(1, result.size());
        assertEquals(customerId, result.get(0).getCustomerId());
    }

    @Test
    void testGetCustomerLast3MonthsPoints_NoData() {
        when(transactionRepo.getMonthlyPointsSummary()).thenReturn(Collections.emptyList());

        assertThrows(CutomerNotFoundException.class,
                () -> transactionService.getCustomerLast3MonthsPoints(1L));
    }

    @Test
    void testGetAllCustomers() {
        List<Customer> customers = List.of(new Customer(), new Customer());
        when(customerRepo.findAll()).thenReturn(customers);

        List<Customer> result = transactionService.getLast3MonthsAllCustomers();

        assertEquals(2, result.size());
        verify(customerRepo, times(1)).findAll();
    }
}
