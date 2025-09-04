package com.joelle.backend.purchase;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.joelle.backend.staff.Staff;

@Entity
@Table(name = "product_purchases")
public class ProductPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "amount_spent")
    private Double amountSpent;

    @Column(name = "date", nullable = false)
    private String date; // "2025-08-27"



    public ProductPurchase() {
    }

    public ProductPurchase(Staff staff, String productName, Double amountSpent, String date) {
        this.staff = staff;
        this.productName = productName;
        this.amountSpent = amountSpent;
        this.date = date;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(Double amountSpent) {
        this.amountSpent = amountSpent;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    @JsonIgnore
public LocalDate getDateAsLocalDate() {
    return LocalDate.parse(this.date); // works if "YYYY-MM-DD"
}
}
    
