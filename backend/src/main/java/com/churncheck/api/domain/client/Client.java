package com.churncheck.api.domain.client;

import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class Client {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "active")
    private Boolean active = true;
    
    @Column(name = "client_name")
    private String clientName;
    
    @Column(name = "client_phone")
    private String clientPhone;

    private Integer age;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "near_location")
    private Integer nearLocation;

    @Column(name = "partner_employee")
    private Integer partnerEmployee;

    @Column(name = "promo_friends")
    private Integer promoFriends;

    @Column(name = "contract_period")
    private Integer contractPeriod;
    
    @Column(name = "month_to_end_contract")
    private Integer monthToEndContract;

    @Column(name = "lifetime_months")
    private Integer lifetimeMonths;

    @Column(name = "avg_class_frequency_total")
    private Double avgClassFrequencyTotal;

    @Column(name = "avg_class_frequency_current_month")
    private Double avgClassFrequencyCurrentMonth;

    @Column(name = "group_visit")
    private Boolean groupVisit;
    
    @Column(name = "avg_additional_charges_total")
    private Double avgAdditionalChargesTotal;
    
    public Client() {}
    
    public Client(ClientCreateRequestDTO clientCreateRequest) {
    	
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
        if (clientUpdateRequestDTO.age() != null) {
            this.age = clientUpdateRequestDTO.age();
        }
    }
    
    public void deleteClient() {
        this.active = false;
    }

    // Getters y setters
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
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

	public Double getAvgClassFrequencyTotal() {
		return avgClassFrequencyTotal;
	}

	public void setAvgClassFrequencyTotal(Double double1) {
		this.avgClassFrequencyTotal = double1;
	}

	public Double getAvgClassFrequencyCurrentMonth() {
		return avgClassFrequencyCurrentMonth;
	}

	public void setAvgClassFrequencyCurrentMonth(Double avgClassFrequencyCurrentMonth) {
		this.avgClassFrequencyCurrentMonth = avgClassFrequencyCurrentMonth;
	}

	public Double getAvgAdditionalChargesTotal() {
		return this.avgAdditionalChargesTotal;
	}
	
	public Boolean getGroupVisit() {
		return this.groupVisit;
	}

}
