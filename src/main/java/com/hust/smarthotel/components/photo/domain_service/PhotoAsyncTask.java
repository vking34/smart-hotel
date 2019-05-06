package com.hust.smarthotel.components.photo.domain_service;

import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PhotoAsyncTask {

    @Autowired
    private HotelRepository hotelRepository;

    @Async
    public void changeLogoHotel(Hotel hotel, String logoUrl){
        hotel.setLogo(logoUrl);
        hotelRepository.save(hotel);
    }
}
