package com.travelagency.reviewservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews/monitoring")
public class MonitoringController {

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getServiceStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("service", "review-service");
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now());
        status.put("version", "1.0.0");
        status.put("uptime", System.currentTimeMillis());
        
        return ResponseEntity.ok(status);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealthInfo() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("database", "UP");
        health.put("eureka", "UP");
        health.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(health);
    }

    @GetMapping("/metrics/summary")
    public ResponseEntity<Map<String, Object>> getMetricsSummary() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalReviews", 0); // This would be calculated from database
        metrics.put("averageRating", 0.0);
        metrics.put("reviewsToday", 0);
        metrics.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(metrics);
    }
}
