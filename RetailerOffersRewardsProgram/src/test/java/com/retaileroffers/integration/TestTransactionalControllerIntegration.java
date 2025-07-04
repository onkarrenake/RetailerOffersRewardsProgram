package com.retaileroffers.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retaileroffers.dao.TransactionsDTO;
import com.retaileroffers.entity.Customer;
import com.retaileroffers.repo.CustomerRepository;
import com.retaileroffers.repo.TransactionRepository;

@SpringBootTest
@AutoConfigureMockMvc
class TestTransactionalControllerIntegration {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private TransactionRepository transactionRepo;

	@Autowired
	private ObjectMapper objectMapper;

	private Customer customer1;
	private Customer customer2;

	@BeforeEach
	void setup() {
		transactionRepo.deleteAll();
		customerRepo.deleteAll();

		customer1 = new Customer();
		customer1.setName("Alice");
		customer1.setEmailId("alice@example.com");
		customer1 = customerRepo.save(customer1);

		customer2 = new Customer();
		customer2.setName("Bob");
		customer2.setEmailId("bob@example.com");
		customer2 = customerRepo.save(customer2);
	}

	@Test
	void testSaveTransactionsAndFetchPoints() throws Exception {
		// Save transactions for customer1
		saveTransaction(customer1.getCustomerId(), 120L); // 90 points
		saveTransaction(customer1.getCustomerId(), 80L); // 30 points

		// Save transactions for customer2
		saveTransaction(customer2.getCustomerId(), 60L); // 10 points
		saveTransaction(customer2.getCustomerId(), 200L); // 250 points

		// Fetch last 3 months points for customer1
		mockMvc.perform(
				get("/transaction/points/last3months").param("customerId", String.valueOf(customer1.getCustomerId())))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].customerId").value(customer1.getCustomerId()))
				.andExpect(jsonPath("$[0].totalPoints").exists());

		// Fetch last 3 months points for customer2
		mockMvc.perform(
				get("/transaction/points/last3months").param("customerId", String.valueOf(customer2.getCustomerId())))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].customerId").value(customer2.getCustomerId()))
				.andExpect(jsonPath("$[0].totalPoints").exists());
	}

	/**
	 * Helper method to simulate saving a transaction for a given customer. It
	 * performs an HTTP PUT request to the /transaction/save/{id} endpoint.
	 */
	private void saveTransaction(Long customerId, Long amount) throws Exception {
		// Create a TransactionsDTO object with the given customerId and amount
		TransactionsDTO dto = new TransactionsDTO(customerId, "2025-07", 120l);
		dto.setAmount(amount); // Set the transaction amount
		dto.setCustomerId(customerId); // Set the customer ID
		dto.setMonth("2025-07"); // Set the transaction month

		// Perform a PUT request to save the transaction
		mockMvc.perform(put("/transaction/save").contentType(MediaType.APPLICATION_JSON) // Set content
																										// type to JSON
				.content(objectMapper.writeValueAsString(dto))) // Convert DTO to JSON
				.andExpect(status().isOk()); // Expect HTTP 200 OK response
	}

}
