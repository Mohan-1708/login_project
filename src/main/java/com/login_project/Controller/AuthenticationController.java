package com.login_project.Controller;

import com.login_project.Config.JwtUtil;

import com.login_project.Service.CustomUserDetailsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) {
        try {
            // 1. Authenticate username and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            // This is correct, keep it
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Incorrect username or password"));
        }

        // 2. Load the user
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        // 3. --- THIS IS THE NEW LOGIC ---
        // Check if the authenticated user is an admin
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            // If they are an admin, reject them from this (user) login endpoint.
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Access Denied: Please use the admin login portal."));
        }
        // --------------------------------

        // 4. If they are a normal user, generate token and set cookie
        final String jwt = jwtUtil.generateToken(userDetails);

        Cookie jwtCookie = new Cookie("jwtToken", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60*5);
        response.addCookie(jwtCookie);

        return ResponseEntity.ok(Map.of("message", "Authentication Successful"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Create a cookie that instantly expires to clear the existing one
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0); // This effectively deletes the cookie

        response.addCookie(cookie);
        return ResponseEntity.ok("Logout successful");
    }
    // In com.login_project.Controller.AuthenticationController

// (You already have the @Autowired fields and the /api/login and /api/logout methods)

    @PostMapping("/admin/login")
    public ResponseEntity<?> createAdminAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) {
        try {
            // 1. Authenticate username and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Incorrect username or password"));
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        // 2. --- NEW ADMIN CHECK ---
        // Check if the authenticated user actually has the 'ROLE_ADMIN'
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            // If they are a valid user but not an admin, reject them.
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Access Denied: This is an admin-only login."));
        }
        // -------------------------

        // 3. If they are an admin, generate token and set cookie
        final String jwt = jwtUtil.generateToken(userDetails);

        Cookie jwtCookie = new Cookie("jwtToken", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60*5); // 5 minutes
        response.addCookie(jwtCookie);

        return ResponseEntity.ok(Map.of("message", "Admin Authentication Successful"));
    }
}
