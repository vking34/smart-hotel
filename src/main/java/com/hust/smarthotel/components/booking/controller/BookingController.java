package com.hust.smarthotel.components.booking.controller;

import com.hust.smarthotel.components.booking.app_model.BookingRequest;
import com.hust.smarthotel.components.booking.app_model.BookingResponse;
import com.hust.smarthotel.components.booking.app_model.DetailBookingResponse;
import com.hust.smarthotel.components.booking.domain_model.BookingRecord;
import com.hust.smarthotel.components.booking.domain_service.BookingService;
import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.domain_service.HotelService;
import com.hust.smarthotel.components.publish.Publisher;
import com.hust.smarthotel.generic.constant.HeaderConstant;
import com.hust.smarthotel.generic.constant.RoleConstants;
import com.hust.smarthotel.generic.constant.UrlConstants;
import com.hust.smarthotel.generic.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDateTime;

import static com.hust.smarthotel.generic.response.ErrorResponses.BOOKING_HOTEL_NOT_FOUND;
import static com.hust.smarthotel.generic.response.ErrorResponses.BOOKING_INVALID_DATE;;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(UrlConstants.API +"/booking")
public class BookingController {

    private static final ResponseEntity HOTEL_NOT_FOUND = new ResponseEntity<>(BOOKING_HOTEL_NOT_FOUND, HttpStatus.BAD_REQUEST);
    private static final ResponseEntity INVALID_DATE = new ResponseEntity<>(BOOKING_INVALID_DATE, HttpStatus.BAD_REQUEST);

    @Autowired
    private Publisher publisher;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    ResponseEntity<DetailBookingResponse> bookRoom(@Valid @RequestBody BookingRequest bookingRequest){
        Hotel hotel = hotelService.findHotelById(bookingRequest.getHotelId());
        if (hotel == null)
            return HOTEL_NOT_FOUND;

        LocalDateTime checkinDate = bookingRequest.getCheckinDate();
        LocalDateTime checkoutDate = bookingRequest.getCheckoutDate();
        if (checkinDate.isAfter(checkoutDate) || checkinDate.isEqual(checkoutDate))
            return INVALID_DATE;

        DetailBookingResponse bookingResponse = bookingService.insert(bookingRequest, hotel);
        BookingRecord bookingRecord = bookingResponse.getBookingRecord();
        publisher.announceBookRequest(bookingRecord.getHotelId(), bookingRecord.getId());
        return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
    }

    @GetMapping
//    @PreAuthorize("hasRole('ROLE_CLIENT')")
    ResponseEntity<Page<BookingRecord>> getBookingRecords(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                          @RequestParam(value = "page", required = false) Integer page,
                                          @RequestParam(value = "page_size", required = false) Integer pageSize){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String role = claims.get(JwtUtil.ROLE, String.class);
        if (!role.equals(RoleConstants.CLIENT))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        String userId = claims.getSubject();

        return new ResponseEntity<>(bookingService.findBookingRecordsByUserId(userId, page, pageSize), HttpStatus.OK);
    }


}
