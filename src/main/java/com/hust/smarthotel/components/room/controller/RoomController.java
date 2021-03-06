package com.hust.smarthotel.components.room.controller;


import com.hust.smarthotel.components.mananging.domain_model.Managing;
import com.hust.smarthotel.components.mananging.domain_service.ManagingService;
import com.hust.smarthotel.components.room.app_model.RoomRequest;
import com.hust.smarthotel.components.room.app_model.RoomResponse;
import com.hust.smarthotel.components.room.domain_model.Rooms;
import com.hust.smarthotel.components.room.domain_service.RoomService;
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

import static com.hust.smarthotel.generic.response.ErrorResponses.ROOM_FORBIDDEN;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(UrlConstant.API + "/hotels")
public class RoomController {

    private final static ResponseEntity<RoomResponse> FORBIDDEN = new ResponseEntity<>(ROOM_FORBIDDEN, HttpStatus.FORBIDDEN);


    @Autowired
    private RoomService roomService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ManagingService managingService;


//    @GetMapping("/rooms")
//    Page<Rooms> getRooms(@RequestParam(value = "page", required = false) Integer page,
//                         @RequestParam(value = "page_size", required = false) Integer pageSize){
//        return roomService.findAll(page, pageSize);
//    }

    @GetMapping("/{hotelId}/rooms")
    Rooms getRoomsByHotelId(@PathVariable String hotelId){
        return roomService.getRoomsInHotel(hotelId);
    }

    @PostMapping("/{hotelId}/rooms")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    ResponseEntity<RoomResponse> createRooms(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                             @PathVariable String hotelId,
                                             @Valid @RequestBody RoomRequest roomRequest){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String userId = claims.getSubject();

        Managing managing = managingService.findManaging(userId, hotelId);
        if (managing == null)
            return FORBIDDEN;

        RoomResponse roomResponse = roomService.createRooms(hotelId, roomRequest);
        if (!roomResponse.getStatus())
            return new ResponseEntity<>(roomResponse, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(roomResponse, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{hotelId}/rooms")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    ResponseEntity<RoomResponse> updateRooms(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                             @PathVariable String hotelId,
                                             @Valid @RequestBody RoomRequest roomRequest){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String userId = claims.getSubject();

        Managing managing = managingService.findManaging(userId, hotelId);
        if (managing == null)
            return FORBIDDEN;

        RoomResponse roomResponse = roomService.updateRooms(hotelId, roomRequest);
        if (!roomResponse.getStatus())
            return new ResponseEntity<>(roomResponse, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(roomResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{hotelId}/rooms")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    ResponseEntity<RoomResponse> deleteRooms(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                             @PathVariable String hotelId){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String userId = claims.getSubject();

        Managing managing = managingService.findManaging(userId, hotelId);
        if (managing == null)
            return FORBIDDEN;

        RoomResponse roomResponse = roomService.deleteRooms(hotelId);
        if (!roomResponse.getStatus())
            return new ResponseEntity<>(roomResponse, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(roomResponse, HttpStatus.OK);
    }
}
