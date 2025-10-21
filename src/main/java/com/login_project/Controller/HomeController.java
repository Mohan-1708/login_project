package com.login_project.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class HomeController {

    /**
     * This endpoint now returns both the username and a list of the user's roles.
     * The dashboard script will use this to determine which view to render.
     */
    @GetMapping("/user-details")
    public ResponseEntity<?> getUserDetails(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(Map.of("error", "User not authenticated"));
        }

        // The Principal object is actually an Authentication object in Spring Security.
        // We can cast it to get access to the user's authorities (roles).
        org.springframework.security.core.Authentication authentication =
                (org.springframework.security.core.Authentication) principal;

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Return a JSON object containing the username and roles.
        return ResponseEntity.ok(Map.of(
                "username", principal.getName(),
                "roles", roles
        ));
    }
}

