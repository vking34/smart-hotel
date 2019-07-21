package com.hust.smarthotel.components.booking.controller;


import com.hust.smarthotel.components.booking.app_model.BookingResponse;
import com.hust.smarthotel.components.booking.app_model.DetailBookingResponse;
import com.hust.smarthotel.components.booking.app_model.StateRequest;
import com.hust.smarthotel.components.booking.domain_model.BookingRecord;
import com.hust.smarthotel.components.booking.domain_model.DetailBookingRecord;
import com.hust.smarthotel.components.booking.domain_service.BookingService;
import com.hust.smarthotel.components.mananging.domain_model.Managing;
import com.hust.smarthotel.components.mananging.domain_service.ManagingService;
import com.hust.smarthotel.components.publish.Publisher;
import com.hust.smarthotel.generic.constant.HeaderConstant;
import com.hust.smarthotel.generic.constant.UrlConstant;
import com.hust.smarthotel.generic.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import static com.hust.smarthotel.generic.response.ErrorResponses.BOOKING_RECORD_NOT_FOUND;
import static com.hust.smarthotel.generic.response.ErrorResponses.BOOKING_FORBIDDEN_GETTING_RECORD;
import static com.hust.smarthotel.generic.response.ErrorResponses.BOOKING_FORBIDDEN_CANCELATION;
import static com.hust.smarthotel.generic.response.ErrorResponses.BOOKING_NOT_FOUND;
import static com.hust.smarthotel.generic.constant.RoleConstants.MANAGER;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(UrlConstant.API +"/booking")
public class BookingRequestController {

    private static final ResponseEntity FORBIDDEN = new ResponseEntity<>(BOOKING_FORBIDDEN_GETTING_RECORD, HttpStatus.FORBIDDEN);
    private static final ResponseEntity FORBIDDEN_GET_BOOKING_RECORD = new ResponseEntity(HttpStatus.FORBIDDEN);
    private static final ResponseEntity RECORD_NOT_FOUND = new ResponseEntity<>(BOOKING_RECORD_NOT_FOUND, HttpStatus.BAD_REQUEST);
    private static final ResponseEntity FORBIDDEN_CANCELATION = new ResponseEntity<>(BOOKING_FORBIDDEN_CANCELATION, HttpStatus.FORBIDDEN);
    private static final ResponseEntity BOOKING_REQUEST_NOT_FOUND = new ResponseEntity<>(BOOKING_NOT_FOUND, HttpStatus.BAD_REQUEST);

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
        if (bookingRecord == null)
            return RECORD_NOT_FOUND;

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
        if (bookingRecord == null)
            return BOOKING_REQUEST_NOT_FOUND;

        Managing managing = managingService.findManaging(userId, bookingRecord.getHotelRef().toHexString());
        if (managing == null)
            return FORBIDDEN;

        bookingRecord = bookingService.changeState(bookingRecord, stateRequest);
        publisher.announceBookingStateToClient(bookingRecordId, bookingRecord);
        return new ResponseEntity<>(new BookingResponse(true, null, null, bookingRecord), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{bookingRecordId}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    ResponseEntity<DetailBookingResponse> cancelBookingRequest(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                                               @PathVariable String bookingRecordId){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String userId = claims.getSubject();

        DetailBookingRecord bookingRecord = bookingService.findDetailBookingRecordById(bookingRecordId);
        if (bookingRecord == null)
            return RECORD_NOT_FOUND;

        if (!bookingRecord.getUser().getId().equals(userId))
            return FORBIDDEN_CANCELATION;

        DetailBookingResponse bookingResponse = bookingService.cancelBookingRequest(bookingRecord);
        if (!bookingResponse.getStatus())
            return new ResponseEntity<>(bookingResponse, HttpStatus.BAD_REQUEST);

        publisher.announceBookingCancelation(bookingRecord);
        return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
    }

    @PostMapping("/{bookingRecordId}/fetched")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    ResponseEntity<BookingResponse> fetchBookingRequest(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                                        @PathVariable String bookingRecordId){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String userId = claims.getSubject();

        BookingRecord bookingRecord = bookingService.findBookingRecordById(bookingRecordId);
        if (bookingRecord == null)
            return RECORD_NOT_FOUND;

        if (!bookingRecord.getUser().getId().equals(userId))
            return FORBIDDEN_CANCELATION;

        BookingResponse bookingResponse = bookingService.changeFetchedStatus(bookingRecord);

        if (!bookingResponse.getStatus())
            return new ResponseEntity<>(bookingResponse, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
    }
}
