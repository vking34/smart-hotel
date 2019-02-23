package com.hust.smarthotel.components.hotel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends MongoRepository<Hotel, String> {
    public Page<Hotel> findAll(Pageable pageable);
}
