package com.hust.smarthotel.components.user.controller;

import com.hust.smarthotel.components.user.app_model.ManagerResponse;
import com.hust.smarthotel.components.user.app_model.UserResponse;
import com.hust.smarthotel.components.user.domain_model.Manager;
import com.hust.smarthotel.components.user.domain_model.User;
import com.hust.smarthotel.components.user.domain_service.UserService;
import com.hust.smarthotel.generic.constant.RoleConstants;
import com.hust.smarthotel.generic.constant.UrlConstants;
import com.hust.smarthotel.generic.model.ErrorResponse;
import com.hust.smarthotel.generic.response.ErrorResponses;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(UrlConstants.API+ "/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping
    Page<User> getUsers(@RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "page_size", required = false) Integer pageSize,
                        @RequestParam(value = "name", required = false) String name,
                        @RequestParam(value = "phone", required = false) String phone,
                        @RequestParam(value = "email", required = false) String email,
                        @RequestParam(value = "role", required = false) String role
                        ){
        return userService.findAll(page, pageSize);
    }

    @ApiOperation(value = "Get/Search/Filter Client...")
    @GetMapping("/clients")
    Page<User> getClients(@RequestParam(value = "page", required = false) Integer page,
                          @RequestParam(value = "page_size", required = false) Integer pageSize){
        return userService.findClients(page, pageSize);
    }

    @ApiOperation(value = "Create a client")
    @PostMapping("/clients")
    ResponseEntity<UserResponse> createClient(@Valid @RequestBody User requestClient){
        UserResponse userResponse = userService.createClient(requestClient);
        if (!userResponse.getStatus()){
            return new ResponseEntity<UserResponse>(userResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<UserResponse>(userResponse, HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "Get/Search/Filter Managers... Manager have one more field hotel_id than Client")
    @GetMapping("/managers")
    Page<Manager> getManagers(@RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "page_size", required = false) Integer pageSize){
        return userService.findManagers(page, pageSize);
    }

    @ApiOperation(value = "Create a manager for a hotel", response = ManagerResponse.class)
    @PostMapping("/managers")
    ResponseEntity createManager(@Valid @RequestBody Manager requestedManager){
        ManagerResponse managerResponse = userService.createManger(requestedManager);
        if (!managerResponse.getStatus()){
            return new ResponseEntity<>(managerResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(managerResponse, HttpStatus.ACCEPTED);
    }

    @ApiOperation("Add/Update hotel id for a manager")
    @PostMapping("/managers/{managerId}/hotels/{hotelId}")
    ResponseEntity<ManagerResponse> addHotelToManager(@PathVariable("managerId") String managerId,
                                                      @PathVariable("hotelId") String hotelId){
        Manager manager = userService.findUser(managerId);
        if (manager == null)
            return new ResponseEntity<ManagerResponse>(ErrorResponses.MANAGER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        else if (!manager.getRole().equals(RoleConstants.MANAGER))
            return new ResponseEntity<ManagerResponse>(ErrorResponses.ADD_HOTEL_NOT_ALLOW, HttpStatus.FORBIDDEN);

        ManagerResponse managerResponse = userService.addHotelToManager(hotelId, manager);
        if (!managerResponse.getStatus())
            return new ResponseEntity<ManagerResponse>(managerResponse, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<ManagerResponse>(managerResponse, HttpStatus.ACCEPTED);
    }

}
