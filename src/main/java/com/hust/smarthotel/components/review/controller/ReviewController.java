package com.hust.smarthotel.components.review.controller;

import com.hust.smarthotel.components.review.domain_model.Reviews;
import com.hust.smarthotel.components.review.domain_service.ReviewService;
import com.hust.smarthotel.generic.constant.UrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UrlConstants.API + "/hotels/{hotelId}/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    Reviews getViewsOfHotel(@PathVariable String hotelId){
        return reviewService.findViewsOfHotel(hotelId);
    }
}
