package com.login_project.Controller;

import com.login_project.Entity.UserEntity;
import com.login_project.Repo.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final LoginRepo loginRepo;

    @Autowired
    public ProfileController(LoginRepo loginRepo) {
        this.loginRepo = loginRepo;
    }

    /**
     * Gets the current user's profile details.
     * We return a ProfileResponse DTO to avoid sending the password.
     */
    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(Principal principal) {
        UserEntity user = loginRepo.findByEmailid(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Use the DTO to send data safely
        ProfileResponse profileResponse = new ProfileResponse(user);
        return ResponseEntity.ok(profileResponse);
    }

    /**
     * Updates the current user's profile details.
     */
    @PutMapping
    public ResponseEntity<?> updateProfile(Principal principal, @RequestBody ProfileUpdateRequest request) {
        UserEntity user = loginRepo.findByEmailid(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Update all the fields from the request
        user.setName(request.getName());
        user.setMobileNumber(request.getMobileNumber());
        user.setCollege(request.getCollege());
        user.setCurrentYear(request.getCurrentYear());
        user.setCurrentSemester(request.getCurrentSemester());

        // Save the updated entity
        loginRepo.save(user);

        return ResponseEntity.ok(Map.of("message", "Profile updated successfully"));
    }
}