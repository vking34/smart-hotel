package com.hust.smarthotel.components.review.repository;


import com.hust.smarthotel.components.review.domain_model.Reviews;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ReviewRepository extends MongoRepository<Reviews, String> {

    @Query(value = "{ hotel_id : ?0 }", fields = "{ reviews : { $slice: ?1 } }")
    public Reviews findReviewsByHotelId(String hotelId, Integer numOfReview);

    public Reviews findReviewsByHotelId(String hotelId);
}
