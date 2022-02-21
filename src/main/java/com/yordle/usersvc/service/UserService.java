package com.yordle.usersvc.service;

import com.yordle.usersvc.model.User;
import com.yordle.usersvc.repository.UserRepository;
import com.yordle.usersvc.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService implements UserServiceInterface{
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository, UserDetailsService userDetailsService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String createUser(User user) throws ResponseStatusException {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if(existingUser != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User credentials invalid");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.insert(user);
        final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(user.getUsername());
        return jwtUtil.generateToken(userDetails);
    }

    @Override
    public String authenticateUser(String username, String password) throws ResponseStatusException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect username or password");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtUtil.generateToken(userDetails);
    }

    @Override
    public String getUsername(String authHeader) throws ResponseStatusException {
        if(!authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
        }
        return jwtUtil.extractUsername(authHeader.substring(7));
    }
}
