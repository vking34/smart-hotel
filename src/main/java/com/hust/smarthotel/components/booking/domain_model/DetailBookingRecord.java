package com.hust.smarthotel.components.booking.domain_model;

import com.hust.smarthotel.components.booking.app_model.BookingRequest;
import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailBookingRecord extends BookingRecord {


    private Hotel hotel;

    public DetailBookingRecord(BookingRequest bookingRequest){
        super(bookingRequest);
    }
}
