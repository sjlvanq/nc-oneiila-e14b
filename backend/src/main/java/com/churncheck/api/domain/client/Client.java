package com.churncheck.api.domain.client;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Formula;

import com.churncheck.api.domain.attendance.Attendance;
import com.churncheck.api.domain.charge.AdditionalCharge;
import com.churncheck.api.domain.client.dto.ClientCreateRequestDTO;
import com.churncheck.api.domain.client.dto.ClientUpdateRequestDTO;
import com.churncheck.api.domain.partner.Partner;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "active")
    private Boolean active = true;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<AdditionalCharge> additionalCharges = new ArrayList<>();

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Formula("TIMESTAMPDIFF(YEAR, birth_date, CURDATE())")
    private Integer age;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Attendance> attendances = new ArrayList<>();

    @Column(name = "name")
    private String clientName;

    @Column(name = "phone")
    private String clientPhone;

    @Column(name = "contract_period")
    private Integer contractPeriod;

    @Column(name = "contract_start_date")
    private LocalDate contractStartDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "group_visit")
    private Boolean groupVisits;

    @Column(name = "near_location")
    private Boolean nearLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @Column(name = "promo_friends")
    private Boolean promoFriends;

    @Column(name = "registration_date")
    private Instant registrationDate;

    @Column(name = "last_prediction_churn")
    private Byte lastPredictionChurn;

    @Column(name = "last_prediction_probability")
    private Double lastPredictionProbability;

    @Column(name = "last_prediction_timestamp")
    private Instant lastPredictionTimestamp;

    public Client() {
    }

    public static Client createFromDto(ClientCreateRequestDTO dto, Partner partner) {
        Client client = new Client();
        client.clientName = dto.clientName();
        client.clientPhone = dto.clientPhone();
        client.birthDate = dto.birthDate();
        client.gender = dto.gender();
        client.nearLocation = dto.nearLocation();
        client.partner = partner;
        client.promoFriends = dto.promoFriends();
        client.contractPeriod = dto.contractPeriod();
        client.groupVisits = dto.groupVisits();
        client.active = dto.active() != null ? dto.active() : true;

        client.registrationDate = Instant.now();
        client.contractStartDate = LocalDate.now();

        return client;
    }

    public void deleteClient() {
        this.active = false;
    }

    public Boolean getActive() {
        return active;
    }

    public List<AdditionalCharge> getAdditionalCharges() {
        return additionalCharges;
    }

    public Integer getAge() {
        return age;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public String getClientName() {
        return clientName;
    }

    public String getclientPhone() {
        return this.clientPhone;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public Integer getContractPeriod() {
        return contractPeriod;
    }

    public LocalDate getContractStartDate() {
        return contractStartDate;
    }

    public Gender getGender() {
        return gender;
    }

    public Boolean getGroupVisits() {
        return this.groupVisits;
    }

    public Long getId() {
        return id;
    }

    public Boolean getNearLocation() {
        return nearLocation;
    }

    public Partner getPartner() {
        return this.partner;
    }

    public Boolean getPromoFriends() {
        return promoFriends;
    }

    public Byte getLastPredictionChurn() { 
        return lastPredictionChurn; 
    }

    public Double getLastPredictionProbability() { 
        return lastPredictionProbability; 
    }

    public Instant getLastPredictionTimestamp() { 
        return lastPredictionTimestamp; 
    }
    
    public Instant getRegistrationDate() {
        return registrationDate;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public void setContractPeriod(Integer contractPeriod) {
        this.contractPeriod = contractPeriod;
    }

    public void setContractStartDate(LocalDate contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setGroupVisit(Boolean groupVisits) {
        this.groupVisits = groupVisits;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNearLocation(Boolean nearLocation) {
        this.nearLocation = nearLocation;
    }

    public void setPromoFriends(Boolean promoFriends) {
        this.promoFriends = promoFriends;
    }

    public void setLastPredictionChurn(Byte churn) { 
        this.lastPredictionChurn = churn; 
    }

    public void setLastPredictionProbability(Double probability) { 
        this.lastPredictionProbability = probability; 
    }

    public void setLastPredictionTimestamp(Instant timestamp) { 
        this.lastPredictionTimestamp = timestamp; 
    }

    public void updateClientData(ClientUpdateRequestDTO clientUpdateRequestDTO) {
        if (clientUpdateRequestDTO.clientName() != null) {
            this.clientName = clientUpdateRequestDTO.clientName();
        }

        if (clientUpdateRequestDTO.clientPhone() != null) {
            this.clientPhone = clientUpdateRequestDTO.clientPhone();
        }
        if (clientUpdateRequestDTO.nearLocation() != null) {
            this.nearLocation = clientUpdateRequestDTO.nearLocation();
        }
        if (clientUpdateRequestDTO.birthDate() != null) {
            this.birthDate = clientUpdateRequestDTO.birthDate();
        }
    }
}
