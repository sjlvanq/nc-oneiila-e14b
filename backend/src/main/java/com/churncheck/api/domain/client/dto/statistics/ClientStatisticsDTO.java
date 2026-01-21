package com.churncheck.api.domain.client.dto.statistics;

import java.util.List;

public record ClientStatisticsDTO(
    List<Long> monthlyAttendanceLastSixMonths,
    AdditionalChargesDTO additionalChargesByCat
) {}
