package com.hust.smarthotel.components.user.controller;


import com.hust.smarthotel.components.user.app_model.UserResponse;
import com.hust.smarthotel.components.user.domain_model.User;
import com.hust.smarthotel.components.user.domain_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    private UserService userService;

    @PostMapping
    ResponseEntity<UserResponse> signUp(@Valid @RequestBody User user){
        UserResponse userResponse = userService.createClient(user);
        if (!userResponse.getStatus())
            return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userResponse, HttpStatus.ACCEPTED);
    }
}
