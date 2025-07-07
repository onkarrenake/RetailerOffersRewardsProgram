package com.retaileroffers.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retaileroffers.entity.Customer;
import com.retaileroffers.exception.CutomerNotFoundException;
import com.retaileroffers.model.TransactionRequest;
import com.retaileroffers.model.TransactionsResponse;
import com.retaileroffers.service.TransactionService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TransactionService txService;

	@Autowired
	private ObjectMapper objectMapper;

	// Test for successful transaction save
	@Test
	void testSaveTransaction_Success() throws Exception {
		TransactionRequest request = new TransactionRequest(null, null);
		request.setCustomerId(1L);
		request.setAmount(120L);

		Customer customer = new Customer();
		customer.setCustomerId(1L);

		when(txService.saveTransaction(any(TransactionRequest.class))).thenReturn(customer);

		mockMvc.perform(put("/transaction/save").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(jsonPath("$.customerId").value(1L));
	}

	// Test for failed transaction save
	@Test
	void testSaveTransaction_Failure() throws Exception {
		TransactionRequest request = new TransactionRequest(null, null);
		request.setCustomerId(1L);
		request.setAmount(120L);

		when(txService.saveTransaction(any(TransactionRequest.class))).thenThrow(new RuntimeException("DB error"));

		mockMvc.perform(put("/transaction/save").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isInternalServerError())
				.andExpect(content().string("Transaction failed."));
	}

	// Test for successful retrieval of all customers
	@Test
	void testAllCustomerDetails_Success() throws Exception {
		when(txService.getAllCustomers()).thenReturn(List.of(new Customer(), new Customer()));

		mockMvc.perform(get("/transaction/customer/allDetails")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2));
	}

	// Test for failure in retrieving all customers
	@Test
	void testAllCustomerDetails_Failure() throws Exception {
		when(txService.getAllCustomers()).thenThrow(new RuntimeException("DB error"));

		mockMvc.perform(get("/transaction/customer/allDetails")).andExpect(status().isInternalServerError())
				.andExpect(content().string("Not able to find customer list."));
	}

	// Test for successful retrieval of last 3 months' points
	@Test
	void testGetCustomerLast3MonthsPoints_Success() throws Exception {
		TransactionsResponse response = new TransactionsResponse(null, null, null);
		response.setCustomerId(1L);
		response.setMonth("2025-06");
		response.setTotalPoints(100L);

		when(txService.getCustomerLast3MonthsPoints(1L)).thenReturn(List.of(response));

		mockMvc.perform(get("/transaction/points/last3months").param("customerId", "1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1));
	}

	// Test for customer not found in last 3 months' points
	@Test
	void testGetCustomerLast3MonthsPoints_NotFound() throws Exception {
		when(txService.getCustomerLast3MonthsPoints(1L)).thenThrow(new CutomerNotFoundException("No data"));

		mockMvc.perform(get("/transaction/points/last3months").param("customerId", "1"))
				.andExpect(status().isNotFound()).andExpect(content().string("Customer or data not found."));
	}
}