package com.hust.smarthotel.components.hotel.domain_service;

import com.hust.smarthotel.components.hotel.app_model.BasicHotel;
import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface HotelService {
    public Page<Hotel> findAllSortedByPointDesc(Integer page, Integer pageSize);
    public Page<Hotel> findHotelsByName(Integer page, Integer pageSize, String name);
    public Hotel createHotel(BasicHotel basicHotel);
    public Hotel updateHotel(String id, BasicHotel basicHotel);
    public Hotel deleteHotel(String id);
    public Page<Hotel> findHotelsAround(Double lng, Double lat, Integer radius);
    public Hotel findHotelById(String hotelId);
}
