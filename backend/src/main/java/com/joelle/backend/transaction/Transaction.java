package com.joelle.backend.transaction;

import jakarta.persistence.*;

import java.security.Provider.Service;
import java.time.LocalDateTime;

import com.joelle.backend.clients.Clients;
import com.joelle.backend.staff.Staff;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Clients client;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @Column(name = "amount_paid")
    private Double amountPaid;

    @Column(name = "percentage_given")
    private Double percentageGiven;

    @ManyToOne
    @JoinColumn(name = "percentage_recipient_id")
    private Staff percentageRecipient;

    private LocalDateTime date = LocalDateTime.now();


    

    public Transaction() {
    }

    public Transaction(Clients client, Staff staff, Service service, Double amountPaid, Double percentageGiven,
            Staff percentageRecipient, LocalDateTime date) {
        this.client = client;
        this.staff = staff;
        this.service = service;
        this.amountPaid = amountPaid;
        this.percentageGiven = percentageGiven;
        this.percentageRecipient = percentageRecipient;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Double getPercentageGiven() {
        return percentageGiven;
    }

    public void setPercentageGiven(Double percentageGiven) {
        this.percentageGiven = percentageGiven;
    }

    public Staff getPercentageRecipient() {
        return percentageRecipient;
    }

    public void setPercentageRecipient(Staff percentageRecipient) {
        this.percentageRecipient = percentageRecipient;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }



    
    // Getters & setters
}
