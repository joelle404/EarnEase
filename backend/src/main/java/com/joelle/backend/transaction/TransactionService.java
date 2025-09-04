package com.joelle.backend.transaction;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Total of all transactions
    public Double getTotalAmount() {
        return transactionRepository.sumAllTransactions();
    }
public Double getSumLastWeek(Long staffId) {
    LocalDate now = LocalDate.now();
    LocalDate weekAgo = now.minusWeeks(1);
    return filterAndSum(staffId, weekAgo, now);
}

public Double getSumLastMonth(Long staffId) {
    LocalDate now = LocalDate.now();
    LocalDate monthAgo = now.minusMonths(1);
    return filterAndSum(staffId, monthAgo, now);
}

public Double getSumLastYear(Long staffId) {
    LocalDate now = LocalDate.now();
    LocalDate yearAgo = now.minusYears(1);
    return filterAndSum(staffId, yearAgo, now);
}

// Updated helper
private Double filterAndSum(Long staffId, LocalDate from, LocalDate to) {
    List<Transaction> transactions = transactionRepository.findByStaffId(staffId);

    return transactions.stream()
            .filter(t -> {
                LocalDate d = t.getDateAsLocalDate();
                return !d.isBefore(from) && !d.isAfter(to);
            })
            .mapToDouble(Transaction::getAmountPaid)
            .sum();
}

}
