package com.joelle.backend.purchase;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductPurchaseService {

    private final ProductPurchaseRepository purchaseRepository;

    @Autowired
    public ProductPurchaseService(ProductPurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    // Total of all purchases
    public Double getTotalAmount(Long staffId) {
        List<ProductPurchase> purchases = purchaseRepository.findByStaffId(staffId);
        return purchases.stream()
                        .mapToDouble(ProductPurchase::getAmountSpent)
                        .sum();
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

    // Helper method to filter purchases by date range and sum
    private Double filterAndSum(Long staffId, LocalDate from, LocalDate to) {
        List<ProductPurchase> purchases = purchaseRepository.findByStaffId(staffId);

        return purchases.stream()
                .filter(p -> {
                    LocalDate d = p.getDateAsLocalDate();
                    return !d.isBefore(from) && !d.isAfter(to);
                })
                .mapToDouble(ProductPurchase::getAmountSpent)
                .sum();
    }
}
