package com.feetness.feetness.controllers;

import com.feetness.feetness.models.Statistics;
import com.feetness.feetness.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<Statistics> getStatistics() {
        Statistics statistics = statisticsService.getStatistics();
        return ResponseEntity.ok(statistics);
    }
}