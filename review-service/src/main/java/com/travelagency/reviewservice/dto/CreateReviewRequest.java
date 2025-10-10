package com.travelagency.reviewservice.dto;

import com.travelagency.reviewservice.entity.ReviewType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateReviewRequest {
    
    @NotBlank(message = "Review content is required")
    private String content;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Reviewable ID is required")
    private Long reviewableId;
    
    @NotNull(message = "Review type is required")
    private ReviewType reviewType;
    
    // Constructors
    public CreateReviewRequest() {}
    
    public CreateReviewRequest(String content, Integer rating, Long userId, Long reviewableId, ReviewType reviewType) {
        this.content = content;
        this.rating = rating;
        this.userId = userId;
        this.reviewableId = reviewableId;
        this.reviewType = reviewType;
    }
    
    // Getters and Setters
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
}
