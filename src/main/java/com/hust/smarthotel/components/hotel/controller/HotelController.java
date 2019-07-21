package com.hust.smarthotel.components.hotel.controller;

import com.hust.smarthotel.components.hotel.app_model.BasicHotel;
import com.hust.smarthotel.components.hotel.app_model.HotelResponse;
import com.hust.smarthotel.components.hotel.app_model.HotelStatus;
import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.domain_service.HotelService;
import com.hust.smarthotel.generic.constant.HeaderConstant;
import com.hust.smarthotel.generic.constant.UrlConstant;
import com.hust.smarthotel.generic.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(UrlConstant.API + "/hotels/{hotelId}")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private JwtUtil jwtUtil;

    @ApiOperation("Get more information about a hotel")
    @GetMapping
    Hotel getHotel(@PathVariable String hotelId){
        return hotelService.findHotelById(hotelId);
    }

    @ApiOperation("Update information for a hotel")
    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    ResponseEntity<HotelResponse> updateHotel(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                              @PathVariable String hotelId, @Valid @RequestBody BasicHotel basicHotel){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String userId = claims.getSubject();
        String role = claims.get(JwtUtil.ROLE, String.class);

        HotelResponse hotelResponse = hotelService.updateHotel(hotelId, basicHotel, role, userId);
        if (!hotelResponse.getStatus())
            return new ResponseEntity<>(hotelResponse, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(hotelResponse, HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    ResponseEntity<HotelResponse> deleteHotel(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                              @PathVariable String hotelId){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String userId = claims.getSubject();
        String role = claims.get(JwtUtil.ROLE, String.class);

        HotelResponse hotelResponse = hotelService.deleteHotel(hotelId, role, userId);

        if (!hotelResponse.getStatus())
            return new ResponseEntity<>(hotelResponse, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(hotelResponse, HttpStatus.OK);
    }

    @PostMapping("/status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    ResponseEntity<HotelResponse> changeHotelStatus(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                                    @PathVariable String hotelId,
                                                    @Valid @RequestBody HotelStatus hotelStatus){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String userId = claims.getSubject();
        String role = claims.get(JwtUtil.ROLE, String.class);

        HotelResponse hotelResponse = hotelService.changeHotelStatus(hotelId, hotelStatus, role, userId);

        if (!hotelResponse.getStatus())
            return new ResponseEntity<>(hotelResponse, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(hotelResponse, HttpStatus.OK);
    }
}
