package com.hust.smarthotel.components.review.domain_service;


import com.hust.smarthotel.components.review.domain_model.Review;
import com.hust.smarthotel.components.review.domain_model.Reviews;
import com.hust.smarthotel.components.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Reviews findViewsOfHotel(String hotelId, Integer numOfReview){
        if (numOfReview == null)
            numOfReview = 5;
        System.out.println(numOfReview);
        return reviewRepository.findReviewsByHotelId(hotelId, numOfReview);
    }

    @Async
    public void saveReview(String hotelId, Review review){
        Reviews reviews = reviewRepository.findReviewsByHotelId(hotelId);
        reviews.getReviews().add(0,review);
        reviewRepository.save(reviews);
    }

}
