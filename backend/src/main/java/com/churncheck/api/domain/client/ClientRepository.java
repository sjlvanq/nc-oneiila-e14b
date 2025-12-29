package com.churncheck.api.domain.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Buscar cliente por DNI (para Sprint 2)
    Optional<Client> findByClientPhone(String clientPhone);

    // Buscar solo clientes activos
    List<Client> findAllByActiveTrue();
}
