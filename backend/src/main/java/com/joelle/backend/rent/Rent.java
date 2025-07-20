package com.joelle.backend.rent;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.joelle.backend.staff.Staff;

@Entity
@Table(name = "rents", uniqueConstraints = @UniqueConstraint(columnNames = {"staff_id", "month"}))
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    private String month;
    private Double amount;

    @Column(name = "paid_date")
    private LocalDateTime paidDate = LocalDateTime.now();




    

    public Rent() {
    }

    public Rent(Staff staff, String month, Double amount, LocalDateTime paidDate) {
        this.staff = staff;
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

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
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

    public LocalDateTime getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDateTime paidDate) {
        this.paidDate = paidDate;
    }

    
}