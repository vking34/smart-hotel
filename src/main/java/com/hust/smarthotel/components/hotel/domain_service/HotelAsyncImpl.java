package com.hust.smarthotel.components.hotel.domain_service;

import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class HotelAsyncImpl implements HotelAsyncTasks {

    @Autowired
    private HotelRepository hotelRepository;

    @Async
    @Override
    public void updateHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }
}
