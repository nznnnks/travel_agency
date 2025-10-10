package com.travelagency.matchservice.service;

import com.travelagency.matchservice.dto.MatchRequestDto;
import com.travelagency.matchservice.dto.TravelMatchDto;
import com.travelagency.matchservice.entity.TravelMatch;
import com.travelagency.matchservice.entity.MatchStatus;
import com.travelagency.matchservice.exception.MatchNotFoundException;
import com.travelagency.matchservice.repository.TravelMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MatchService {
    
    @Autowired
    private TravelMatchRepository travelMatchRepository;
    
    public TravelMatchDto createMatch(MatchRequestDto request) {
        TravelMatch match = new TravelMatch(
            request.getTripId(),
            request.getUserId(),
            calculateMatchScore(request)
        );
        
        TravelMatch savedMatch = travelMatchRepository.save(match);
        return convertToDto(savedMatch);
    }
    
    @Transactional(readOnly = true)
    public TravelMatchDto getMatchById(Long id) {
        TravelMatch match = travelMatchRepository.findById(id)
            .orElseThrow(() -> new MatchNotFoundException("Match not found with id: " + id));
        return convertToDto(match);
    }
    
    @Transactional(readOnly = true)
    public List<TravelMatchDto> getAllMatches() {
        return travelMatchRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TravelMatchDto> getMatchesByTripId(Long tripId) {
        return travelMatchRepository.findByTripId(tripId).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TravelMatchDto> getMatchesByUserId(Long userId) {
        return travelMatchRepository.findByUserId(userId).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TravelMatchDto> getMatchesByStatus(MatchStatus status) {
        return travelMatchRepository.findByStatus(status).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    public TravelMatchDto acceptMatch(Long matchId) {
        TravelMatch match = travelMatchRepository.findById(matchId)
            .orElseThrow(() -> new MatchNotFoundException("Match not found with id: " + matchId));
        
        match.setStatus(MatchStatus.ACCEPTED);
        match.setUpdatedAt(LocalDateTime.now());
        TravelMatch updatedMatch = travelMatchRepository.save(match);
        
        return convertToDto(updatedMatch);
    }
    
    public TravelMatchDto rejectMatch(Long matchId) {
        TravelMatch match = travelMatchRepository.findById(matchId)
            .orElseThrow(() -> new MatchNotFoundException("Match not found with id: " + matchId));
        
        match.setStatus(MatchStatus.REJECTED);
        match.setUpdatedAt(LocalDateTime.now());
        TravelMatch updatedMatch = travelMatchRepository.save(match);
        
        return convertToDto(updatedMatch);
    }
    
    public void deleteMatch(Long id) {
        if (!travelMatchRepository.existsById(id)) {
            throw new MatchNotFoundException("Match not found with id: " + id);
        }
        travelMatchRepository.deleteById(id);
    }
    
    private Double calculateMatchScore(MatchRequestDto request) {
        // Простая реализация для демонстрации
        // В реальном приложении здесь была бы сложная логика сопоставления
        return Math.random();
    }
    
    private TravelMatchDto convertToDto(TravelMatch match) {
        TravelMatchDto dto = new TravelMatchDto();
        dto.setId(match.getId());
        dto.setTripId(match.getTripId());
        dto.setUserId(match.getUserId());
        dto.setMatchScore(match.getMatchScore());
        dto.setStatus(match.getStatus());
        dto.setCreatedAt(match.getCreatedAt());
        dto.setUpdatedAt(match.getUpdatedAt());
        return dto;
    }
}
