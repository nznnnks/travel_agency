package com.travelagency.tripservice.dto;

import com.travelagency.tripservice.entity.TripStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TripResponse {
    
    private Long id;
    private String destination;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal price;
    private Long userId;
    private TripStatus status;
    
    // Дополнительные поля для фильтрации
    private String tripType; // пеший, велосипед, водный
    private String difficulty; // лёгкая, средняя, сложная
    private String region; // горы, лес, побережье
    private Integer duration; // длительность в днях
    
    // Constructors
    public TripResponse() {}
    
    public TripResponse(Long id, String destination, String description, LocalDate startDate, 
                       LocalDate endDate, BigDecimal price, Long userId, TripStatus status,
                       String tripType, String difficulty, String region, Integer duration) {
        this.id = id;
        this.destination = destination;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.userId = userId;
        this.status = status;
        this.tripType = tripType;
        this.difficulty = difficulty;
        this.region = region;
        this.duration = duration;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public TripStatus getStatus() {
        return status;
    }
    
    public void setStatus(TripStatus status) {
        this.status = status;
    }
    
    // Getters and Setters for new fields
    public String getTripType() {
        return tripType;
    }
    
    public void setTripType(String tripType) {
        this.tripType = tripType;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public String getRegion() {
        return region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
