package com.travelagency.tripservice.service;

import com.travelagency.tripservice.client.ReviewServiceClient;
import com.travelagency.tripservice.dto.ReviewSummaryDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class TripReviewService {

    @Autowired
    private ReviewServiceClient reviewServiceClient;

    @CircuitBreaker(name = "review-service", fallbackMethod = "getTripReviewSummaryAsyncFallback")
    @Retry(name = "review-service")
    @TimeLimiter(name = "review-service")
    public CompletableFuture<ReviewSummaryDto> getTripReviewSummaryAsync(Long tripId) {
        return CompletableFuture.supplyAsync(() -> {
            return reviewServiceClient.getTripReviewSummary(tripId);
        });
    }

    @CircuitBreaker(name = "review-service", fallbackMethod = "getTripReviewSummaryFallback")
    @Retry(name = "review-service")
    public ReviewSummaryDto getTripReviewSummary(Long tripId) {
        return reviewServiceClient.getTripReviewSummary(tripId);
    }

    // Synchronous fallback method
    public ReviewSummaryDto getTripReviewSummaryFallback(Long tripId, Exception ex) {
        ReviewSummaryDto fallbackResponse = new ReviewSummaryDto();
        fallbackResponse.setTripId(tripId);
        fallbackResponse.setAverageRating(0.0);
        fallbackResponse.setTotalReviews(0L);
        fallbackResponse.setStatus("SERVICE_UNAVAILABLE");
        return fallbackResponse;
    }

    // Asynchronous fallback method
    public CompletableFuture<ReviewSummaryDto> getTripReviewSummaryAsyncFallback(Long tripId, Exception ex) {
        ReviewSummaryDto fallbackResponse = new ReviewSummaryDto();
        fallbackResponse.setTripId(tripId);
        fallbackResponse.setAverageRating(0.0);
        fallbackResponse.setTotalReviews(0L);
        fallbackResponse.setStatus("SERVICE_UNAVAILABLE");
        return CompletableFuture.completedFuture(fallbackResponse);
    }
}
