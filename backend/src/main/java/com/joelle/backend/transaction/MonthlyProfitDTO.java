package com.joelle.backend.transaction;

public class MonthlyProfitDTO {
    private String month;  // e.g. "2025-09"
    private Double profit;

    public MonthlyProfitDTO(String month, Double profit) {
        this.month = month;
        this.profit = profit;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }
}
