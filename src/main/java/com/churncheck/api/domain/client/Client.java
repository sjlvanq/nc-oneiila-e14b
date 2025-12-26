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
    private String name;
    private String email;
    private Boolean active;
    private Date subscriptionDate;

    public Client (ClientCreateRequestDTO clientCreateRequestDTO) {
        this.name = clientCreateRequestDTO.name();
        this.email = clientCreateRequestDTO.email();
        this.active = true;
        this.subscriptionDate = clientCreateRequestDTO.subscriptionDate();
    }

}
