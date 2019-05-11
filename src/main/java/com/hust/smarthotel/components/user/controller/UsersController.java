package com.hust.smarthotel.components.user.controller;

import com.hust.smarthotel.components.hotel.app_model.ManagerStatus;
import com.hust.smarthotel.components.user.app_model.ManagerResponse;
import com.hust.smarthotel.components.user.app_model.UserResponse;
import com.hust.smarthotel.components.user.domain_model.BasicManager;
import com.hust.smarthotel.components.user.domain_model.Manager;
import com.hust.smarthotel.components.user.domain_model.User;
import com.hust.smarthotel.components.user.domain_service.UserService;
import com.hust.smarthotel.generic.constant.UrlConstants;
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
@RequestMapping(UrlConstants.API+ "/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<User> getUsers(@RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "page_size", required = false) Integer pageSize,
                        @RequestParam(value = "name", required = false) String name,
                        @RequestParam(value = "phone", required = false) String phone,
                        @RequestParam(value = "email", required = false) String email,
                        @RequestParam(value = "role", required = false) String role
                        ){
        return userService.findAll(page, pageSize);
    }


    @GetMapping("/clients")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<User> getClients(@RequestParam(value = "page", required = false) Integer page,
                          @RequestParam(value = "page_size", required = false) Integer pageSize){
        return userService.findClients(page, pageSize);
    }

    @ApiOperation(value = "Create a client")
    @PostMapping("/clients")
    public ResponseEntity<UserResponse> createClient(@Valid @RequestBody User requestClient){
        UserResponse userResponse = userService.createClient(requestClient);
        if (!userResponse.getStatus())
            return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Get/Search/Filter Managers... Manager have one more field hotel_id than Client")
    @GetMapping("/managers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<User> getManagers(@RequestParam(value = "page", required = false) Integer page,
                           @RequestParam(value = "page_size", required = false) Integer pageSize){
        return userService.findManagers(page, pageSize);
    }

    @PostMapping("/managers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> createManager(@Valid @RequestBody User manager){
        UserResponse userResponse = userService.createManager(manager);
        if (!userResponse.getStatus())
            return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping("/verifying-managers")
    public ResponseEntity<ManagerResponse> requestManager(@Valid @RequestBody BasicManager manager){
        ManagerResponse managerResponse = userService.requestVerifyingManager(manager);
        if (!managerResponse.getStatus())
            return new ResponseEntity<>(managerResponse, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(managerResponse, HttpStatus.OK);
    }

    @GetMapping("/verifying-managers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<Manager> getVerifyingManagers(@RequestParam(value = "page", required = false) Integer page,
                                      @RequestParam(value = "page_size", required = false) Integer pageSize,
                                      @RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "phone", required = false) String phone){
        return userService.findVerifyingManagers(page, pageSize, name, phone);
    }

    @GetMapping("/verifying-managers/{managerId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Manager getVerifyingManager(@PathVariable String managerId){
        return userService.findManager(managerId);
    }

    @PostMapping("/verifying-managers/{managerId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> changeVerifyingManagerStatus(@PathVariable String managerId,@Valid @RequestBody ManagerStatus managerStatus){
        UserResponse userResponse = userService.verifyManager(managerId, managerStatus);
        if (!userResponse.getStatus())
            return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
