package com.travelagency.reviewservice.dto;

public class ReviewSummaryDto {
    private Long tripId;
    private Double averageRating;
    private Long totalReviews;
    private String status;

    public ReviewSummaryDto() {}

    public ReviewSummaryDto(Long tripId, Double averageRating, Long totalReviews, String status) {
        this.tripId = tripId;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.status = status;
    }

    // Getters and Setters
    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Long getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Long totalReviews) {
        this.totalReviews = totalReviews;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
