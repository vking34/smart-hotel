package com.hust.smarthotel.components.photo.controller;


import com.hust.smarthotel.components.mananging.domain_model.Managing;
import com.hust.smarthotel.components.mananging.domain_service.ManagingService;
import com.hust.smarthotel.components.photo.app_model.DeletePhotoRequest;
import com.hust.smarthotel.components.photo.app_model.PhotoResponse;
import com.hust.smarthotel.components.photo.domain_service.PhotoService;
import com.hust.smarthotel.generic.constant.HeaderConstant;
import com.hust.smarthotel.generic.constant.UrlConstants;
import com.hust.smarthotel.generic.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.hust.smarthotel.generic.constant.RoleConstants.MANAGER;
import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_HOTEL_NOT_MANAGER;
import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_HOTEL_NOT_MANAGING;
import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_INVALID_TOKEN;
import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_NOT_IMAGE;
import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_OVER_SIZE;
import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_INTERNAL_ERROR;

@RestController
@RequestMapping(UrlConstants.API)
@CrossOrigin("*")
public class PhotoController {

    private static final ResponseEntity NOT_MANAGER = new ResponseEntity<>(PHOTO_HOTEL_NOT_MANAGER, HttpStatus.FORBIDDEN);
    private static final ResponseEntity NOT_MANAGING = new ResponseEntity<>(PHOTO_HOTEL_NOT_MANAGING, HttpStatus.FORBIDDEN);
    private static final ResponseEntity INVALID_TOKEN = new ResponseEntity<>(PHOTO_INVALID_TOKEN, HttpStatus.FORBIDDEN);
    private static final ResponseEntity NOT_IMAGE = new ResponseEntity<>(PHOTO_NOT_IMAGE, HttpStatus.BAD_REQUEST);
    private static final ResponseEntity OVER_SIZE = new ResponseEntity<>(PHOTO_OVER_SIZE, HttpStatus.BAD_REQUEST);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ManagingService managingService;

    @Autowired
    private PhotoService photoService;


    @PostMapping("/hotels/{hotelId}/photos")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<PhotoResponse> uploadPhoto(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                                     @PathVariable String hotelId,
                                                     @NotNull @RequestParam("file") MultipartFile multipartFile,
                                                     @NotNull @RequestParam("type") String type){
        try {
            if (ImageIO.read(multipartFile.getInputStream()) == null)
                return NOT_IMAGE;
        }
        catch (IOException e){
            e.printStackTrace();
            return new ResponseEntity<>(PHOTO_INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

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

        PhotoResponse photoResponse = photoService.addPhotoToHotel(hotelId, multipartFile, type);
        return getPhotoResponseResponseEntity(photoResponse);
    }

    @DeleteMapping("/hotels/{hotelId}/photos")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<PhotoResponse> deleteHotelPhoto(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                                     @PathVariable String hotelId,
                                                     @Valid @RequestBody DeletePhotoRequest deletePhotoRequest){
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

        PhotoResponse photoResponse = photoService.deleteHotelPhoto(hotelId, deletePhotoRequest);
        return getPhotoResponseResponseEntity(photoResponse);
    }

    @PostMapping("/users/{userId}/photos")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<PhotoResponse> setUserAvatar(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                                       @PathVariable String userId,
                                                       @NotNull @RequestParam("file") MultipartFile multipartFile){
        try {
            if (ImageIO.read(multipartFile.getInputStream()) == null)
                return NOT_IMAGE;
        }
        catch (IOException e){
            e.printStackTrace();
            return new ResponseEntity<>(PHOTO_INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String tokenId = claims.getSubject();
        if (!userId.equals(tokenId))
            return INVALID_TOKEN;

        PhotoResponse photoResponse = photoService.setUserAvatar(userId, multipartFile);
        return getPhotoResponseResponseEntity(photoResponse);
    }

    @DeleteMapping("/users/{userId}/photos")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<PhotoResponse> removeUserAvatar(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                                          @PathVariable String userId){

        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String tokenId = claims.getSubject();
        if (!userId.equals(tokenId))
            return INVALID_TOKEN;

        PhotoResponse photoResponse = photoService.removeUserAvatar(userId);
        return getPhotoResponseResponseEntity(photoResponse);
    }

    private ResponseEntity<PhotoResponse> getPhotoResponseResponseEntity(PhotoResponse photoResponse) {
        if (!photoResponse.getStatus()){
            if (photoResponse.getCode() == 602)
                return new ResponseEntity<>(photoResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(photoResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(photoResponse, HttpStatus.OK);
    }
}
