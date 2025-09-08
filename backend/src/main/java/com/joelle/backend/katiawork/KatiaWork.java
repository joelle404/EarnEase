package com.joelle.backend.katiawork;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "katia_work")
public class KatiaWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientName;

    private LocalDate serviceDate;

    private Double grossAmount;

    private Double dimaCut;

    private Double tamerPercent;

    private Double tamerCut;

    private Double katiaNet;

    // Auto calculation method
    public void calculateCuts() {
        this.dimaCut = this.grossAmount * 0.25; // fixed 25%
        this.tamerCut = this.grossAmount * (this.tamerPercent / 100.0);
        this.katiaNet = this.grossAmount - this.dimaCut - this.tamerCut;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public LocalDate getServiceDate() { return serviceDate; }
    public void setServiceDate(LocalDate serviceDate) { this.serviceDate = serviceDate; }

    public Double getGrossAmount() { return grossAmount; }
    public void setGrossAmount(Double grossAmount) { this.grossAmount = grossAmount; }

    public Double getDimaCut() { return dimaCut; }
    public void setDimaCut(Double dimaCut) { this.dimaCut = dimaCut; }

    public Double getTamerPercent() { return tamerPercent; }
    public void setTamerPercent(Double tamerPercent) { this.tamerPercent = tamerPercent; }

    public Double getTamerCut() { return tamerCut; }
    public void setTamerCut(Double tamerCut) { this.tamerCut = tamerCut; }

    public Double getKatiaNet() { return katiaNet; }
    public void setKatiaNet(Double katiaNet) { this.katiaNet = katiaNet; }
}
