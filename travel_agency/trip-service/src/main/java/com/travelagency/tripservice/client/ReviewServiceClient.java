package com.travelagency.tripservice.client;

import com.travelagency.tripservice.dto.ReviewSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "review-service", fallback = ReviewServiceFallback.class)
public interface ReviewServiceClient {

    @GetMapping("/api/reviews/trip/{tripId}/summary")
    ReviewSummaryDto getTripReviewSummary(@PathVariable Long tripId);
}
