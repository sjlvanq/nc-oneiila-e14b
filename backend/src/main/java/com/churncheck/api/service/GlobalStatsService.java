package com.churncheck.api.service;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class GlobalStatsService {

    public Map<String, Object> getGlobalStats() {
        return Map.of(
            "totalClients", 78,
            "activeClients", 34,
            "averageAge", 29
        );
    }
}

