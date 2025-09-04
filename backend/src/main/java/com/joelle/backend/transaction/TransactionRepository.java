package com.joelle.backend.transaction;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT COALESCE(SUM(t.amountPaid), 0) FROM Transaction t")
    Double sumAllTransactions();

    List<Transaction> findByStaffId(Long id, Sort by);

    List<Transaction> findByStaffId(Long staffId);
}
