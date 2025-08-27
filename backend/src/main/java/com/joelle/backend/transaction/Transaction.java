package com.joelle.backend.transaction;

import jakarta.persistence.*;

import com.joelle.backend.service.Service;
import com.joelle.backend.staff.Staff;
import java.time.LocalDate;
import java.time.LocalTime;
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "staff_id", nullable = false)
    private Long staffId;

    @Column(name = "service_id", nullable = false)
    private Long serviceId;

    @Column(name = "amount_paid", nullable = false)
    private Double amountPaid;

    @Column(name = "percentage_given", nullable = true)
    private Double percentageGiven;

    @Column(name = "percentage_recipient_id", nullable = true)
    private Long percentageRecipientId;

    @Column(name = "date", nullable = false)
    private String date; // "2025-08-27"

    @Column(name = "time", nullable = false)
    private String time; // "14:30"

    public Transaction() {}

    public Transaction(String clientName, Long staffId, Long serviceId, Double amountPaid,
                       Double percentageGiven, Long percentageRecipientId,
                       String date, String time) {
        this.clientName = clientName;
        this.staffId = staffId;
        this.serviceId = serviceId;
        this.amountPaid = amountPaid;
        this.percentageGiven = percentageGiven;
        this.percentageRecipientId = percentageRecipientId;
        this.date = date;
        this.time = time;
    }

    // getters and setters



    // === Getters & Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }



    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getPercentageRecipientId() {
        return percentageRecipientId;
    }

    public void setPercentageRecipientId(Long percentageRecipientId) {
        this.percentageRecipientId = percentageRecipientId;
    }

    public Double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(Double amountPaid) { this.amountPaid = amountPaid; }

    public Double getPercentageGiven() { return percentageGiven; }
    public void setPercentageGiven(Double percentageGiven) { this.percentageGiven = percentageGiven; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
