package com.hust.smarthotel.components.user.controller;


import com.hust.smarthotel.components.user.app_model.UpdatedUser;
import com.hust.smarthotel.components.user.app_model.UserResponse;
import com.hust.smarthotel.components.user.domain_model.User;
import com.hust.smarthotel.components.user.domain_service.UserService;
import com.hust.smarthotel.generic.constant.HeaderConstant;
import com.hust.smarthotel.generic.constant.UrlConstants;

import com.hust.smarthotel.generic.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import static com.hust.smarthotel.generic.response.ErrorResponses.USER_FORBIDDEN;
import static com.hust.smarthotel.generic.constant.RoleConstants.CLIENT;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(UrlConstants.API+ "/users/{userId}")
public class UserController {

    private static final ResponseEntity FORBIDDEN = new ResponseEntity<>(USER_FORBIDDEN, HttpStatus.FORBIDDEN);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @ApiOperation("Get more information about an user")
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    ResponseEntity<User> getUser(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                 @PathVariable String userId){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String userIdInToken = claims.getSubject();
        String role = claims.get(JwtUtil.ROLE, String.class);

        if ( role.equals(CLIENT) && !userId.equals(userIdInToken))
            return FORBIDDEN;

        User user = userService.findUser(userId);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation("Update information a user")
    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    ResponseEntity<UserResponse> updateClient(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                              @PathVariable String userId,
                                              @Valid @RequestBody UpdatedUser requestedClient){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String userIdInToken = claims.getSubject();
        String role = claims.get(JwtUtil.ROLE, String.class);

        if ( role.equals(CLIENT) && !userId.equals(userIdInToken))
            return FORBIDDEN;

        UserResponse userResponse = userService.updateClient(userId, requestedClient);
        if (!userResponse.getStatus())
            return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @ApiOperation("Delete an user")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    ResponseEntity<UserResponse> deleteUser(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String authorizationField,
                                            @PathVariable String userId){
        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        Claims claims = jwtUtil.getClaims(token);
        String userIdInToken = claims.getSubject();
        String role = claims.get(JwtUtil.ROLE, String.class);

        if ( role.equals(CLIENT) && !userId.equals(userIdInToken))
            return FORBIDDEN;

        UserResponse userResponse = userService.deleteUser(userId);
        if (!userResponse.getStatus())
            return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

}
