package com.hust.smarthotel.components.booking.controller;


import com.hust.smarthotel.components.booking.domain_model.BookingRecord;
import com.hust.smarthotel.components.booking.domain_service.BookingService;
import com.hust.smarthotel.generic.constant.HeaderConstant;
import com.hust.smarthotel.generic.constant.UrlConstants;
import com.hust.smarthotel.generic.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(UrlConstants.API +"/booking")
public class BookingRequestController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/{bookingRecordId}")
    BookingRecord getBookingRecord(@RequestHeader(value = HeaderConstant.AUTHORIZATION, required = true) String authorizationField,
                                   @PathVariable String bookingRecordId){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        System.out.println(claims);

        return bookingService.findBookingRecordById(bookingRecordId);
    }

}
