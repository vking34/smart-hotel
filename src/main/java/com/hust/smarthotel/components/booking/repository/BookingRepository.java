package com.hust.smarthotel.components.booking.repository;

import com.hust.smarthotel.components.booking.domain_model.BookingRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends MongoRepository<BookingRecord, String> {

    public BookingRecord findBookingRecordById(String id);
}
