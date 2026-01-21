package com.churncheck.api.domain.charge;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.churncheck.api.domain.client.Client;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "additional_charges")
public class AdditionalCharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private LocalDate chargeDate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "charge_type_id")
    private ChargeType chargeType;
    
    public String getChargeType() {
        return chargeType.getName();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getChargeDate() {
        return chargeDate;
    }
}