package com.travelagency.reviewservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Review content is required")
    @Column(nullable = false, length = 2000)
    private String content;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @Column(nullable = false)
    private Integer rating;
    
    @NotNull(message = "User ID is required")
    @Column(nullable = false)
    private Long userId;
    
    @NotNull(message = "Reviewable ID is required")
    @Column(nullable = false)
    private Long reviewableId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewType reviewType;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column
    private LocalDateTime updatedAt;
    
    // Constructors
    public Review() {}
    
    public Review(String content, Integer rating, Long userId, Long reviewableId, ReviewType reviewType) {
        this.content = content;
        this.rating = rating;
        this.userId = userId;
        this.reviewableId = reviewableId;
        this.reviewType = reviewType;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getReviewableId() {
        return reviewableId;
    }
    
    public void setReviewableId(Long reviewableId) {
        this.reviewableId = reviewableId;
    }
    
    public ReviewType getReviewType() {
        return reviewType;
    }
    
    public void setReviewType(ReviewType reviewType) {
        this.reviewType = reviewType;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
