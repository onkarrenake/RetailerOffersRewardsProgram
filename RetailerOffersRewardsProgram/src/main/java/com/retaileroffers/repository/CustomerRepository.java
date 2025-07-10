package com.retaileroffers.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.retaileroffers.entity.Customer;

/**
 * Repository interface for Customer entity. Provides basic CRUD operations via
 * JpaRepository.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
    /**
     * This query joins the Customer entity with its associated transactions (tranx)
     * and filters based on the transaction date.
     */
	@Query("SELECT DISTINCT c FROM Customer c JOIN c.tranx t WHERE t.date >= :startDate")
	List<Customer> getLast3MonthsPointsSummary(@Param("startDate") LocalDate startDate);
	
}