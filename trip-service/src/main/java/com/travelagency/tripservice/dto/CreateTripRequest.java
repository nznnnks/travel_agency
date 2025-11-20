package com.travelagency.tripservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateTripRequest {
    
    @NotBlank(message = "Destination is required")
    private String destination;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    // Дополнительные поля для фильтрации
    @NotBlank(message = "Trip type is required")
    private String tripType; // пеший, велосипед, водный
    
    @NotBlank(message = "Difficulty is required")
    private String difficulty; // лёгкая, средняя, сложная
    
    @NotBlank(message = "Region is required")
    private String region; // горы, лес, побережье
    
    @Positive(message = "Duration must be positive")
    private Integer duration; // длительность в днях
    
    // Constructors
    public CreateTripRequest() {}
    
    public CreateTripRequest(String destination, String description, LocalDate startDate, 
                           LocalDate endDate, BigDecimal price, Long userId, String tripType,
                           String difficulty, String region, Integer duration) {
        this.destination = destination;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.userId = userId;
        this.tripType = tripType;
        this.difficulty = difficulty;
        this.region = region;
        this.duration = duration;
    }
    
    // Getters and Setters
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
