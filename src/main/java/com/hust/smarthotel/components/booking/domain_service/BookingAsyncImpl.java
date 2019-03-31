package com.hust.smarthotel.components.booking.domain_service;

import com.hust.smarthotel.components.booking.domain_model.BookingRecord;
import com.hust.smarthotel.components.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class BookingAsyncImpl implements BookingAsyncTasks {

    @Autowired
    private BookingRepository bookingRepository;

//    @Async
    @Override
    public void insert(BookingRecord record) {
        bookingRepository.save(record);
    }
}
