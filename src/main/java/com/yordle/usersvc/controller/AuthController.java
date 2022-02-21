package com.yordle.usersvc.controller;

import com.yordle.usersvc.model.AuthenticationRequest;
import com.yordle.usersvc.model.AuthenticationResponse;
import com.yordle.usersvc.model.User;
import com.yordle.usersvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<String> getUsername(@RequestHeader Map<String,String> headers) {
        String username = userService.getUsername(headers.get("authorization"));
        return ResponseEntity.ok(username);
    }

    @PostMapping("/create")
    public ResponseEntity<AuthenticationResponse> createUser(@RequestBody User user) {
        String token = userService.createUser(user);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        String token = userService.authenticateUser(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
