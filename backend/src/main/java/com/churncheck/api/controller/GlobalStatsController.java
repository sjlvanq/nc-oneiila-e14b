package com.churncheck.api.controller;

import com.churncheck.api.service.GlobalStatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/stats")
public class GlobalStatsController {


    private final GlobalStatsService globalStatsService;

    public GlobalStatsController(GlobalStatsService globalStatsService) {
        this.globalStatsService = globalStatsService;
    }

    @GetMapping("/global")
    public Map<String, Object> getGlobalStats() {
        return globalStatsService.getGlobalStats();
    }
}
