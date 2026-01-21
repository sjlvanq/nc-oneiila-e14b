package com.churncheck.api.domain.client.dto.statistics;

import java.time.YearMonth;
import java.util.Map;

public record ClientStatisticsDTO(
    Map<YearMonth, Long> monthlyAttendanceLastSixMonths,
    AdditionalChargesDTO additionalChargesByCat
) {}
