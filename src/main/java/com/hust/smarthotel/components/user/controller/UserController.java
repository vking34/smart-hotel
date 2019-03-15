package com.hust.smarthotel.components.user.controller;


import com.hust.smarthotel.components.user.app_model.ManagerResponse;
import com.hust.smarthotel.components.user.app_model.UserResponse;
import com.hust.smarthotel.components.user.domain_model.Manager;
import com.hust.smarthotel.components.user.domain_model.User;
import com.hust.smarthotel.components.user.domain_service.UserService;
import com.hust.smarthotel.generic.constant.UrlConstants;
import com.hust.smarthotel.generic.response.ErrorResponses;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(UrlConstants.API+ "/users/{userId}")
public class UserController {

    @Autowired
    private UserService userService;


    @ApiOperation("Get more information about an user")
    @GetMapping
    Manager getUser(@PathVariable String userId){
        return userService.findUser(userId);
    }

    @ApiOperation("Update information a user")
    @PutMapping
    ResponseEntity<UserResponse> updateClient(@PathVariable String userId, @Valid @RequestBody User requestedClient){
       UserResponse userResponse = userService.updateClient(userId, requestedClient);
       if (!userResponse.getStatus())
           return new ResponseEntity<UserResponse>(userResponse, HttpStatus.BAD_REQUEST);
       return new ResponseEntity<UserResponse>(userResponse, HttpStatus.ACCEPTED);
    }

    @ApiOperation("Delete an user")
    @DeleteMapping
    ResponseEntity<ManagerResponse> deleteUser(@PathVariable String userId){
        ManagerResponse userResponse = userService.deleteUser(userId);
        if (!userResponse.getStatus())
            return new ResponseEntity<ManagerResponse>(userResponse, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<ManagerResponse>(userResponse, HttpStatus.ACCEPTED);
    }

}
