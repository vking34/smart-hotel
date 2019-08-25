package com.hust.smarthotel.components.booking.controller;

import com.hust.smarthotel.components.booking.domain_model.BookingRecord;
import com.hust.smarthotel.components.booking.domain_service.BookingService;
import com.hust.smarthotel.components.mananging.domain_service.ManagingService;
import com.hust.smarthotel.generic.constant.HeaderConstant;
import com.hust.smarthotel.generic.constant.RoleConstants;
import com.hust.smarthotel.generic.constant.UrlConstant;
import com.hust.smarthotel.generic.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.hust.smarthotel.generic.response.ErrorResponses.INVALID_TOKEN;

@RestController
@CrossOrigin("*")
@RequestMapping(UrlConstant.API)
public class BookingManagementController {

    private static final ResponseEntity FORBIDDEN = new ResponseEntity<>(INVALID_TOKEN, HttpStatus.FORBIDDEN);


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ManagingService managingService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/hotels/{hotelId}/booking")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    ResponseEntity<Page<BookingRecord>> getBookingListOfHotel(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                                              @PathVariable("hotelId") String hotelId,
                                                              @RequestParam(value = "page", required = false) Integer page,
                                                              @RequestParam(value = "page_size", required = false) Integer pageSize){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String userId = claims.getSubject();
        String role = claims.get(JwtUtil.ROLE, String.class);

        if (role.equals(RoleConstants.MANAGER) && managingService.findManaging(userId, hotelId) == null)
            return FORBIDDEN;

        return new ResponseEntity<>(bookingService.findBookingRecordsOfHotel(hotelId, page, pageSize), HttpStatus.OK);
    }

    @GetMapping("/hotels/{hotelId}/booking/not_fetched")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    ResponseEntity<Page<BookingRecord>> getBookingListNotFetched(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                                                 @PathVariable("hotelId") String hotelId,
                                                                 @RequestParam(value = "page", required = false) Integer page,
                                                                 @RequestParam(value = "page_size", required = false) Integer pageSize){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String managerId = claims.getSubject();

        if (managingService.findManaging(managerId, hotelId) == null)
            return FORBIDDEN;

        return new ResponseEntity<>(bookingService.findBookingRecordsNotFetchedByHotel(hotelId, page, pageSize), HttpStatus.OK);
    }

    @GetMapping("/managers/{managerId}/hotels/booking")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    ResponseEntity<Page<BookingRecord>> getBookingList(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                                       @PathVariable("managerId") String managerId,
                                                       @RequestParam(value = "page", required = false) Integer page,
                                                       @RequestParam(value = "page_size", required = false) Integer pageSize){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String userId = claims.getSubject();
        String role = claims.get(JwtUtil.ROLE, String.class);

        if (role.equals(RoleConstants.MANAGER) && !managerId.equals(userId))
            return FORBIDDEN;

        return new ResponseEntity<>(bookingService.findBookingList(managerId, page, pageSize), HttpStatus.OK);
    }

    @GetMapping("/hotels/{hotelId}/booking/new_created")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    ResponseEntity<Page<BookingRecord>> getListOfNotProcessedBookingRequests(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                                                             @PathVariable("hotelId") String hotelId,
                                                                             @RequestParam(value = "page", required = false) Integer page,
                                                                             @RequestParam(value = "page_size", required = false) Integer pageSize){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String managerId = claims.getSubject();

        if (managingService.findManaging(managerId, hotelId) == null)
            return FORBIDDEN;

        return new ResponseEntity<>(bookingService.findNewCreatedBookingRequestsOfHotel(hotelId, page, pageSize) , HttpStatus.OK);
    }
}
