package com.retaileroffers.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retaileroffers.dao.TransactionsDTO;
import com.retaileroffers.entity.Customer;
import com.retaileroffers.globalexception.CutomerNotFoundException;
import com.retaileroffers.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TransactionService txService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testGetMultipleCustomersTransactions_Success() throws Exception {
		// Customer 1 transactions
		TransactionsDTO tx1 = new TransactionsDTO(1L, "2025-06", 120L);
		TransactionsDTO tx2 = new TransactionsDTO(1L, "2025-05", 80L);
		List<TransactionsDTO> customer1Tx = List.of(tx1, tx2);

		// Customer 2 transactions
		TransactionsDTO tx3 = new TransactionsDTO(2L, "2025-06", 150L);
		TransactionsDTO tx4 = new TransactionsDTO(2L, "2025-04", 60L);
		List<TransactionsDTO> customer2Tx = List.of(tx3, tx4);

		// Mocking service for both customers
		Mockito.when(txService.getCustomerLast3MonthsPoints(1L)).thenReturn(customer1Tx);
		Mockito.when(txService.getCustomerLast3MonthsPoints(2L)).thenReturn(customer2Tx);

		// Perform and verify for customer 1
		mockMvc.perform(get("/transaction/points/last3months").param("customerId", "1")).andExpect(status().isOk());

		// Perform and verify for customer 2
		mockMvc.perform(get("/transaction/points/last3months").param("customerId", "2")).andExpect(status().isOk());
	}

// Test for successful transaction save
	@Test
	void testSaveTransaction_Success() throws Exception {
		TransactionsDTO txDto = new TransactionsDTO(75l, "2025-06", 25l);
		txDto.setCustomerId(1L);
		txDto.setMonth("2025-06");
		txDto.setAmount(25L);

		Customer customer = new Customer();
		customer.setAmount(txDto.getAmount());
		customer.setPoints(txDto.getTotalPoints());

		Mockito.when(txService.saveTransaction(any(TransactionsDTO.class))).thenReturn(customer);

		mockMvc.perform(put("/transaction/save").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(txDto))).andExpect(status().isOk());
	}

// Test for internal server error during transaction save
	@Test
	void testSaveTransaction_InternalServerError() throws Exception {
		TransactionsDTO txDto = new TransactionsDTO(null, null, null);
		txDto.setCustomerId(1L);
		txDto.setMonth("2025-06");
		txDto.setAmount(90L);

		Mockito.when(txService.saveTransaction(any(TransactionsDTO.class)))
				.thenThrow(new RuntimeException("DB error"));

		mockMvc.perform(put("/transaction/save").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(txDto))).andExpect(status().isInternalServerError())
				.andExpect(content().string("Transaction failed."));
	}

// Test for bad request due to invalid input
	@Test
	void testSaveTransaction_InvalidInput() throws Exception {
		TransactionsDTO txDto = new TransactionsDTO(1l, "2025-06", 60l);

		mockMvc.perform(put("/transaction/save").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(txDto))).andExpect(status().isBadRequest());
	}

// Test for successful retrieval of last 3 months' points
	@Test
	void testGetCustomerLast3MonthsPoints_Success() throws Exception {
		TransactionsDTO tx = new TransactionsDTO(1l, "2025-06", 70l);
		List<TransactionsDTO> txList = List.of(tx);

		Mockito.when(txService.getCustomerLast3MonthsPoints(1L)).thenReturn(txList);

		mockMvc.perform(get("/transaction/points/last3months").param("customerId", "1")).andExpect(status().isOk());
	}

// Test when customer is not found
	@Test
	void testGetCustomerLast3MonthsPoints_CustomerNotFound() throws Exception {
		Mockito.when(txService.getCustomerLast3MonthsPoints(1L))
				.thenThrow(new CutomerNotFoundException("Customer not found"));

		mockMvc.perform(get("/transaction/points/last3months").param("customerId", "1"))
				.andExpect(status().isNotFound()).andExpect(content().string("Customer or data not found."));
	}

// Test when customerId parameter is missing
	@Test
	void testGetCustomerLast3MonthsPoints_MissingParam() throws Exception {
		mockMvc.perform(get("/transaction/points/last3months")).andExpect(status().isBadRequest());
	}
}
