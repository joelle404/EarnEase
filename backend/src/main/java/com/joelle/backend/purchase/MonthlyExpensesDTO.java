package com.joelle.backend.purchase;

public class MonthlyExpensesDTO {
    private String month;  // e.g. "2025-08"
    private Double amount;

    public MonthlyExpensesDTO(String month, Double amount) {
        this.month = month;
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
