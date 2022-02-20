package com.yordle.usersvc.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Get users from DB
        // pass as follows, username, password and a collection of authorities (user roles)
        // need to return my users in Spring security's User class format
        return new User("foo", "foo", new ArrayList<>());
    }

}
