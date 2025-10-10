package com.travelagency.tripservice.service;

import com.travelagency.tripservice.dto.CreateTripRequest;
import com.travelagency.tripservice.dto.TripResponse;
import com.travelagency.tripservice.entity.Trip;
import com.travelagency.tripservice.entity.TripStatus;
import com.travelagency.tripservice.exception.TripNotFoundException;
import com.travelagency.tripservice.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TripService {
    
    @Autowired
    private TripRepository tripRepository;
    
    public TripResponse createTrip(CreateTripRequest request) {
        Trip trip = new Trip(
            request.getDestination(),
            request.getDescription(),
            request.getStartDate(),
            request.getEndDate(),
            request.getPrice(),
            request.getUserId()
        );
        
        Trip savedTrip = tripRepository.save(trip);
        return convertToResponse(savedTrip);
    }
    
    @Transactional(readOnly = true)
    public TripResponse getTripById(Long id) {
        Trip trip = tripRepository.findById(id)
            .orElseThrow(() -> new TripNotFoundException("Trip not found with id: " + id));
        return convertToResponse(trip);
    }
    
    @Transactional(readOnly = true)
    public List<TripResponse> getAllTrips() {
        return tripRepository.findAll().stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TripResponse> getTripsByUserId(Long userId) {
        return tripRepository.findByUserId(userId).stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TripResponse> getTripsByStatus(TripStatus status) {
        return tripRepository.findByStatus(status).stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TripResponse> searchTripsByDestination(String destination) {
        return tripRepository.findByDestinationContainingIgnoreCase(destination).stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    public TripResponse updateTripStatus(Long id, TripStatus status) {
        Trip trip = tripRepository.findById(id)
            .orElseThrow(() -> new TripNotFoundException("Trip not found with id: " + id));
        
        trip.setStatus(status);
        Trip updatedTrip = tripRepository.save(trip);
        return convertToResponse(updatedTrip);
    }
    
    public void deleteTrip(Long id) {
        if (!tripRepository.existsById(id)) {
            throw new TripNotFoundException("Trip not found with id: " + id);
        }
        tripRepository.deleteById(id);
    }
    
    private TripResponse convertToResponse(Trip trip) {
        return new TripResponse(
            trip.getId(),
            trip.getDestination(),
            trip.getDescription(),
            trip.getStartDate(),
            trip.getEndDate(),
            trip.getPrice(),
            trip.getUserId(),
            trip.getStatus()
        );
    }
}
