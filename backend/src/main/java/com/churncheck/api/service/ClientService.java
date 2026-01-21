package com.churncheck.api.service;

import com.churncheck.api.domain.charge.AdditionalCharge;
import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientRepository;
import com.churncheck.api.domain.client.dto.ClientCreateRequestDTO;
import com.churncheck.api.domain.client.dto.ClientFullResponseDTO;
import com.churncheck.api.domain.client.dto.ClientListResponseDTO;
import com.churncheck.api.domain.client.dto.ClientResponseDTO;
import com.churncheck.api.domain.client.dto.ClientUpdateRequestDTO;
import com.churncheck.api.domain.client.dto.GlobalStatisticsDTO;
import com.churncheck.api.domain.client.dto.prediction.PredictionResponseDTO;
import com.churncheck.api.domain.client.dto.statistics.AdditionalChargesDTO;
import com.churncheck.api.domain.client.dto.statistics.CategoryChargeDTO;
import com.churncheck.api.domain.client.dto.statistics.ClientStatisticsDTO;
import com.churncheck.api.domain.partner.Partner;
import com.churncheck.api.domain.partner.PartnerRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final PartnerRepository partnerRepository;
    private final ChurnService churnService;

    public ClientService(
            ClientRepository clientRepository, 
            PartnerRepository partnerRepository,
            ChurnService churnService) {
        this.clientRepository = clientRepository;
        this.partnerRepository = partnerRepository;
        this.churnService = churnService;
    }

    // Crear cliente desde DTO (alineado al gym churn dataset)
    public ClientResponseDTO createFromDto(ClientCreateRequestDTO dto) {
        Partner partner = null;
        if (dto.partnerId() != null) {
            partner = partnerRepository.findById(dto.partnerId())
                .orElseThrow(() -> new EntityNotFoundException("Partner not found"));
        }
        Client client = Client.createFromDto(dto, partner);
        Client savedClient = clientRepository.save(client);
        return new ClientResponseDTO(savedClient);
    }
    
    public ClientResponseDTO updateClient(ClientUpdateRequestDTO clientUpdateRequestDTO){
        Client client = clientRepository.getReferenceById(clientUpdateRequestDTO.id());
        client.updateClientData(clientUpdateRequestDTO);
        Client savedClient = clientRepository.save(client);
        return new ClientResponseDTO(savedClient);
    }

    public Page<ClientListResponseDTO> findAllByActiveTrue(Pageable pageable){
        return clientRepository.findAllByActiveTrue(pageable).map(ClientListResponseDTO::new);
    }

    public ClientResponseDTO findById(Long id){
        Client client = clientRepository.getReferenceById(id);
        return new ClientResponseDTO(client);
    }

    public void deleteClient(Long id){
        Client client = clientRepository.getReferenceById(id);
        client.deleteClient();
        clientRepository.save(client);
    }

    public ClientFullResponseDTO predictChurn(Long clientId){
    	Client client = clientRepository.findById(clientId).orElseThrow();
    	PredictionResponseDTO prediction = churnService.predict(client);

        return new ClientFullResponseDTO(client, prediction);
    }

    public ClientFullResponseDTO predictChurnByDni(String dni) {
        Client client = clientRepository.findByDni(dni).orElseThrow();
        PredictionResponseDTO prediction = churnService.predict(client);
        
        return new ClientFullResponseDTO(client, prediction);
    }

    public GlobalStatisticsDTO getGlobalStats() {
        long total = clientRepository.count();
        long active = clientRepository.countByActiveTrue();
        Double avgAge = clientRepository.getAverageAge();

        // Redondeo a 1 decimal
        double roundedAvg = (avgAge != null) ? Math.round(avgAge * 10.0) / 10.0 : 0.0;

        return new GlobalStatisticsDTO(total, active, roundedAvg);
    }

    public ClientStatisticsDTO getClientStatistics(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(()->new EntityNotFoundException());
        
        List<AdditionalCharge> charges = client.getAdditionalCharges();
        
        // Gastos por categorías
        BigDecimal totalAmount = charges.stream() //parallelStream
                .map(AdditionalCharge::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Map<String, BigDecimal> amountByType = charges.stream()
                    .collect(Collectors.groupingBy(
                            AdditionalCharge::getChargeType,
                            Collectors.mapping(AdditionalCharge::getAmount, 
                                    Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                            ));
        
        List<CategoryChargeDTO> breakdown = amountByType.entrySet().stream()
                .map(entry -> {
                    String type = entry.getKey();
                    BigDecimal amount = entry.getValue().setScale(2, RoundingMode.HALF_UP);
                    
                    // Cálculo de porcentaje
                    Double percentage = 0.0;
                    if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
                        percentage = amount.multiply(new BigDecimal("100"))
                                .divide(totalAmount, 2, RoundingMode.HALF_UP)
                                .doubleValue();
                    }

                    return new CategoryChargeDTO(type, amount, percentage);
                })
                .toList();
        
        //---
        //List<Attendance> attendanceInLastSixMonths = clientRepository.findAttendanceInLastSixMonths(null);        
        
        
        // ----
        AdditionalChargesDTO additionalCharges = new AdditionalChargesDTO(totalAmount, breakdown);
        return new ClientStatisticsDTO(null, additionalCharges);
    }
    
}

