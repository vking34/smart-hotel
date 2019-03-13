package com.hust.smarthotel.components.hotel.domain_service;

import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import org.springframework.stereotype.Component;


@Component
public interface HotelAsyncTasks {
    void updateHotel(Hotel hotel);
}
