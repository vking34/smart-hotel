package com.hust.smarthotel.components.photo.controller;


import com.hust.smarthotel.components.mananging.domain_model.Managing;
import com.hust.smarthotel.components.mananging.domain_service.ManagingService;
import com.hust.smarthotel.components.photo.app_model.PhotoResponse;
import com.hust.smarthotel.components.photo.domain_service.PhotoService;
import com.hust.smarthotel.generic.constant.HeaderConstant;
import com.hust.smarthotel.generic.constant.UrlConstants;
import com.hust.smarthotel.generic.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import javax.validation.constraints.NotNull;
import java.io.IOException;

import static com.hust.smarthotel.generic.constant.RoleConstants.MANAGER;
import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_HOTEL_NOT_MANAGER;
import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_HOTEL_NOT_MANAGING;


@RestController
@RequestMapping(UrlConstants.API)
@CrossOrigin("*")
public class PhotoController {

    private static final ResponseEntity NOT_MANAGER = new ResponseEntity<>(PHOTO_HOTEL_NOT_MANAGER, HttpStatus.FORBIDDEN);
    private static final ResponseEntity NOT_MANAGING = new ResponseEntity<>(PHOTO_HOTEL_NOT_MANAGING, HttpStatus.FORBIDDEN);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ManagingService managingService;

    @Autowired
    private PhotoService photoService;

    @PostMapping("/hotels/{hotelId}/photos")
    public ResponseEntity<PhotoResponse> uploadPhoto(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                                     @PathVariable String hotelId,
                                                     @RequestParam("file") MultipartFile multipartFile){
        System.out.println("upload ...");
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String role = claims.get(JwtUtil.ROLE, String.class);

        if (!role.equals(MANAGER))
            return NOT_MANAGER;

        String managerId = claims.getSubject();
        Managing managing = managingService.findManaging(managerId, hotelId);
        if (managing == null){
            return NOT_MANAGING;
        }

        System.out.println("upload photo...");
        System.out.println(multipartFile);

        PhotoResponse photoResponse;
//        if (isAvatar)
            photoResponse = photoService.setLogoToHotel(hotelId, multipartFile);


        if (!photoResponse.getStatus())
            return new ResponseEntity<>(photoResponse, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(photoResponse, HttpStatus.OK);
    }
}
