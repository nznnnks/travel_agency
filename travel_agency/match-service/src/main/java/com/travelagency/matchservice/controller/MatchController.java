package com.travelagency.matchservice.controller;

import com.travelagency.matchservice.dto.MatchRequestDto;
import com.travelagency.matchservice.dto.TravelMatchDto;
import com.travelagency.matchservice.entity.MatchStatus;
import com.travelagency.matchservice.service.MatchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "*")
public class MatchController {
    
    @Autowired
    private MatchService matchService;
    
    @PostMapping
    public ResponseEntity<TravelMatchDto> createMatch(@Valid @RequestBody MatchRequestDto request) {
        TravelMatchDto response = matchService.createMatch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TravelMatchDto> getMatchById(@PathVariable Long id) {
        TravelMatchDto response = matchService.getMatchById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<TravelMatchDto>> getAllMatches() {
        List<TravelMatchDto> responses = matchService.getAllMatches();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<TravelMatchDto>> getMatchesByTripId(@PathVariable Long tripId) {
        List<TravelMatchDto> responses = matchService.getMatchesByTripId(tripId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TravelMatchDto>> getMatchesByUserId(@PathVariable Long userId) {
        List<TravelMatchDto> responses = matchService.getMatchesByUserId(userId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TravelMatchDto>> getMatchesByStatus(@PathVariable MatchStatus status) {
        List<TravelMatchDto> responses = matchService.getMatchesByStatus(status);
        return ResponseEntity.ok(responses);
    }
    
    @PutMapping("/{id}/accept")
    public ResponseEntity<TravelMatchDto> acceptMatch(@PathVariable Long id) {
        TravelMatchDto response = matchService.acceptMatch(id);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/reject")
    public ResponseEntity<TravelMatchDto> rejectMatch(@PathVariable Long id) {
        TravelMatchDto response = matchService.rejectMatch(id);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }
    
    // Health check endpoints
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Match Service is running");
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Match Service test endpoint");
    }
    
    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("{\"status\":\"UP\",\"service\":\"match-service\"}");
    }
}