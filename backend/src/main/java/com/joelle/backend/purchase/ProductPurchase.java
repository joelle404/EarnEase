package com.joelle.backend.purchase;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    private LocalDateTime date = LocalDateTime.now();




    public ProductPurchase() {
    }

    public ProductPurchase(Staff staff, String productName, Double amountSpent, LocalDateTime date) {
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
    
