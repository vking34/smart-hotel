package com.hust.smarthotel.components.booking.domain_service;


import com.hust.smarthotel.components.booking.domain_model.BookingRecord;
import org.springframework.stereotype.Component;

@Component
public interface BookingAsyncTasks {
    public void insert(BookingRecord record);
}
