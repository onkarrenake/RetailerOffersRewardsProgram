package com.retaileroffers.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.retaileroffers.entity.Customer;

/**
 * Repository interface for Customer entity. Provides basic CRUD operations via
 * JpaRepository.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	// Define custom query methods here if needed
}
