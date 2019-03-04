package com.hust.smarthotel.components.hotel.domain_service;

import com.hust.smarthotel.components.hotel.app_model.BasicHotel;
import com.hust.smarthotel.generic.util.PageRequestCreator;
import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public Page<Hotel> findAllSortedByPointDesc(Integer page, Integer pageSize){

        return hotelRepository.findAll(PageRequestCreator.getDescPageRequest(page, pageSize, "point"));
    }

    public Page<Hotel> findHotelsByName(Integer page, Integer pageSize, String name){
        return hotelRepository.findHotelsByName(name, PageRequestCreator.getSimplePageRequest(page, pageSize));
    }

    public Hotel createHotel(BasicHotel basicHotel){
        Hotel hotel = new Hotel(basicHotel);
        hotelRepository.insert(hotel);
        return hotel;
    }

    public Hotel updateHotel(String id, BasicHotel basicHotel){
        Hotel hotel = hotelRepository.findHotelById(id);
        if (hotel == null)
            return null;
        return hotel;
    }

    public Hotel deleteHotel(String id){
        return hotelRepository.deleteHotelById(id);
    }

    public Page<Hotel> findHotelsAround(Double lng, Double lat, Integer radius){
        return hotelRepository.findHotelsAround(lng, lat, radius, PageRequestCreator.getDescPageRequest(0, 10, "point"));
    }
}
