package com.hust.smarthotel.components.hotel.domain_service;

import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.repository.HotelRepository;
import com.hust.smarthotel.components.mananging.domain_model.Managing;
import com.hust.smarthotel.components.mananging.repository.ManagingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public class HotelAsyncTasks {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ManagingRepository managingRepository;

    @Async
    public void updateHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    @Async
    public void insertManaging(String managerId, String hotelId){
        Managing managing = new Managing(managerId, hotelId);
        managingRepository.save(managing);
    }

    @Async
    public void deleteHotel(String hotelId, Managing managing){
        hotelRepository.deleteHotelById(hotelId);
        if (managing != null)
            managingRepository.delete(managing);
    }


}
