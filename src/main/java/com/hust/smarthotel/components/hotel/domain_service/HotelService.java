package com.hust.smarthotel.components.hotel.domain_service;

import com.hust.smarthotel.components.hotel.app_model.BasicHotel;
import com.hust.smarthotel.components.hotel.app_model.Facility;
import com.hust.smarthotel.components.hotel.app_model.HotelResponse;
import com.hust.smarthotel.components.hotel.app_model.HotelStatus;
import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.repository.HotelRepository;
import com.hust.smarthotel.components.mananging.domain_model.Managing;
import com.hust.smarthotel.components.mananging.domain_service.ManagingService;
import com.hust.smarthotel.generic.util.PageRequestCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
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
import static com.hust.smarthotel.generic.constant.FacilityList.*;

@Service
@CacheConfig(cacheNames = {"hotels"})
public class HotelService {

    private static final String AVG_PRICE = "average_price";
    private static final String POINT = "point";
    private static final String RATINGS = "ratings";
    private static final String SPLITTER = ",";

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ManagingService managingService;

    @Autowired
    private HotelAsyncTasks asyncTasks;

    @CacheEvict(value = "desc_hotels_cache", allEntries = true)
    public HotelResponse createHotel(BasicHotel basicHotel, String role, String managerId){

        System.out.println(basicHotel.getPhoneNumber());

        if (hotelRepository.findHotelByPhoneNumber(basicHotel.getPhoneNumber()) != null)
            return HOTEL_EXISTS;

        if (role.equals(MANAGER)){
            List<Managing> managingList = managingService.findManagingByManagerId(managerId);
            if (managingList != null){
                System.out.println(managingList.size());
                if (managingList.size() >= 5){
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

    @CacheEvict(value = "desc_hotels_cache", allEntries = true)
    public HotelResponse updateHotel(String hotelId, BasicHotel basicHotel, String role, String managerId){

        Hotel hotel = hotelRepository.findHotelById(hotelId);
        if (hotel == null)
            return HOTEL_NOT_FOUND;

        String newPhoneNumber = basicHotel.getPhoneNumber();
        if (!hotel.getPhoneNumber().equals(newPhoneNumber) && hotelRepository.findHotelByPhoneNumber(newPhoneNumber) != null){
            return HOTEL_EXISTS;
        }

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
        hotel.setAveragePrice(basicHotel.getAveragePrice());
        if (basicHotel.getStatus() == null)
            hotel.setStatus(true);
        else
            hotel.setStatus(basicHotel.getStatus());

        asyncTasks.updateHotel(hotel);
        return new HotelResponse(true, null, null, hotel);
    }

    @CacheEvict(value = "desc_hotels_cache", allEntries = true)
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

    @CacheEvict(value = "desc_hotels_cache", allEntries = true)
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

    public Hotel findHotelById(String hotelId){
        return hotelRepository.findHotelById(hotelId);
    }

    @Cacheable(value = "desc_hotels_cache")
    public Page<Hotel> findAllSortedByPointDesc(Integer page, Integer pageSize){
        return hotelRepository.findAll(PageRequestCreator.getDescPageRequest(page, pageSize, POINT, RATINGS));
    }

    public Page<Hotel> findHotelsByName(Integer page, Integer pageSize, String name){
        return hotelRepository.findHotelsByName(name, PageRequestCreator.getSimplePageRequest(page, pageSize));
    }

    public Page<Hotel> findHotelsAround(Double lng, Double lat, Long radius){
        return hotelRepository.findHotelsAround(lng, lat, radius, PageRequestCreator.getDescPageRequest(0, 100, POINT));
    }

    public Page<Hotel> findHotelsByPointsAndFacilities(Integer page, Integer pageSize,Integer minPoint, Integer maxPoint, String facilities){
        if (minPoint == null)
            minPoint = 0;
        if (maxPoint == null)
            maxPoint = 5;
        if (facilities == null)
            return hotelRepository.findHotelsByPointBetween(minPoint, maxPoint, PageRequestCreator.getDescPageRequest(page, pageSize, POINT, RATINGS));

        System.out.println(facilities);

        String[] facilityList = facilities.split(SPLITTER);
        Facility facilityCheck = new Facility();
//        if (facilities.equals(""))
//            facilityCheck.checkAll();
//        else {
        if (!facilities.equals("")){
            for (String facility : facilityList){
                switch (facility){
                    case WIFI:
                        facilityCheck.setWifi(true);
                        break;
                    case BAR:
                        facilityCheck.setBar(true);
                        break;
                    case LAUNDRY:
                        facilityCheck.setLaundry(true);
                        break;
                    case FITNESS:
                        facilityCheck.setFitness(true);
                        break;
                }
            }
        }
//        System.out.println(minPoint);
//        System.out.println(maxPoint);
//        System.out.println("wifi: " + facilityCheck.getWifi());
//        System.out.println("bar: " + facilityCheck.getBar());
//        System.out.println("laundry: " + facilityCheck.getLaundry());
//        System.out.println("fitness: " + facilityCheck.getFitness());
        return hotelRepository.findHotelsByPointBetweenAndFacilitiesWifiAndFacilitiesBarAndFacilitiesLaundryAndFacilitiesFitness(minPoint, maxPoint, facilityCheck.getWifi(), facilityCheck.getBar(), facilityCheck.getLaundry(), facilityCheck.getFitness(), PageRequestCreator.getDescPageRequest(page, pageSize, POINT, RATINGS));
    }

    public Page<Hotel> findHotelsInPriceRange(Integer minPrice, Integer maxPrice, Integer direction){
        if (minPrice == null)
            minPrice = 0;
        if (maxPrice == null)
            maxPrice = 99999999;

        return hotelRepository.findHotelsByAveragePriceInRange(minPrice, maxPrice, PageRequestCreator.getPageRequest(0, 100, AVG_PRICE, direction));
    }
}
