package com.hust.smarthotel.components.review.domain_service;

import com.hust.smarthotel.components.review.domain_model.Reviews;
import com.hust.smarthotel.components.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ReviewAsyncTask {

    @Autowired
    private ReviewRepository reviewRepository;

    @Async
    public void saveReview(Reviews reviews){
        reviewRepository.save(reviews);
    }
}
