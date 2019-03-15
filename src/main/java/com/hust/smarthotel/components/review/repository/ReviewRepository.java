package com.hust.smarthotel.components.review.repository;


import com.hust.smarthotel.components.review.domain_model.Reviews;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReviewRepository extends MongoRepository<Reviews, String> {

    public Reviews findReviewsByHotelId(String hotelId);
}
