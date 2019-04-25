package com.hust.smarthotel.components.booking.controller;


import com.hust.smarthotel.components.booking.app_model.BookingResponse;
import com.hust.smarthotel.components.booking.app_model.StateRequest;
import com.hust.smarthotel.components.booking.domain_model.BookingRecord;
import com.hust.smarthotel.components.booking.domain_model.DetailBookingRecord;
import com.hust.smarthotel.components.booking.domain_service.BookingService;
import com.hust.smarthotel.components.mananging.domain_model.Managing;
import com.hust.smarthotel.components.mananging.domain_service.ManagingService;
import com.hust.smarthotel.components.publish.Publisher;
import com.hust.smarthotel.generic.constant.HeaderConstant;
import com.hust.smarthotel.generic.constant.UrlConstants;
import com.hust.smarthotel.generic.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.hust.smarthotel.generic.response.ErrorResponses.BOOKING_FORBIDDEN_GETTING_RECORD;
import static com.hust.smarthotel.generic.constant.RoleConstants.MANAGER;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(UrlConstants.API +"/booking")
public class BookingRequestController {

    private static final ResponseEntity<BookingResponse> FORBIDDEN = new ResponseEntity<>(BOOKING_FORBIDDEN_GETTING_RECORD, HttpStatus.FORBIDDEN);
    private static final ResponseEntity FORBIDDEN_GET_BOOKING_RECORD = new ResponseEntity(HttpStatus.FORBIDDEN);

    @Autowired
    private BookingService bookingService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ManagingService managingService;

    @Autowired
    private Publisher publisher;

    @GetMapping("/{bookingRecordId}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT' ,'ROLE_MANAGER')")
    ResponseEntity<DetailBookingRecord> getBookingRecord(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                                     @PathVariable String bookingRecordId){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);

        String userId = claims.getSubject();
        String role = claims.get(JwtUtil.ROLE, String.class);

        DetailBookingRecord bookingRecord = bookingService.findDetailBookingRecordById(bookingRecordId);

        if (role.equals(MANAGER)){
            Managing managing = managingService.findManaging(userId, bookingRecord.getHotelRef().toHexString());
            if (managing == null)
                return FORBIDDEN_GET_BOOKING_RECORD;
        } else {
            if (!userId.equals(bookingRecord.getUser().getId()))
                return FORBIDDEN_GET_BOOKING_RECORD;
        }

        return new ResponseEntity<>(bookingRecord, HttpStatus.ACCEPTED);
    }

    @PostMapping("/{bookingRecordId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    ResponseEntity<BookingResponse> changeState(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                                @PathVariable String bookingRecordId,
                                                @Valid @RequestBody StateRequest stateRequest){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);

        String userId = claims.getSubject();
        DetailBookingRecord bookingRecord = bookingService.findDetailBookingRecordById(bookingRecordId);

        Managing managing = managingService.findManaging(userId, bookingRecord.getHotelRef().toHexString());
        if (managing == null)
            return FORBIDDEN;

        bookingRecord = bookingService.changeState(bookingRecord, stateRequest);
        publisher.announceBookingStateToClient(bookingRecordId, bookingRecord);
        return new ResponseEntity<>(new BookingResponse(true, null, null, bookingRecord), HttpStatus.ACCEPTED);
    }
}
