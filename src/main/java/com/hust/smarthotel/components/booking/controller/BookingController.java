package com.hust.smarthotel.components.booking.controller;


import com.hust.smarthotel.components.publish.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private Publisher publisher;

    @PostMapping
    String bookRoom(){
        publisher.announce();
        return "ok";
    }
}
