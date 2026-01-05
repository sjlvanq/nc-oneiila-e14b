package com.churncheck.api.domain.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Buscar cliente por Id (para Sprint 2)
    Optional<Client> findById(Long id);

    Page<Client> findAllByActiveTrue(Pageable pageable);
    
    // Validación de negocio - teléfono único
    boolean existsByClientPhone(String clientPhone);
}
