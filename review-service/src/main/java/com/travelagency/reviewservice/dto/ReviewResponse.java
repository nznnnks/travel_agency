package com.travelagency.reviewservice.dto;

import com.travelagency.reviewservice.entity.ReviewType;
import java.time.LocalDateTime;

public class ReviewResponse {
    
    private Long id;
    private String content;
    private Integer rating;
    private Long userId;
    private Long reviewableId;
    private ReviewType reviewType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public ReviewResponse() {}
    
    public ReviewResponse(Long id, String content, Integer rating, Long userId, 
                        Long reviewableId, ReviewType reviewType, 
                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.userId = userId;
        this.reviewableId = reviewableId;
        this.reviewType = reviewType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
}
