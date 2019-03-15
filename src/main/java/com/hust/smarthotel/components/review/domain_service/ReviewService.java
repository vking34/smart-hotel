package com.hust.smarthotel.components.review.domain_service;


import com.hust.smarthotel.components.review.domain_model.Reviews;
import com.hust.smarthotel.components.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Reviews findViewsOfHotel(String hotelId){
        return reviewRepository.findReviewsByHotelId(hotelId);
    }

}
