package com.hust.smarthotel.components.hotel.controller;

import com.hust.smarthotel.components.hotel.app_model.BasicHotel;
import com.hust.smarthotel.components.hotel.app_model.HotelResponse;
import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.domain_service.HotelService;
import com.hust.smarthotel.generic.constant.HeaderConstant;
import com.hust.smarthotel.generic.constant.UrlConstants;
import com.hust.smarthotel.generic.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(UrlConstants.API + "/hotels")
public class HotelsController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private JwtUtil jwtUtil;

    @ApiOperation(value = "Get/Search/Filter hotels...")
    @GetMapping
    Page<Hotel> getHotels(@RequestParam(value = "page", required = false) Integer page,
                          @RequestParam(value = "page_size", required = false) Integer pageSize,
                          @RequestParam(value = "name", required = false) String name,
                          @RequestParam(value = "lng", required = false) Double lng,
                          @RequestParam(value = "lat", required = false) Double lat,
                          @RequestParam(value = "radius", required = false) Long radius
    ){

        if (name != null)
            return hotelService.findHotelsByName(page, pageSize, name);
        if (radius != null || lng != null || lat != null)
            return hotelService.findHotelsAround(lng, lat, radius);

        return hotelService.findAllSortedByPointDesc(page, pageSize);
    }

    @ApiOperation(value = "Create a hotel")
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    ResponseEntity<HotelResponse> createHotel(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                              @Valid @RequestBody BasicHotel basicHotel){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String userId = claims.getSubject();
        String role = claims.get(JwtUtil.ROLE, String.class);

        HotelResponse hotelResponse = hotelService.createHotel(basicHotel, role, userId);
        if (!hotelResponse.getStatus())
            return new ResponseEntity<>(hotelResponse, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(hotelResponse, HttpStatus.OK);
    }
}
