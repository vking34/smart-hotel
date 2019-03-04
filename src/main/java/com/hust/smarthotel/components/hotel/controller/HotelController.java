package com.hust.smarthotel.components.hotel.controller;

import com.hust.smarthotel.components.hotel.app_model.BasicHotel;
import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.domain_service.HotelService;
import com.hust.smarthotel.components.hotel.domain_service.HotelServiceImpl;
import com.hust.smarthotel.generic.constant.UrlConstants;
import com.hust.smarthotel.generic.response.ErrorResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(UrlConstants.API + "/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    Page<Hotel> getHotels(@RequestParam(value = "page", required = false) Integer page,
                          @RequestParam(value = "page_size", required = false) Integer pageSize,
                          @RequestParam(value = "name", required = false) String name,
                          @RequestParam(value = "lng", required = false) Double lng,
                          @RequestParam(value = "lat", required = false) Double lat,
                          @RequestParam(value = "radius", required = false) Integer radius
                          ){

        if (name != null)
            return hotelService.findHotelsByName(page, pageSize, name);

        if (radius != null || lng != null || lat != null)
            return hotelService.findHotelsAround(lng, lat, radius);

        return hotelService.findAllSortedByPointDesc(page, pageSize);
    }

    @PostMapping
    Hotel createHotel(@RequestBody BasicHotel hotel){
        return hotelService.createHotel(hotel);
    }

    @PutMapping("/{hotelId}")
    ResponseEntity updateHotel(@PathVariable String hotelId, @RequestBody BasicHotel basicHotel){
        Hotel hotel = hotelService.updateHotel(hotelId, basicHotel);
        if (hotel == null)
            return new ResponseEntity(ErrorResponses.NOT_FOUND, HttpStatus.ACCEPTED);
        return new ResponseEntity(hotel, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{hotelId}")
    ResponseEntity deleteHotel(@PathVariable String hotelId){
        Hotel hotel = hotelService.deleteHotel(hotelId);
        if (hotel == null)
            return new ResponseEntity(ErrorResponses.NOT_FOUND, HttpStatus.ACCEPTED);
        return new ResponseEntity(hotel, HttpStatus.ACCEPTED);
    }

}
