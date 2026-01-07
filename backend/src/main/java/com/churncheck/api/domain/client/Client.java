package com.churncheck.api.domain.client;

import java.math.BigDecimal;

import com.churncheck.api.domain.client.dto.ClientCreateRequestDTO;
import com.churncheck.api.domain.client.dto.ClientUpdateRequestDTO;

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

    @Column(name = "lifetime")
    private Integer lifetime;

    @Column(name = "avg_class_frequency_total")
    private BigDecimal avgClassFrequencyTotal;

    @Column(name = "avg_class_frequency_current_month")
    private BigDecimal avgClassFrequencyCurrentMonth;

    @Column(name = "group_visit")
    private Integer groupVisit;
    
    @Column(name = "avg_additional_charges_total")
    private BigDecimal avgAdditionalChargesTotal;
    
    public Client() {}
    
    // Factory Method - creación controlada
    public static Client createFromDto(ClientCreateRequestDTO dto) {
        Client client = new Client();
        
        // Validaciones de negocio en la entidad
        client.validateInitialData(dto);
        
        // Asignación controlada
        client.clientName = dto.clientName();
        client.clientPhone = dto.clientPhone();
        client.age = dto.age();
        client.gender = dto.gender();
        client.nearLocation = dto.nearLocation();
        client.partnerEmployee = dto.partnerEmployee();
        client.promoFriends = dto.promoFriends();
        client.contractPeriod = dto.contractPeriod();
        client.monthToEndContract = dto.monthToEndContract();
        client.lifetime = dto.lifetime();
        client.groupVisit = dto.groupVisit();
        client.avgClassFrequencyTotal = dto.avgClassFrequencyTotal();
        client.avgClassFrequencyCurrentMonth = dto.avgClassFrequencyCurrentMonth();
        client.avgAdditionalChargesTotal = dto.avgAdditionalChargesTotal();
        client.active = dto.active() != null ? dto.active() : true;
        
        return client;
    }
    
    // Validaciones del dominio
    private void validateInitialData(ClientCreateRequestDTO dto) {
        if (dto.clientName() == null || dto.clientName().trim().isEmpty()) {
            throw new DomainException("Client name is required");
        }
        if (dto.clientPhone() == null || dto.clientPhone().trim().isEmpty()) {
            throw new DomainException("Client phone is required");
        }
        if (dto.age() != null && (dto.age() < 16 || dto.age() > 100)) {
            throw new DomainException("Age must be between 16 and 100");
        }
        if (dto.avgClassFrequencyTotal() != null && dto.avgClassFrequencyTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Average frequency cannot be negative");
        }
        if (dto.avgClassFrequencyCurrentMonth() != null && dto.avgClassFrequencyCurrentMonth().compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Current month frequency cannot be negative");
        }
        if (dto.avgAdditionalChargesTotal() != null && dto.avgAdditionalChargesTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Additional charges cannot be negative");
        }
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

	public Integer getLifetime() {
		return lifetime;
	}

	public void setLifetime(Integer lifetime) {
		this.lifetime = lifetime;
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

	public BigDecimal getAvgAdditionalChargesTotal() {
		return this.avgAdditionalChargesTotal;
	}
	
	public Integer getGroupVisit() {
		return this.groupVisit;
	}
	
	public void setGroupVisit(Integer groupVisit) {
		this.groupVisit = groupVisit;
	}

}
