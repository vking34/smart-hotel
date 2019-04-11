package com.hust.smarthotel.components.mananging.controller;

import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.domain_service.HotelService;
import com.hust.smarthotel.components.mananging.app_model.HotelRequest;
import com.hust.smarthotel.components.mananging.app_model.ManagingResponse;
import com.hust.smarthotel.components.mananging.domain_service.ManagingService;
import com.hust.smarthotel.components.user.domain_model.User;
import com.hust.smarthotel.components.user.domain_service.UserService;
import com.hust.smarthotel.generic.constant.UrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.hust.smarthotel.generic.response.ErrorResponses.MANAGING_USER_NOT_FOUND;
import static com.hust.smarthotel.generic.response.ErrorResponses.MANAGING_NOT_MANAGER;
import static com.hust.smarthotel.generic.response.ErrorResponses.MANAGING_HOTEL_NOT_FOUND;

import static com.hust.smarthotel.generic.constant.RoleConstants.MANAGER;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(UrlConstants.API + "/users/managers")
public class ManagingHotelController {

    private static final ResponseEntity<ManagingResponse> USER_NOT_FOUND = new ResponseEntity<>(MANAGING_USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
    private static final ResponseEntity<ManagingResponse> NOT_MANAGER = new ResponseEntity<>(MANAGING_NOT_MANAGER, HttpStatus.BAD_REQUEST);
    private static final ResponseEntity<ManagingResponse> HOTEL_NOT_FOUND = new ResponseEntity<>(MANAGING_HOTEL_NOT_FOUND, HttpStatus.BAD_REQUEST);

    @Autowired
    private UserService userService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private ManagingService managingService;

    @PostMapping("/{managerId}/hotels")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<ManagingResponse> addHotelToManager(@PathVariable String managerId,
                                                       @Valid @RequestBody HotelRequest hotelRequest){

        User manager = userService.findUser(managerId);
        if (manager == null)
            return USER_NOT_FOUND;
        if (!manager.getRole().equals(MANAGER))
            return NOT_MANAGER;

        String hotelId = hotelRequest.getHotelId();
        Hotel hotel = hotelService.findHotelById(hotelId);
        if (hotel == null)
            return HOTEL_NOT_FOUND;

        ManagingResponse managingResponse = managingService.addHotelToManager(managerId, hotelId);
        if (!managingResponse.getStatus())
            return new ResponseEntity<>(managingResponse, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(managingResponse, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{managerId}/hotels/{hotelId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<ManagingResponse> removeHotelFromManager(@PathVariable("managerId") String managerId,
                                                            @PathVariable("hotelId") String hotelId){
        ManagingResponse managingResponse = managingService.removeHotelFromManager(managerId, hotelId);
        if (!managingResponse.getStatus())
            return new ResponseEntity<>(managingResponse, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(managingResponse, HttpStatus.ACCEPTED);
    }
}
