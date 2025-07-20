package com.joelle.backend.staff;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff_payments")
public class StaffPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payer_id")
    private Staff payer;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Staff recipient;

    private Double amount;
    private String reason;
    private LocalDateTime date = LocalDateTime.now();




    
    public StaffPayment() {
    }
    public StaffPayment(Staff payer, Staff recipient, Double amount, String reason, LocalDateTime date) {
        this.payer = payer;
        this.recipient = recipient;
        this.amount = amount;
        this.reason = reason;
        this.date = date;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Staff getPayer() {
        return payer;
    }
    public void setPayer(Staff payer) {
        this.payer = payer;
    }
    public Staff getRecipient() {
        return recipient;
    }
    public void setRecipient(Staff recipient) {
        this.recipient = recipient;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    
}
