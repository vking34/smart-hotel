package com.hust.smarthotel.components.booking.repository;

import com.hust.smarthotel.components.booking.domain_model.BookingRecord;
import com.hust.smarthotel.components.booking.domain_model.DetailBookingRecord;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends MongoRepository<BookingRecord, String>, BookingRepositoryCustom {

    public BookingRecord findBookingRecordById(String id);

//    @Query("{ _id : ?0 }")
//    public DetailBookingRecord findDetailBookingRecordById(ObjectId id);

    public Page<BookingRecord> findBookingRecordByUserId(String userid, Pageable pageable);
}
