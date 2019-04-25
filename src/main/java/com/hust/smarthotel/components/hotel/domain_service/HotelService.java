package com.hust.smarthotel.components.hotel.domain_service;

import com.hust.smarthotel.components.hotel.app_model.BasicHotel;
import com.hust.smarthotel.components.hotel.app_model.HotelResponse;
import com.hust.smarthotel.components.hotel.app_model.HotelStatus;
import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.repository.HotelRepository;
import com.hust.smarthotel.generic.util.PageRequestCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static com.hust.smarthotel.generic.response.ErrorResponses.HOTEL_EXISTS;
import static com.hust.smarthotel.generic.response.ErrorResponses.HOTEL_INVALID_COORDINATES;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelAsyncTasks asyncTasks;

    public Page<Hotel> findAllSortedByPointDesc(Integer page, Integer pageSize){

        return hotelRepository.findAll(PageRequestCreator.getDescPageRequest(page, pageSize, "point"));
    }

    public Page<Hotel> findHotelsByName(Integer page, Integer pageSize, String name){
        return hotelRepository.findHotelsByName(name, PageRequestCreator.getSimplePageRequest(page, pageSize));
    }

    public HotelResponse createHotel(BasicHotel basicHotel){

        if (hotelRepository.findHotelByPhoneNumber(basicHotel.getPhoneNumber()) != null)
            return HOTEL_EXISTS;

        Hotel hotel = new Hotel(basicHotel);

        try {
            hotelRepository.insert(hotel);
        }
        catch ( Exception e){
            return HOTEL_INVALID_COORDINATES;
        }

        return new HotelResponse(true, null, null, hotel);
    }

    public Hotel updateHotel(String id, BasicHotel basicHotel){
        Hotel hotel = hotelRepository.findHotelById(id);
        if (hotel == null)
            return null;
        hotel.setName(basicHotel.getName());
        hotel.setAddress(basicHotel.getAddress());
        hotel.setDescription(basicHotel.getDescription());
        hotel.setLocation(basicHotel.getLocation());
        hotel.setFacilities(basicHotel.getFacilities());
        hotel.setStatus(basicHotel.getStatus());
        asyncTasks.updateHotel(hotel);
        return hotel;
    }

    public Hotel deleteHotel(String id){
        return hotelRepository.deleteHotelById(id);
    }

    public Page<Hotel> findHotelsAround(Double lng, Double lat, Long radius){
        return hotelRepository.findHotelsAround(lng, lat, radius, PageRequestCreator.getDescPageRequest(0, 100, "point"));
    }

    public Hotel findHotelById(String hotelId){
        return hotelRepository.findHotelById(hotelId);
    }

    public Hotel changeHotelStatus(String hotelId, HotelStatus status) {
        Hotel hotel = hotelRepository.findHotelById(hotelId);
        if (hotel == null) return null;

        hotel.setStatus(status.getStatus());
        asyncTasks.updateHotel(hotel);
        return hotel;
    }
}
