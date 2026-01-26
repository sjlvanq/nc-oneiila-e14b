package com.churncheck.api.domain.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.churncheck.api.domain.attendance.Attendance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Buscar cliente por Id (para Sprint 2)
    Optional<Client> findById(Long id);
    
    Optional<Client> findByDni(String dni);

    Page<Client> findAllByActiveTrue(Pageable pageable);
    
    // Validación de negocio - teléfono único
    boolean existsByClientPhone(String clientPhone);

    long countByActiveTrue();
    
    long countByActiveFalse();

    @Query("SELECT COUNT(c) FROM Client c WHERE c.active = true AND c.lastPredictionProbability > 0.7")
    long countHighRiskClients();

    @Query("SELECT c FROM Client c WHERE c.active = true AND c.lastPredictionProbability > 0.7 ORDER BY c.lastPredictionProbability DESC")
    List<Client> findTopHighRiskClients(Pageable pageable);

    @Query("SELECT AVG(c.age) FROM Client c")
    Double getAverageAge();
    
    @Query("SELECT a FROM Attendance a " +
            "WHERE a.client.id = :clientId " +
            "AND a.checkedInAt >= :startDate")
    List<Attendance> findAttendanceInLastSixMonths(Long clientId, LocalDateTime startDate);
}
