package com.joelle.backend.rent;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "rents", uniqueConstraints = @UniqueConstraint(columnNames = {"staff_id", "month"}))
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "staff_id", nullable = false)
    private Long staffId;

    private String month;
    private Double amount;

    @Column(name = "paid_date")
    private LocalDate paidDate = LocalDate.now();

    public Rent() {}

    public Rent(Long staffId, String month, Double amount, LocalDate paidDate) {
        this.staffId = staffId;
        this.month = month;
        this.amount = amount;
        this.paidDate = paidDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
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

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }
}
