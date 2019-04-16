package com.hust.smarthotel.components.booking.domain_service;

import com.hust.smarthotel.components.booking.app_model.BookingRequest;
import com.hust.smarthotel.components.booking.app_model.BookingResponse;
import com.hust.smarthotel.components.booking.domain_model.BookingRecord;
import com.hust.smarthotel.components.booking.repository.BookingRepository;
import com.hust.smarthotel.generic.util.PageRequestCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;


    public BookingResponse insert(BookingRequest bookingRequest){
        BookingRecord bookingRecord = new BookingRecord(bookingRequest);
        bookingRepository.save(bookingRecord);
        return new BookingResponse(true, null, null, bookingRecord);
    }

    public BookingRecord findBookingRecordById(String id){
        BookingRecord record = bookingRepository.findBookingRecordById(id);
        if (record == null)
            record = new BookingRecord();
        return record;
    }

    public BookingRecord changeState(BookingRecord bookingRecord, String status){
        bookingRecord.setStatus(status);
        bookingRecord.setUpdatedTime(LocalDateTime.now());
        return bookingRepository.save(bookingRecord);
    }

    public Page<BookingRecord> getBookingRecords(){
        return bookingRepository.findAll(PageRequestCreator.getSimplePageRequest(0,10));
    }

    public Page<BookingRecord> findBookingRecordsByUserId(String userId, Integer page, Integer pageSize){
        return bookingRepository.findBookingRecordByUserId(userId, PageRequestCreator.getSimplePageRequest(page, pageSize));
    }

}
