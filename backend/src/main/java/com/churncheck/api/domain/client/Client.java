package com.churncheck.api.domain.client;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_name")
    private String clientName;

    private String gender;

    @Column(name = "near_location")
    private Integer nearLocation;

    @Column(name = "partner_employee")
    private Integer partnerEmployee;

    @Column(name = "promo_friends")
    private Integer promoFriends;

    @Column(name = "client_phone")
    private String clientPhone;

    private Integer age;

    @Column(name = "contract_period")
    private Integer contractPeriod;

    @Column(name = "month_to_end_contract")
    private Integer monthToEndContract;

    @Column(name = "lifetime_months")
    private Integer lifetimeMonths;

    @Column(name = "avg_class_frequency_total")
    private BigDecimal avgClassFrequencyTotal;

    @Column(name = "avg_class_frequency_current_month")
    private BigDecimal avgClassFrequencyCurrentMonth;

    private Integer churn;

    // 🔴 CAMBIO CLAVE
    @Column(name = "active")
    private Boolean active;

    // ========= GETTERS / SETTERS =========

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.cli
