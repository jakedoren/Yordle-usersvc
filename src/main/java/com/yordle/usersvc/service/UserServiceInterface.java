package com.yordle.usersvc.service;

import com.yordle.usersvc.model.User;

public interface UserServiceInterface {
    String createUser(User user);
    String authenticateUser(String username, String password);
    String getUsername(String authHeader);
}
