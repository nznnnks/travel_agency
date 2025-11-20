package com.travelagency.tripservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "trips")
public class Trip {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Destination is required")
    @Column(nullable = false)
    private String destination;
    
    @NotBlank(message = "Description is required")
    @Column(nullable = false, length = 1000)
    private String description;
    
    @NotNull(message = "Start date is required")
    @Column(nullable = false)
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    @Column(nullable = false)
    private LocalDate endDate;
    
    @Positive(message = "Price must be positive")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @NotNull(message = "User ID is required")
    @Column(nullable = false)
    private Long userId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus status = TripStatus.PLANNED;
    
    // Дополнительные поля для фильтрации
    @NotBlank(message = "Trip type is required")
    @Column(nullable = false)
    private String tripType; // пеший, велосипед, водный
    
    @NotBlank(message = "Difficulty is required")
    @Column(nullable = false)
    private String difficulty; // лёгкая, средняя, сложная
    
    @NotBlank(message = "Region is required")
    @Column(nullable = false)
    private String region; // горы, лес, побережье
    
    @Positive(message = "Duration must be positive")
    @Column(nullable = false)
    private Integer duration; // длительность в днях
    
    // Constructors
    public Trip() {}
    
    public Trip(String destination, String description, LocalDate startDate, 
                LocalDate endDate, BigDecimal price, Long userId) {
        this.destination = destination;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.userId = userId;
    }
    
    public Trip(String destination, String description, LocalDate startDate, 
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
