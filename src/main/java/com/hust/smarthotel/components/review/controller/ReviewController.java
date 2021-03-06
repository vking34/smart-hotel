package com.hust.smarthotel.components.review.controller;

import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.domain_service.HotelService;
import com.hust.smarthotel.components.review.app_model.ReviewResponse;
import com.hust.smarthotel.components.review.domain_model.BasicReview;
import com.hust.smarthotel.components.review.domain_model.Review;
import com.hust.smarthotel.components.review.domain_model.Reviews;
import com.hust.smarthotel.components.review.domain_service.ReviewService;
import com.hust.smarthotel.generic.constant.UrlConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.hust.smarthotel.generic.response.ErrorResponses.REVIEW_HOTEL_NOT_FOUND;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(UrlConstant.API + "/hotels/{hotelId}/reviews")
public class ReviewController {

    private static final ResponseEntity<ReviewResponse> HOTEL_NOT_FOUND = new ResponseEntity<>(REVIEW_HOTEL_NOT_FOUND, HttpStatus.BAD_REQUEST);

    @Autowired
    private HotelService hotelService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    Reviews getReviewsOfHotel(@PathVariable String hotelId,
                              @RequestParam(value = "number", required = false) Integer numOfReview){
        return reviewService.findViewsOfHotel(hotelId, numOfReview);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    ResponseEntity<ReviewResponse> postReview(@PathVariable String hotelId,
                                              @Valid @RequestBody BasicReview basicReview){
        Hotel hotel = hotelService.findHotelById(hotelId);
        if (hotel == null)
            return HOTEL_NOT_FOUND;

        Review review = new Review(basicReview);

        reviewService.pushReview(hotelId, review);
        return new ResponseEntity<>(new ReviewResponse(true, null, null, review), HttpStatus.OK);
    }

}
