package com.hust.smarthotel.components.review.domain_service;


import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.domain_service.HotelAsyncTasks;
import com.hust.smarthotel.components.hotel.domain_service.HotelService;
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

    @Autowired
    private HotelService hotelService;

    @Autowired
    private ReviewAsyncTask reviewAsyncTask;

    @Autowired
    private HotelAsyncTasks hotelAsyncTasks;

    public Reviews findViewsOfHotel(String hotelId, Integer numOfReview){
        if (numOfReview == null)
            numOfReview = 5;

        Reviews reviews = reviewRepository.findReviewsByHotelId(hotelId, numOfReview);
        if (reviews == null){
            reviews = new Reviews(hotelId);
            reviewAsyncTask.saveReview(reviews);
        }
        return reviews;
    }

    @Async
    public void pushReview(String hotelId, Review review){
        Reviews reviews = reviewRepository.findReviewsByHotelId(hotelId);

        if (reviews == null) {
            reviews = new Reviews(hotelId);
            reviewRepository.save(reviews);
        }

        reviews.getReviews().add(0,review);
        reviewRepository.save(reviews);

        Hotel hotel = hotelService.findHotelById(reviews.getHotelId());

        Double point = hotel.getPoint();
        Integer ratings = hotel.getRatings();

        if (ratings == 0){
            ratings = 1;
            point = review.getRatingPoint()/1d;
        }else {
            // 1d* to cast into Double
            point = 1d*point*ratings/(ratings+1) + review.getRatingPoint()*1d/(ratings+1);
            ratings++;
        }

        hotel.setPoint(point);
        hotel.setRatings(ratings);
        hotelAsyncTasks.updateHotel(hotel);
    }



}
