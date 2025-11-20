package com.travelagency.tripservice.repository;

import com.travelagency.tripservice.entity.Trip;
import com.travelagency.tripservice.entity.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    
    List<Trip> findByUserId(Long userId);
    
    List<Trip> findByStatus(TripStatus status);
    
    List<Trip> findByUserIdAndStatus(Long userId, TripStatus status);
    
    List<Trip> findByDestinationContainingIgnoreCase(String destination);
}
