package com.retaileroffers.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.retaileroffers.entity.Transaction;
import com.retaileroffers.model.TransactionsResponse;

/**
 * Repository interface for Transaction entity.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	/**
	 * Returns monthly reward points summary for each customer. Groups transactions
	 * by customer and month, and calculates total earned points.
	 *
	 * @return List of TransactionsDTO with customerId, month, and totalPoints
	 */
	@Query("SELECT new com.retaileroffers.model.TransactionsResponse(" + "t.customer.customerId, "
			+ "CONCAT(YEAR(t.date), '-', LPAD(CAST(MONTH(t.date) AS string), 2, '0')), " + "SUM(t.earnPoints)) "
			+ "FROM Transaction t " + "GROUP BY t.customer.customerId, YEAR(t.date), MONTH(t.date) "
			+ "ORDER BY t.customer.customerId, YEAR(t.date), MONTH(t.date)")
	List<TransactionsResponse> getMonthlyPointsSummary();
}
