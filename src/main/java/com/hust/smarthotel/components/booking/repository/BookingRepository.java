package com.hust.smarthotel.components.booking.repository;

import com.hust.smarthotel.components.booking.domain_model.BookingRecord;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<BookingRecord, String>, BookingRepositoryCustom {

    public BookingRecord findBookingRecordById(String id);

    public Page<BookingRecord> findBookingRecordsByHotelRef(ObjectId hotelRef, Pageable pageable);

    public Page<BookingRecord> findBookingRecordsByHotelRefIsIn(List<ObjectId> hotelList, Pageable pageable);

    @Query("{ hotel_ref : ?0 , hotel_fetched : false , status : { $eq : 'NEW_CREATED' } }")
    public Page<BookingRecord> findBookingRecordsNotFetchedByHotel(ObjectId hotelId, Pageable pageable);

    public Page<BookingRecord> findBookingRecordsByHotelRefAndStatus(ObjectId hotelId,String status, Pageable pageable);
}
