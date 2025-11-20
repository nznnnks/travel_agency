package com.travelagency.tripservice.client;

import com.travelagency.tripservice.dto.ReviewSummaryDto;
import org.springframework.stereotype.Component;

@Component
public class ReviewServiceFallback implements ReviewServiceClient {

    @Override
    public ReviewSummaryDto getTripReviewSummary(Long tripId) {
        ReviewSummaryDto fallbackResponse = new ReviewSummaryDto();
        fallbackResponse.setTripId(tripId);
        fallbackResponse.setAverageRating(0.0);
        fallbackResponse.setTotalReviews(0L);
        fallbackResponse.setStatus("SERVICE_UNAVAILABLE");
        return fallbackResponse;
    }
}
