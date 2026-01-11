package com.churncheck.api.domain.client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.churncheck.api.domain.attendance.Attendance;
import com.churncheck.api.domain.charge.AdditionalCharge;
import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;

@Component
public class ClientPredictionMapper {

    public PredictionRequestDTO toPredictionRequest(Client client) {
    	Integer gender =
                client.getGender() == Gender.MALE ?  0 :
                client.getGender() == Gender.FEMALE ?  1 :  0;
    	
        Byte hasPhone = (byte) (client.getClientPhone() != null ? 1 : 0);
        Byte hasPartner = (byte) (client.getPartner() != null ? 1 : 0);
        
        Byte isNearLocation = (byte) (client.getNearLocation() ? 1 : 0);
        Byte isGroupVisits = (byte) (client.getGroupVisits() ? 1 : 0);
        Byte isPromoFriends = (byte) (client.getPromoFriends() ? 1 : 0);
        
        LocalDate today = LocalDate.now();
        LocalDate registrationDate = client.getRegistrationDate()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        Integer lifetime = (int) ChronoUnit.MONTHS.between(registrationDate, today);
        
        Map<YearMonth, java.util.List<AdditionalCharge>> chargesByMonth = client.getAdditionalCharges()
                .stream()
                .collect(Collectors.groupingBy(charge -> 
                        YearMonth.from(charge.getChargeDate())));
        
        BigDecimal sumOfMonthlyAverages = chargesByMonth.values().stream()
                .map(monthlyList -> {
                    BigDecimal monthlyTotal = monthlyList.stream()
                            .map(AdditionalCharge::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return monthlyTotal.divide(
                            BigDecimal.valueOf(monthlyList.size()), 2, RoundingMode.HALF_UP);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        LocalDate endContractDate = client.getContractStartDate().plusMonths(client.getContractPeriod());
        Integer monthsToEndContract = Period.between(today, endContractDate).getMonths();
        
        BigDecimal avgClassFrequencyTotal = calculateAvgClassFrequencyTotal(
                client.getAttendances(), registrationDate, today);
        
        BigDecimal avgClassFrequencyCurrentMonth = calculateAvgClassFrequencyCurrentMonth(
                client.getAttendances(), today);
        
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
                avgClassFrequencyTotal,
                avgClassFrequencyCurrentMonth
        );
    }
    
    /**
     * Calcula la frecuencia promedio de visitas por semana desde el registro.
     * Formula: Total de visitas / Total de semanas desde registro
     */
    private BigDecimal calculateAvgClassFrequencyTotal(
            List<Attendance> attendances, LocalDate registrationDate, LocalDate today) {
        
        if (attendances == null || attendances.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        // Total de semanas desde el registro
        long totalWeeks = ChronoUnit.WEEKS.between(registrationDate, today);
        if (totalWeeks == 0) {
            totalWeeks = 1; // Evitar división por cero
        }
        
        // Total de visitas
        long totalVisits = attendances.size();
        
        return BigDecimal.valueOf(totalVisits)
                .divide(BigDecimal.valueOf(totalWeeks), 2, RoundingMode.HALF_UP);
    }
    
    /**
     * Calcula la frecuencia promedio de visitas por semana en el mes actual.
     * Formula: Visitas del mes actual / Semanas transcurridas en el mes
     */
    private BigDecimal calculateAvgClassFrequencyCurrentMonth(
            List<Attendance> attendances, LocalDate today) {
        
        if (attendances == null || attendances.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        YearMonth currentMonth = YearMonth.from(today);
        
        // Filtrar asistencias del mes actual
        long visitsThisMonth = attendances.stream()
                .filter(a -> {
                    LocalDate attendanceDate = a.getCheckedInAt().toLocalDate();
                    return YearMonth.from(attendanceDate).equals(currentMonth);
                })
                .count();
        
        if (visitsThisMonth == 0) {
            return BigDecimal.ZERO;
        }
        
        // Calcular días transcurridos en el mes actual
        LocalDate firstDayOfMonth = currentMonth.atDay(1);
        long daysInMonth = ChronoUnit.DAYS.between(firstDayOfMonth, today) + 1; // +1 para incluir hoy
        
        // Convertir días a semanas (decimal)
        BigDecimal weeksInMonth = BigDecimal.valueOf(daysInMonth)
                .divide(BigDecimal.valueOf(7), 4, RoundingMode.HALF_UP);
        
        // Asegurar mínimo de 0.14 semanas (1 día)
        if (weeksInMonth.compareTo(BigDecimal.valueOf(0.14)) < 0) {
            weeksInMonth = BigDecimal.valueOf(0.14);
        }
        
        return BigDecimal.valueOf(visitsThisMonth)
                .divide(weeksInMonth, 2, RoundingMode.HALF_UP);
    }
}
