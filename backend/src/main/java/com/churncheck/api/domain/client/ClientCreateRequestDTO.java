package com.churncheck.api.domain.client;

import java.math.BigDecimal;

public class ClientCreateRequestDTO {

    private String clientName;
    private String gender;
    private Integer nearLocation;
    private Integer partnerEmployee;
    private Integer promoFriends;
    private String clientPhone;
    private Integer age;
    private Integer contractPeriod;
    private Integer monthToEndContract;
    private Integer lifetimeMonths;
    private BigDecimal avgClassFrequencyTotal;
    private BigDecimal avgClassFrequencyCurrentMonth;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getNearLocation() {
        return nearLocation;
    }

    public void setNearLocation(Integer nearLocation) {
        this.nearLocation = nearLocation;
    }

    public Integer getPartnerEmployee() {
        return partnerEmployee;
    }

    public void setPartnerEmployee(Integer partnerEmployee) {
        this.partnerEmployee = partnerEmployee;
    }

    public Integer getPromoFriends() {
        return promoFriends;
    }

    public void setPromoFriends(Integer promoFriends) {
        this.promoFriends = promoFriends;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getContractPeriod() {
        return contractPeriod;
    }

    public void setContractPeriod(Integer contractPeriod) {
        this.contractPeriod = contractPeriod;
    }

    public Integer getMonthToEndContract() {
        return monthToEndContract;
    }

    public void setMonthToEndContract(Integer monthToEndContract) {
        this.monthToEndContract = monthToEndContract;
    }

    public Integer getLifetimeMonths() {
        return lifetimeMonths;
    }

    public void setLifetimeMonths(Integer lifetimeMonths) {
        this.lifetimeMonths = lifetimeMonths;
    }

    public BigDecimal getAvgClassFrequencyTotal() {
        return avgClassFrequencyTotal;
    }

    public void setAvgClassFrequencyTotal(BigDecimal avgClassFrequencyTotal) {
        this.avgClassFrequencyTotal = avgClassFrequencyTotal;
    }

    public BigDecimal getAvgClassFrequencyCurrentMonth() {
        return avgClassFrequencyCurrentMonth;
    }

    public void setAvgClassFrequencyCurrentMonth(BigDecimal avgClassFrequencyCurrentMonth) {
        this.avgClassFrequencyCurrentMonth = avgClassFrequencyCurrentMonth;
    }
}
