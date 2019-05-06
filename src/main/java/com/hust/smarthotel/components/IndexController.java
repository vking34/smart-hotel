package com.hust.smarthotel.components;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping(method = RequestMethod.GET)
    String getIndex(Model model){
//        return "index";
        return "admin";
    }

    @GetMapping("/signup")
    String getSignUp(Model model){
        return "signup";
    }

    @GetMapping("/admin")
    String getAdminPage(Model model){
        System.out.println("get admin page");
        return "admin";
    }
}
