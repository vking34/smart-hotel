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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.hust.smarthotel.generic.constant.RoleConstants.MANAGER;
import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_HOTEL_NOT_MANAGER;
import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_HOTEL_NOT_MANAGING;
import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_INVALID_TOKEN;


@RestController
@RequestMapping(UrlConstants.API)
@CrossOrigin("*")
public class PhotoController {

    private static final ResponseEntity NOT_MANAGER = new ResponseEntity<>(PHOTO_HOTEL_NOT_MANAGER, HttpStatus.FORBIDDEN);
    private static final ResponseEntity NOT_MANAGING = new ResponseEntity<>(PHOTO_HOTEL_NOT_MANAGING, HttpStatus.FORBIDDEN);
    private static final ResponseEntity INVALID_TOKEN = new ResponseEntity<>(PHOTO_INVALID_TOKEN, HttpStatus.FORBIDDEN);


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ManagingService managingService;

    @Autowired
    private PhotoService photoService;

    @PostMapping("/hotels/{hotelId}/photos")
    public ResponseEntity<PhotoResponse> uploadPhoto(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                                     @PathVariable String hotelId,
                                                     @NotNull @RequestParam("file") MultipartFile multipartFile,
                                                     @NotNull @RequestParam("type") String type){

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
    public ResponseEntity<PhotoResponse> deletePhoto(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
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

        PhotoResponse photoResponse = photoService.deletePhoto(hotelId, deletePhotoRequest);

        return getPhotoResponseResponseEntity(photoResponse);
    }

    @PostMapping("/users/{userId}/photos")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_MANAGER')")
    public ResponseEntity<PhotoResponse> setUserAvatar(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                                       @PathVariable String userId,
                                                       @NotNull @RequestParam("file") MultipartFile multipartFile){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String tokenId = claims.getSubject();
        if (!userId.equals(tokenId))
            return INVALID_TOKEN;


        PhotoResponse photoResponse = photoService.setUserAvatar(userId, multipartFile);

        return getPhotoResponseResponseEntity(photoResponse);
    }

    @DeleteMapping("/users/{userId}/photos")
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
