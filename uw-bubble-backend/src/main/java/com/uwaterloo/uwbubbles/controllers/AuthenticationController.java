package com.uwaterloo.uwbubbles.controllers;

import com.uwaterloo.uwbubbles.config.JwtTokenUtil;
import com.uwaterloo.uwbubbles.config.JwtUserDetailsService;
import com.uwaterloo.uwbubbles.dto.JwtRequest;
import com.uwaterloo.uwbubbles.dto.JwtResponse;
import com.uwaterloo.uwbubbles.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping("/users/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {

        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        }
        catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is disabled");
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid User Credentials");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateSessionToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) {
        if (!userService.usernameExist(username)) {
            throw new BadCredentialsException("User does not exist");
        }
        else if (!userService.enabled(username)) {
            throw new DisabledException("User is disabled");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @GetMapping("/users/verify-account/{token}")
    public ResponseEntity<?> createAuthenticationToken(@PathVariable("token") String token) {
        String username;
        try {
            username = jwtTokenUtil.getUsernameFromToken(token);
        } catch (MalformedJwtException | IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid verification token", HttpStatus.BAD_REQUEST);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Token has expired, sign up again to receive a new verification link", HttpStatus.BAD_REQUEST);
        }

        if (userService.enabled(username)) {
            return new ResponseEntity<>("You have already verified your account", HttpStatus.BAD_REQUEST);
        }
        else {
            userService.enableUser(username);
            return new ResponseEntity<>("You have verified your account, now you can login", HttpStatus.OK);
        }
    }
}
