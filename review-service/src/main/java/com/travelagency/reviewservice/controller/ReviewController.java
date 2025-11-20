package com.travelagency.reviewservice.controller;

import com.travelagency.reviewservice.dto.CreateReviewRequest;
import com.travelagency.reviewservice.dto.ReviewResponse;
import com.travelagency.reviewservice.dto.ReviewSummaryDto;
import com.travelagency.reviewservice.entity.ReviewType;
import com.travelagency.reviewservice.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody CreateReviewRequest request) {
        ReviewResponse response = reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable Long id) {
        ReviewResponse response = reviewService.getReviewById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        List<ReviewResponse> responses = reviewService.getAllReviews();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUserId(@PathVariable Long userId) {
        List<ReviewResponse> responses = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/reviewable/{reviewableId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByReviewableId(@PathVariable Long reviewableId) {
        List<ReviewResponse> responses = reviewService.getReviewsByReviewableId(reviewableId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/type/{reviewType}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByType(@PathVariable ReviewType reviewType) {
        List<ReviewResponse> responses = reviewService.getReviewsByType(reviewType);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/reviewable/{reviewableId}/type/{reviewType}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByReviewableIdAndType(
            @PathVariable Long reviewableId, @PathVariable ReviewType reviewType) {
        List<ReviewResponse> responses = reviewService.getReviewsByReviewableIdAndType(reviewableId, reviewType);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/reviewable/{reviewableId}/type/{reviewType}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long reviewableId, @PathVariable ReviewType reviewType) {
        Double averageRating = reviewService.getAverageRating(reviewableId, reviewType);
        return ResponseEntity.ok(averageRating != null ? averageRating : 0.0);
    }
    
    @GetMapping("/reviewable/{reviewableId}/type/{reviewType}/count")
    public ResponseEntity<Long> getReviewCount(@PathVariable Long reviewableId, @PathVariable ReviewType reviewType) {
        Long count = reviewService.getReviewCount(reviewableId, reviewType);
        return ResponseEntity.ok(count);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable Long id, @Valid @RequestBody CreateReviewRequest request) {
        ReviewResponse response = reviewService.updateReview(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
    
    // Circuit Breaker Demo Endpoint
    @GetMapping("/trip/{tripId}/summary")
    public ResponseEntity<ReviewSummaryDto> getTripReviewSummary(@PathVariable Long tripId) {
        Double averageRating = reviewService.getAverageRating(tripId, ReviewType.TRIP);
        Long totalReviews = reviewService.getReviewCount(tripId, ReviewType.TRIP);
        
        ReviewSummaryDto summary = new ReviewSummaryDto();
        summary.setTripId(tripId);
        summary.setAverageRating(averageRating != null ? averageRating : 0.0);
        summary.setTotalReviews(totalReviews);
        summary.setStatus("AVAILABLE");
        
        return ResponseEntity.ok(summary);
    }
}
