package com.hust.smarthotel.components.hotel.domain_service;

import com.hust.smarthotel.generic.util.PageRequestCreator;
import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public Page<Hotel> findAll(Integer page, Integer pageSize){

        return hotelRepository.findAll(PageRequestCreator.getSimplePageRequest(page, pageSize));
    }
}
