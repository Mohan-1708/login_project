package com.login_project.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class HomeController {

    /**
     * This is a protected endpoint.
     * Spring Security, thanks to the JwtRequestFilter, will ensure that only
     * requests with a valid JWT can access it.
     * The 'Principal' object contains the authenticated user's details (in this case, the email).
     */
    @GetMapping("/welcome")
    public ResponseEntity<String> welcomeMessage(Principal principal) {
        // 'principal.getName()' will return the username (email) from the JWT
        return ResponseEntity.ok("Welcome, " + principal.getName() + "!");
    }
}