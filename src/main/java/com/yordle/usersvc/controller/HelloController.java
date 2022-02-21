package com.yordle.usersvc.controller;

import com.yordle.usersvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {
    private final UserService userService;

    @Autowired
    public HelloController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/hello")
    public String hello(@RequestHeader Map<String,String> headers) {
        String username = userService.getUsername(headers.get("authorization"));
        return "Hello, " + username;
    }
}
