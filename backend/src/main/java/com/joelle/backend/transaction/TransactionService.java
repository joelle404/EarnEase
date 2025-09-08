package com.joelle.backend.transaction;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joelle.backend.katiawork.KatiaWorkService;
import com.joelle.backend.purchase.ProductPurchaseService;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductPurchaseService purchaseService;
    private final KatiaWorkService katiaWorkService;


    @Autowired
    public TransactionService(TransactionRepository transactionRepository, ProductPurchaseService purchaseService, KatiaWorkService katiaWorkService) {
        this.transactionRepository = transactionRepository;
        this.purchaseService = purchaseService;
        this.katiaWorkService = katiaWorkService;
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
// In TransactionService.java
private Double getGivenPercentsInRange(Long staffId, LocalDate from, LocalDate to) {
    List<Transaction> transactions = transactionRepository.findByStaffId(staffId);

    return transactions.stream()
            .filter(t -> {
                LocalDate d = t.getDateAsLocalDate();
                return !d.isBefore(from) && !d.isAfter(to);
            })
            .mapToDouble(t -> {
                if (t.getPercentageGiven() != null && t.getPercentageRecipientId() != null) {
                    return t.getAmountPaid() * (t.getPercentageGiven() / 100.0);
                }
                return 0.0;
            })
            .sum();
}

private Double getReceivedPercentsInRange(Long staffId, LocalDate from, LocalDate to) {
    List<Transaction> allTransactions = transactionRepository.findAll();

    return allTransactions.stream()
            .filter(t -> {
                LocalDate d = t.getDateAsLocalDate();
                return !d.isBefore(from) && !d.isAfter(to);
            })
            .mapToDouble(t -> {
                if (t.getPercentageRecipientId() != null 
                        && t.getPercentageRecipientId().equals(staffId) 
                        && t.getPercentageGiven() != null) {
                    return t.getAmountPaid() * (t.getPercentageGiven() / 100.0);
                }
                return 0.0;
            })
            .sum();
}

public Double calculatePureProfit(Long staffId, LocalDate from, LocalDate to) {
    Double income = filterAndSum(staffId, from, to);

    Double expenses = purchaseService.filterAndSum(staffId, from, to);

    Double givenPercents = getGivenPercentsInRange(staffId, from, to);
    Double receivedPercents = getReceivedPercentsInRange(staffId, from, to);

    Double katiaShare = katiaWorkService.getShareInRange(staffId, from, to);

    return (income != null ? income : 0.0)
         - (expenses != null ? expenses : 0.0)
         - (givenPercents != null ? givenPercents : 0.0)
         + (receivedPercents != null ? receivedPercents : 0.0)
         + (katiaShare != null ? katiaShare : 0.0);
}
public Double getPureProfitLastWeek(Long staffId) {
    LocalDate now = LocalDate.now();
    LocalDate weekAgo = now.minusWeeks(1);
    return calculatePureProfit(staffId, weekAgo, now);
}

public Double getPureProfitLastMonth(Long staffId) {
    LocalDate now = LocalDate.now();
    LocalDate monthAgo = now.minusMonths(1);
    return calculatePureProfit(staffId, monthAgo, now);
}

public Double getPureProfitLastYear(Long staffId) {
    LocalDate now = LocalDate.now();
    LocalDate yearAgo = now.minusYears(1);
    return calculatePureProfit(staffId, yearAgo, now);
}

    public Map<String, Double> getMonthlyPureProfit(Long staffId) {
        LocalDate start = LocalDate.of(2025, 8, 1); // September 2025
        LocalDate now = LocalDate.now();

        Map<String, Double> monthlyProfits = new LinkedHashMap<>();

        YearMonth current = YearMonth.from(start);
        YearMonth end = YearMonth.from(now);

        while (!current.isAfter(end)) {
            LocalDate firstDay = current.atDay(1);
            LocalDate lastDay = current.atEndOfMonth();

            Double monthlyProfit = calculatePureProfit(staffId, firstDay, lastDay);
            monthlyProfits.put(current.toString(), monthlyProfit);

            current = current.plusMonths(1);
        }

        return monthlyProfits;
    }


public Map<String, Double> getMonthlyIncome(Long staffId) {
    LocalDate start = LocalDate.of(2025, 8, 1); // or dynamically get earliest transaction
    LocalDate now = LocalDate.now();

    Map<String, Double> monthlyIncome = new LinkedHashMap<>();

    YearMonth current = YearMonth.from(start);
    YearMonth end = YearMonth.from(now);

    while (!current.isAfter(end)) {
        LocalDate firstDay = current.atDay(1);
        LocalDate lastDay = current.atEndOfMonth();

        // Sum all amountPaid for this month using getDateAsLocalDate()
        Double income = transactionRepository.findByStaffId(staffId).stream()
                .filter(t -> {
                    LocalDate d = t.getDateAsLocalDate();
                    return !d.isBefore(firstDay) && !d.isAfter(lastDay);
                })
                .mapToDouble(Transaction::getAmountPaid)
                .sum();

        monthlyIncome.put(current.toString(), income);
        current = current.plusMonths(1);
    }

    return monthlyIncome;
}


}
