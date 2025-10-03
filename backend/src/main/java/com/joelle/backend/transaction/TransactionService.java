package com.joelle.backend.transaction;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joelle.backend.katiawork.KatiaWorkService;
import com.joelle.backend.purchase.ProductPurchaseService;
import com.joelle.backend.rent.Rent;
import com.joelle.backend.rent.RentRepository;
import com.joelle.backend.rent.RentService;
import com.joelle.backend.staff.Staff;
import com.joelle.backend.staff.StaffRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductPurchaseService purchaseService;
    private final KatiaWorkService katiaWorkService;
    private final StaffRepository staffRepository; // inject it
        private final RentRepository rentRepository; // inject it


@Autowired
public TransactionService(TransactionRepository transactionRepository,
                          ProductPurchaseService purchaseService,
                          KatiaWorkService katiaWorkService,
                          StaffRepository staffRepository,
                          RentRepository rentRepository) {
    this.transactionRepository = transactionRepository;
    this.purchaseService = purchaseService;
    this.katiaWorkService = katiaWorkService;
    this.staffRepository = staffRepository;
    this.rentRepository = rentRepository;
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

    // // Fixed deduction of 3000 on the first of each month ONLY for staffId = 5
    // Double fixedDeduction = 0.0;
    // if (staffId == 5L) {
    //     LocalDate temp = from.withDayOfMonth(1);
    //     while (!temp.isAfter(to)) {
    //         fixedDeduction += 3000.0;
    //         temp = temp.plusMonths(1);
    //     }
    // }

    return (income != null ? income : 0.0)
         - (expenses != null ? expenses : 0.0)
         - (givenPercents != null ? givenPercents : 0.0)
         + (receivedPercents != null ? receivedPercents : 0.0)
         + (katiaShare != null ? katiaShare : 0.0);
        //  - fixedDeduction;
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


public void deleteTransaction(Long transactionId) {
    if (!transactionRepository.existsById(transactionId)) {
        throw new IllegalArgumentException("Transaction with ID " + transactionId + " not found");
    }
    transactionRepository.deleteById(transactionId);
}

 
    public List<StaffPercentageDTO> getGivenPercentsGrouped(Long staffId, LocalDate from, LocalDate to) {
        List<Transaction> transactions = transactionRepository.findByStaffId(staffId);

        return transactions.stream()
                .filter(t -> t.getPercentageGiven() != null && t.getPercentageRecipientId() != null)
                .filter(t -> {
                    LocalDate d = t.getDateAsLocalDate();
                    return !d.isBefore(from) && !d.isAfter(to);
                })
                .collect(Collectors.groupingBy(
                        Transaction::getPercentageRecipientId,
                        Collectors.summingDouble(t -> t.getAmountPaid() * (t.getPercentageGiven() / 100.0))
                ))
                .entrySet().stream()
                .map(e -> {
                    Staff staff = staffRepository.findById(e.getKey()).orElse(null);
                    return new StaffPercentageDTO(
                        staff != null ? staff.getName() : "Unknown", 
                        e.getValue()
                    );
                })
                .toList();
    }
public List<StaffPercentageDTO> getReceivedPercentsGrouped(Long staffId, LocalDate from, LocalDate to) {
    // 1) Sum received percents coming from regular transactions (grouped by giver staffId)
    List<Transaction> allTransactions = transactionRepository.findAll();

    Map<Long, Double> receivedByGiver = allTransactions.stream()
            .filter(t -> t.getPercentageGiven() != null
                      && t.getPercentageRecipientId() != null
                      && t.getPercentageRecipientId().equals(staffId))
            .filter(t -> {
                LocalDate d = t.getDateAsLocalDate();
                return !d.isBefore(from) && !d.isAfter(to);
            })
            .collect(Collectors.groupingBy(
                    Transaction::getStaffId,
                    Collectors.summingDouble(t -> t.getAmountPaid() * (t.getPercentageGiven() / 100.0))
            ));

    // 2) Convert transaction-based entries to DTOs
    List<StaffPercentageDTO> result = receivedByGiver.entrySet().stream()
            .map(e -> {
                Staff staff = staffRepository.findById(e.getKey()).orElse(null);
                String name = staff != null ? staff.getName() : "Unknown";
                return new StaffPercentageDTO(name, e.getValue());
            })
            .collect(Collectors.toList());

    // 3) Add Katia (special-case) — sum her contribution to this recipient from katia_work table
    //    We use your KatiaWorkService which already sums dima/tamer cuts by staffId mapping.
    Double katiaContribution = katiaWorkService.getShareInRange(staffId, from, to);
    if (katiaContribution != null && katiaContribution != 0.0) {
        // Append Katia entry (name is literal since Katia is not a Staff row)
        result.add(new StaffPercentageDTO("Katia", katiaContribution));
    }

    // optional: sort descending by value so biggest givers come first
    result.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

    return result;
}

    // Wrappers for last week / month / year
    public List<StaffPercentageDTO> getGivenLastWeek(Long staffId) {
        LocalDate now = LocalDate.now();
        return getGivenPercentsGrouped(staffId, now.minusWeeks(1), now);
    }
    public List<StaffPercentageDTO> getGivenLastMonth(Long staffId) {
        LocalDate now = LocalDate.now();
        return getGivenPercentsGrouped(staffId, now.minusMonths(1), now);
    }
    public List<StaffPercentageDTO> getGivenLastYear(Long staffId) {
        LocalDate now = LocalDate.now();
        return getGivenPercentsGrouped(staffId, now.minusYears(1), now);
    }

    public List<StaffPercentageDTO> getReceivedLastWeek(Long staffId) {
        LocalDate now = LocalDate.now();
        return getReceivedPercentsGrouped(staffId, now.minusWeeks(1), now);
    }
    public List<StaffPercentageDTO> getReceivedLastMonth(Long staffId) {
        LocalDate now = LocalDate.now();
        return getReceivedPercentsGrouped(staffId, now.minusMonths(1), now);
    }
    public List<StaffPercentageDTO> getReceivedLastYear(Long staffId) {
        LocalDate now = LocalDate.now();
        return getReceivedPercentsGrouped(staffId, now.minusYears(1), now);
    }

}
