package com.churncheck.api.domain.client;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

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
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "active")
    private Boolean active = true;
    
    private Integer age;

    @Column(name = "avg_additional_charges_total")
    private BigDecimal avgAdditionalChargesTotal;

    @Column(name = "avg_class_frequency_current_month")
    private BigDecimal avgClassFrequencyCurrentMonth;    

    @Column(name = "avg_class_frequency_total")
    private BigDecimal avgClassFrequencyTotal;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_phone")
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

    public Client() {}

    // Factory Method - creación controlada
    public static Client createFromDto(ClientCreateRequestDTO dto, Partner partner) {
        Client client = new Client();
        
        // Validaciones de negocio en la entidad
        client.validateInitialData(dto);
        
        // Asignación controlada
        client.clientName = dto.clientName();
        client.clientPhone = dto.clientPhone();
        client.age = dto.age();
        client.gender = dto.gender();
        client.nearLocation = dto.nearLocation();
        client.partner = partner;
        client.promoFriends = dto.promoFriends();
        client.contractPeriod = dto.contractPeriod();
        client.groupVisits = dto.groupVisits();
        client.avgClassFrequencyTotal = dto.avgClassFrequencyTotal();
        client.avgClassFrequencyCurrentMonth = dto.avgClassFrequencyCurrentMonth();
        client.avgAdditionalChargesTotal = dto.avgAdditionalChargesTotal();
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
    
    public Integer getAge() {
		return age;
	}
    
    public BigDecimal getAvgAdditionalChargesTotal() {
		return this.avgAdditionalChargesTotal;
	}
    
    public BigDecimal getAvgClassFrequencyCurrentMonth() {
		return avgClassFrequencyCurrentMonth;
	}
   
	public BigDecimal getAvgClassFrequencyTotal() {
		return avgClassFrequencyTotal;
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

    public Instant getRegistrationDate() {
        return registrationDate;
    }

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public void setAvgClassFrequencyCurrentMonth(BigDecimal avgClassFrequencyCurrentMonth) {
		this.avgClassFrequencyCurrentMonth = avgClassFrequencyCurrentMonth;
	}

	public void setAvgClassFrequencyTotal(BigDecimal avgClassFrequencyTotal) {
		this.avgClassFrequencyTotal = avgClassFrequencyTotal;
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

	// Validaciones del dominio
    // TODO: Quitar cuando se implementen validaciones y manejo global de excepciones
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

}
