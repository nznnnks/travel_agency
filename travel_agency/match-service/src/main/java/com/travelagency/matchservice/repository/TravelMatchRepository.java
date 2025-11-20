package com.travelagency.matchservice.repository;

import com.travelagency.matchservice.entity.TravelMatch;
import com.travelagency.matchservice.entity.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelMatchRepository extends JpaRepository<TravelMatch, Long> {
    
    List<TravelMatch> findByTripId(Long tripId);
    
    List<TravelMatch> findByUserId(Long userId);
    
    List<TravelMatch> findByTripIdAndStatus(Long tripId, MatchStatus status);
    
    List<TravelMatch> findByUserIdAndStatus(Long userId, MatchStatus status);
    
    List<TravelMatch> findByStatus(MatchStatus status);
}
