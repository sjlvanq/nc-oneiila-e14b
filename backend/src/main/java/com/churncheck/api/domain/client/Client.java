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
    private String email;
    private Boolean active;
    private Date subscriptionDate;
    private String gender;
    private String phoneRegistered;
    private String nearCity;
    private String age;

    public Client (ClientCreateRequestDTO clientCreateRequestDTO) {
        this.clientName = clientCreateRequestDTO.clientName();
        this.email = clientCreateRequestDTO.email();
        this.active = true;
        this.subscriptionDate = clientCreateRequestDTO.subscriptionDate();
        this.gender = clientCreateRequestDTO.gender();
        this.phoneRegistered = clientCreateRequestDTO.phoneRegistered();
        this.nearCity = clientCreateRequestDTO.nearCity();
        this.age = clientCreateRequestDTO.age();
    }

    public Long getId() {
		return id;
	}

	public String getClientName() {
		return clientName;
	}

	public String getEmail() {
		return email;
	}

	public Boolean getActive() {
		return active;
	}

	public Date getSubscriptionDate() {
		return subscriptionDate;
	}

	public String getGender() {
		return gender;
	}

	public String getPhoneRegistered() {
		return phoneRegistered;
	}

	public String getNearCity() {
		return nearCity;
	}

	public String getAge() {
		return age;
	}
    
    public void updateClientData(ClientUpdateRequestDTO clientUpdateRequestDTO) {
        if (clientUpdateRequestDTO.clientName() != null) {
            this.clientName = clientUpdateRequestDTO.clientName();
        }
        if (clientUpdateRequestDTO.email() != null) {
            this.email = clientUpdateRequestDTO.email();
        }
        
        if (clientUpdateRequestDTO.phoneRegistered() != null) {
            this.phoneRegistered = clientUpdateRequestDTO.phoneRegistered();
        }
        if (clientUpdateRequestDTO.nearCity() != null) {
            this.nearCity = clientUpdateRequestDTO.nearCity();
        }
        if (clientUpdateRequestDTO.age() != null) {
            this.age = clientUpdateRequestDTO.age();
        }
    }
    
    

}
