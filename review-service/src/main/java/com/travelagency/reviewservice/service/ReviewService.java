package com.travelagency.reviewservice.service;

import com.travelagency.reviewservice.dto.CreateReviewRequest;
import com.travelagency.reviewservice.dto.ReviewResponse;
import com.travelagency.reviewservice.entity.Review;
import com.travelagency.reviewservice.entity.ReviewType;
import com.travelagency.reviewservice.exception.ReviewNotFoundException;
import com.travelagency.reviewservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    public ReviewResponse createReview(CreateReviewRequest request) {
        Review review = new Review(
            request.getContent(),
            request.getRating(),
            request.getUserId(),
            request.getReviewableId(),
            request.getReviewType()
        );
        
        Review savedReview = reviewRepository.save(review);
        return convertToResponse(savedReview);
    }
    
    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + id));
        return convertToResponse(review);
    }
    
    @Transactional(readOnly = true)
    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll().stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId).stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByReviewableId(Long reviewableId) {
        return reviewRepository.findByReviewableId(reviewableId).stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByType(ReviewType reviewType) {
        return reviewRepository.findByReviewType(reviewType).stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByReviewableIdAndType(Long reviewableId, ReviewType reviewType) {
        return reviewRepository.findByReviewableIdAndReviewType(reviewableId, reviewType).stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Double getAverageRating(Long reviewableId, ReviewType reviewType) {
        return reviewRepository.findAverageRatingByReviewableIdAndType(reviewableId, reviewType);
    }
    
    @Transactional(readOnly = true)
    public Long getReviewCount(Long reviewableId, ReviewType reviewType) {
        return reviewRepository.countReviewsByReviewableIdAndType(reviewableId, reviewType);
    }
    
    public ReviewResponse updateReview(Long id, CreateReviewRequest request) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + id));
        
        review.setContent(request.getContent());
        review.setRating(request.getRating());
        
        Review updatedReview = reviewRepository.save(review);
        return convertToResponse(updatedReview);
    }
    
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ReviewNotFoundException("Review not found with id: " + id);
        }
        reviewRepository.deleteById(id);
    }
    
    private ReviewResponse convertToResponse(Review review) {
        return new ReviewResponse(
            review.getId(),
            review.getContent(),
            review.getRating(),
            review.getUserId(),
            review.getReviewableId(),
            review.getReviewType(),
            review.getCreatedAt(),
            review.getUpdatedAt()
        );
    }
}
