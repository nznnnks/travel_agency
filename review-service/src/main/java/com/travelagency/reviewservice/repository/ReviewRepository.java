package com.travelagency.reviewservice.repository;

import com.travelagency.reviewservice.entity.Review;
import com.travelagency.reviewservice.entity.ReviewType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByUserId(Long userId);
    
    List<Review> findByReviewableId(Long reviewableId);
    
    List<Review> findByReviewType(ReviewType reviewType);
    
    List<Review> findByUserIdAndReviewType(Long userId, ReviewType reviewType);
    
    List<Review> findByReviewableIdAndReviewType(Long reviewableId, ReviewType reviewType);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.reviewableId = :reviewableId AND r.reviewType = :reviewType")
    Double findAverageRatingByReviewableIdAndType(@Param("reviewableId") Long reviewableId, 
                                                 @Param("reviewType") ReviewType reviewType);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.reviewableId = :reviewableId AND r.reviewType = :reviewType")
    Long countReviewsByReviewableIdAndType(@Param("reviewableId") Long reviewableId, 
                                          @Param("reviewType") ReviewType reviewType);
}
