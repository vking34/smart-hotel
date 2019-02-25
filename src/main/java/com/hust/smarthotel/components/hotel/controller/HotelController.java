package com.hust.smarthotel.components.hotel.controller;

import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.domain_service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    Page<Hotel> getHotels(@RequestParam(value = "page", required = false) Integer page,
                          @RequestParam(value = "page_size", required = false) Integer pageSize){
        System.out.println("GET");
        return hotelService.findAll(page, pageSize);
    }

}
