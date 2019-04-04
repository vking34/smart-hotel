package com.hust.smarthotel.components.review.app_model;


import com.hust.smarthotel.components.review.domain_model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Boolean status;
    private String message;
    private Integer code;
    private Review review;
}
