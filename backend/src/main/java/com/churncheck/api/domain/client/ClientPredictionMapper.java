package com.churncheck.api.domain.client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.churncheck.api.domain.charge.AdditionalCharge;
import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;

@Component
public class ClientPredictionMapper {

    public PredictionRequestDTO toPredictionRequest(Client client) {

        Integer gender =
                client.getGender() == Gender.MALE ? 0 :
                client.getGender() == Gender.FEMALE ? 1 : 0;

        Byte hasPhone = (byte) (client.getClientPhone() != null ? 1 : 0);
        Byte hasPartner = (byte) (client.getPartner() != null ? 1 : 0);

        Byte isNearLocation = (byte) (Boolean.TRUE.equals(client.getNearLocation()) ? 1 : 0);
        Byte isGroupVisits = (byte) (Boolean.TRUE.equals(client.getGroupVisits()) ? 1 : 0);
        Byte isPromoFriends = (byte) (Boolean.TRUE.equals(client.getPromoFriends()) ? 1 : 0);

        LocalDate today = LocalDate.now();
        LocalDate registrationDate = client.getRegistrationDate()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        Integer lifetime = (int) ChronoUnit.MONTHS.between(registrationDate, today);
    
        Map<YearMonth, java.util.List<AdditionalCharge>> chargesByMonth =
                client.getAdditionalCharges()
                        .stream()
                        .collect(Collectors.groupingBy(
                                charge -> YearMonth.from(charge.getChargeDate())
                        ));

        BigDecimal sumOfMonthlyAverages = chargesByMonth.values().stream()
                .map(monthlyList -> {
                    BigDecimal monthlyTotal = monthlyList.stream()
                            .map(AdditionalCharge::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return monthlyTotal.divide(
                            BigDecimal.valueOf(monthlyList.size()),
                            2,
                            RoundingMode.HALF_UP
                    );
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDate endContractDate =
                client.getContractStartDate().plusMonths(client.getContractPeriod());

        Integer monthsToEndContract =
                (int) ChronoUnit.MONTHS.between(today, endContractDate);

        return new PredictionRequestDTO(
                gender,
                isNearLocation,
                hasPartner,
                isPromoFriends,
                hasPhone,
                client.getContractPeriod(),
                isGroupVisits,
                client.getAge(),
                sumOfMonthlyAverages,
                monthsToEndContract,
                lifetime,
                client.getAvgClassFrequencyTotal(),
                client.getAvgClassFrequencyCurrentMonth()
        );
    }
}
