package com.travelagency.matchservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public class MatchRequestDto {
    
    @NotNull(message = "Trip ID is required")
    private Long tripId;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotBlank(message = "Destination is required")
    private String destination;
    
    @NotNull(message = "Departure date is required")
    private LocalDateTime departureDate;
    
    @NotNull(message = "Return date is required")
    private LocalDateTime returnDate;
    
    @Positive(message = "Max companions must be positive")
    private Integer maxCompanions;
    
    private String preferences;
    
    // Constructors
    public MatchRequestDto() {}
    
    public MatchRequestDto(Long tripId, Long userId, String destination, 
                          LocalDateTime departureDate, LocalDateTime returnDate) {
        this.tripId = tripId;
        this.userId = userId;
        this.destination = destination;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
    }
    
    // Getters and Setters
    public Long getTripId() {
        return tripId;
    }
    
    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public LocalDateTime getDepartureDate() {
        return departureDate;
    }
    
    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }
    
    public LocalDateTime getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }
    
    public Integer getMaxCompanions() {
        return maxCompanions;
    }
    
    public void setMaxCompanions(Integer maxCompanions) {
        this.maxCompanions = maxCompanions;
    }
    
    public String getPreferences() {
        return preferences;
    }
    
    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}
