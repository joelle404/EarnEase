package com.joelle.backend.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT COALESCE(SUM(t.amountPaid), 0) FROM Transaction t")
    Double sumAllTransactions();
}
