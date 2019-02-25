package com.hust.smarthotel.components.room.controller;


import com.hust.smarthotel.components.room.domain_model.Rooms;
import com.hust.smarthotel.components.room.domain_service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/rooms")
    Page<Rooms> getRooms(@RequestParam(value = "page", required = false) Integer page,
                         @RequestParam(value = "page_size", required = false) Integer pageSize){
        return roomService.findAll(page, pageSize);
    }

    @GetMapping("/hotels/{hotelId}/rooms")
    Rooms getRoomsByHotelId(@PathVariable String hotelId){
        return roomService.getRoomsInHotel(hotelId);
    }
}
