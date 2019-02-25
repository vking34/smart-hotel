package com.hust.smarthotel.components.user.controller;

import com.hust.smarthotel.components.user.domain_model.SysUser;
import com.hust.smarthotel.components.user.domain_model.User;
import com.hust.smarthotel.components.user.domain_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    Page<User> getUsers(@RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "page_size", required = false) Integer pageSize){
        return userService.findAll(page, pageSize);
    }

    @GetMapping("/sysusers")
    Page<SysUser> getSysUsers(@RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "page_size", required = false) Integer pageSize){
        return userService.findSysUsers(page, pageSize);
    }
}
