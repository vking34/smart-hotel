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
    public void updateBookingRecord(BookingRecord bookingRecord){
        bookingRepository.save(bookingRecord);
    }
}
