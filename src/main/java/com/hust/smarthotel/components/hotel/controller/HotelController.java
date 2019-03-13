package com.hust.smarthotel.components.hotel.controller;

import com.hust.smarthotel.components.hotel.app_model.BasicHotel;
import com.hust.smarthotel.components.hotel.app_model.HotelResponse;
import com.hust.smarthotel.components.hotel.app_model.HotelStatus;
import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.domain_service.HotelService;
import com.hust.smarthotel.generic.constant.UrlConstants;
import com.hust.smarthotel.generic.response.ErrorResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(UrlConstants.API + "/hotels/{hotelId}")
public class HotelController {

    @Autowired
    private HotelService hotelService;


    @GetMapping
    Hotel getHotel(@PathVariable String hotelId){
        return hotelService.findHotelById(hotelId);
    }

    @PutMapping
    ResponseEntity<HotelResponse> updateHotel(@PathVariable String hotelId, @Valid @RequestBody BasicHotel basicHotel){
        Hotel hotel = hotelService.updateHotel(hotelId, basicHotel);
        if (hotel == null)
            return new ResponseEntity(ErrorResponses.NOT_FOUND, HttpStatus.NOT_FOUND);
        return new ResponseEntity(new HotelResponse(hotel), HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    ResponseEntity<HotelResponse> deleteHotel(@PathVariable String hotelId){
        Hotel hotel = hotelService.deleteHotel(hotelId);
        if (hotel == null)
            return new ResponseEntity(ErrorResponses.NOT_FOUND, HttpStatus.NOT_FOUND);

        return new ResponseEntity<HotelResponse>(new HotelResponse(hotel), HttpStatus.ACCEPTED);
    }

    @PostMapping("/status")
    ResponseEntity<HotelResponse> changeHotelStatus(@PathVariable String hotelId, @Valid @RequestBody HotelStatus hotelStatus){
        Hotel hotel = hotelService.changeHotelStatus(hotelId, hotelStatus);
        if (hotel == null)
            return new ResponseEntity(ErrorResponses.NOT_FOUND, HttpStatus.NOT_FOUND);

        return new ResponseEntity<HotelResponse>(new HotelResponse(hotel), HttpStatus.ACCEPTED);
    }
}
