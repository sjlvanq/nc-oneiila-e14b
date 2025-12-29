package com.churncheck.api.domain.client;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Date;

@Table(name = "clients")
@Entity(name = "Client")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String clientName;
    private String clientEmail;
    private Boolean active;
    private Date subscriptionDate;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String clientPhone;
    private Integer nearLocation;
    private Integer age;

    public Client (ClientCreateRequestDTO clientCreateRequestDTO) {
        this.clientName = clientCreateRequestDTO.clientName();
        this.clientEmail = clientCreateRequestDTO.clientEmail();
        this.active = true;
        this.subscriptionDate = new Date();
        this.gender = clientCreateRequestDTO.gender();
        this.clientPhone = clientCreateRequestDTO.clientPhone();
        this.nearLocation = clientCreateRequestDTO.nearLocation();
        this.age = clientCreateRequestDTO.age();
    }

    public Long getId() {
		return id;
	}

	public String getClientName() {
		return clientName;
	}

	public String getClientEmail() {
		return clientEmail;
	}

	public Boolean getActive() {
		return active;
	}

	public Date getSubscriptionDate() {
		return subscriptionDate;
	}

	public Gender getGender() {
		return gender;
	}

	public String getClientPhone() {
		return clientPhone;
	}

	public Integer getNearLocation() {
		return nearLocation;
	}

	public Integer getAge() {
		return age;
	}
    
    public void updateClientData(ClientUpdateRequestDTO clientUpdateRequestDTO) {
        if (clientUpdateRequestDTO.clientName() != null) {
            this.clientName = clientUpdateRequestDTO.clientName();
        }
        if (clientUpdateRequestDTO.clientEmail() != null) {
            this.clientEmail = clientUpdateRequestDTO.clientEmail();
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

}
