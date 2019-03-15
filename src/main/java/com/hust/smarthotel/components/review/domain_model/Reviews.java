package com.hust.smarthotel.components.review.domain_model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;


@Data
@NoArgsConstructor
@Document(collection = "Review")
public class Reviews {

    @Id
    private String id;

    @Field("hotel_id")
    private String hotelId;

    @Field("reviews")
    List<Review> reviews;
}
