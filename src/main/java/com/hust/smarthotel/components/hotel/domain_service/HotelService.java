package com.hust.smarthotel.components.hotel.domain_service;

import com.hust.smarthotel.components.hotel.app_model.BasicHotel;
import com.hust.smarthotel.components.hotel.app_model.HotelResponse;
import com.hust.smarthotel.components.hotel.app_model.HotelStatus;
import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.repository.HotelRepository;
import com.hust.smarthotel.components.mananging.domain_model.Managing;
import com.hust.smarthotel.components.mananging.domain_service.ManagingService;
import com.hust.smarthotel.generic.util.PageRequestCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hust.smarthotel.generic.response.ErrorResponses.HOTEL_EXISTS;
import static com.hust.smarthotel.generic.response.ErrorResponses.HOTEL_INVALID_COORDINATES;
import static com.hust.smarthotel.generic.response.ErrorResponses.HOTEL_OUT_OF_MANAGING;
import static com.hust.smarthotel.generic.response.ErrorResponses.HOTEL_NOT_FOUND;
import static com.hust.smarthotel.generic.response.ErrorResponses.HOTEL_NOT_MANAGED_BY_THIS_MANAGER;
import static com.hust.smarthotel.generic.constant.RoleConstants.MANAGER;


@Service
@CacheConfig(cacheNames = {"hotels"})
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ManagingService managingService;

    @Autowired
    private HotelAsyncTasks asyncTasks;

    @Cacheable(value = "desc_hotels_cache")
    public Page<Hotel> findAllSortedByPointDesc(Integer page, Integer pageSize){
        return hotelRepository.findAll(PageRequestCreator.getDescPageRequest(page, pageSize, "point"));
    }

    public Page<Hotel> findHotelsByName(Integer page, Integer pageSize, String name){
        return hotelRepository.findHotelsByName(name, PageRequestCreator.getSimplePageRequest(page, pageSize));
    }

    public HotelResponse createHotel(BasicHotel basicHotel, String role, String managerId){

        if (hotelRepository.findHotelByPhoneNumber(basicHotel.getPhoneNumber()) != null)
            return HOTEL_EXISTS;

        if (role.equals(MANAGER)){
            List<Managing> managingList = managingService.findManagingByManagerId(managerId);
            if (managingList != null){
                System.out.println(managingList.size());
                if (managingList.size() >= 3){
                    return HOTEL_OUT_OF_MANAGING;
                }
            }
        }

        Hotel hotel = new Hotel(basicHotel);

        try {
            hotelRepository.insert(hotel);
        }
        catch ( Exception e){
            return HOTEL_INVALID_COORDINATES;
        }

        if (role.equals(MANAGER))
            asyncTasks.insertManaging(managerId, hotel.getId());

        return new HotelResponse(true, null, null, hotel);
    }

    @CachePut(value = "desc_hotels_cache")
    public HotelResponse updateHotel(String hotelId, BasicHotel basicHotel, String role, String managerId){

        Hotel hotel = hotelRepository.findHotelById(hotelId);
        if (hotel == null)
            return HOTEL_NOT_FOUND;

        if (role.equals(MANAGER)){
            Managing managing = managingService.findManaging(managerId, hotelId);
            if (managing == null){
                return HOTEL_NOT_MANAGED_BY_THIS_MANAGER;
            }
        }

        hotel.setName(basicHotel.getName());
        hotel.setAddress(basicHotel.getAddress());
        hotel.setDescription(basicHotel.getDescription());
        hotel.setLocation(basicHotel.getLocation());
        hotel.setFacilities(basicHotel.getFacilities());
        if (basicHotel.getStatus() == null)
            hotel.setStatus(true);
        else
            hotel.setStatus(basicHotel.getStatus());

        asyncTasks.updateHotel(hotel);
        return new HotelResponse(true, null, null, hotel);
    }

    @CachePut(value = "desc_hotels_cache")
    public HotelResponse deleteHotel(String hotelId, String role, String managerId){
        Hotel hotel = hotelRepository.findHotelById(hotelId);
        if (hotel == null)
            return HOTEL_NOT_FOUND;

        Managing managing = null;
        if (role.equals(MANAGER)){
            managing = managingService.findManaging(managerId, hotelId);
            if (managing == null){
                return HOTEL_NOT_MANAGED_BY_THIS_MANAGER;
            }
        }

        asyncTasks.deleteHotel(hotelId, managing);

        return new HotelResponse(true, null, null, hotel);
    }

    public Page<Hotel> findHotelsAround(Double lng, Double lat, Long radius){
        return hotelRepository.findHotelsAround(lng, lat, radius, PageRequestCreator.getDescPageRequest(0, 100, "point"));
    }

    public Hotel findHotelById(String hotelId){
        return hotelRepository.findHotelById(hotelId);
    }

    @CachePut
    public HotelResponse changeHotelStatus(String hotelId, HotelStatus status, String role, String managerId) {
        Hotel hotel = hotelRepository.findHotelById(hotelId);
        if (hotel == null)
            return HOTEL_NOT_FOUND;

        if (role.equals(MANAGER)){
            Managing managing = managingService.findManaging(managerId, hotelId);
            if (managing == null){
                return HOTEL_NOT_MANAGED_BY_THIS_MANAGER;
            }
        }

        hotel.setStatus(status.getStatus());
        asyncTasks.updateHotel(hotel);
        return new HotelResponse(true, null, null, hotel);
    }
}
