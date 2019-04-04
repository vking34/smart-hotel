//package com.hust.smarthotel.components.booking.controller;
//
//
//import com.hust.smarthotel.components.booking.app_model.BookingResponse;
//import com.hust.smarthotel.components.booking.domain_service.BookingService;
//import com.hust.smarthotel.generic.constant.HeaderConstant;
//import com.hust.smarthotel.generic.constant.UrlConstants;
//import com.hust.smarthotel.generic.util.JwtUtil;
//import io.jsonwebtoken.Claims;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import static com.hust.smarthotel.generic.constant.RoleConstants.MANAGER;
//import static com.hust.smarthotel.generic.response.ErrorResponses.FORBIDDEN_GETTING_BOOKING_RECORD;
//
//
//@RestController
//@CrossOrigin(origins = "*")
//@RequestMapping(UrlConstants.API +"/booking")
//public class BookingRequestController {
//
//    private static final ResponseEntity FORBIDDEN = new ResponseEntity<>(FORBIDDEN_GETTING_BOOKING_RECORD, HttpStatus.FORBIDDEN);
//
//    @Autowired
//    private BookingService bookingService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @GetMapping("/{bookingRecordId}")
//    ResponseEntity<BookingResponse> getBookingRecord(@RequestHeader(value = HeaderConstant.AUTHORIZATION, required = true) String authorizationField,
//                                                     @PathVariable String bookingRecordId){
//        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
//        Claims claims = jwtUtil.getClaims(token);
////        System.out.println(claims);
////        String role = claims.get("role", String.class);
////        if (!role.equals(MANAGER))
////            return FORBIDDEN;
//        String userId = claims.getSubject();
//
//
////        return bookingService.findBookingRecordById(bookingRecordId);
//
//    }
//
//}
