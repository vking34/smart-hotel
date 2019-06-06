package com.hust.smarthotel.components.booking.domain_service;

import com.hust.smarthotel.components.booking.domain_model.BookingRecord;
import com.hust.smarthotel.components.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class BookingAsyncTask {

    @Autowired
    private BookingRepository bookingRepository;

    @Async
    public void updateBookingStatus(BookingRecord bookingRecord){
        bookingRepository.save(bookingRecord);
    }

    @Async
    public void changeFetchedStatus(BookingRecord bookingRecord){
        bookingRecord.setIsFetched(true);
        bookingRepository.save(bookingRecord);
    }

}
