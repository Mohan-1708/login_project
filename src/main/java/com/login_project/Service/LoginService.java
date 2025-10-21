package com.login_project.Service;

import com.login_project.Entity.UserEntity;
import com.login_project.Exception.RegistrationException;
import com.login_project.Repo.LoginRepo;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class LoginService {

    private final LoginRepo loginRepo;
    private final PasswordEncoder passwordEncoder;

    public LoginService(LoginRepo loginRepo, PasswordEncoder passwordEncoder) {
        this.loginRepo = loginRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String username, String password, String confirmPassword) {
        // ... all your existing validation logic ...
        if (userExists(username)) {
            throw new RegistrationException("An account with this email already exists.");
        }
        if (!password.equals(confirmPassword)) {
            throw new RegistrationException("Passwords do not match!");
        }
        if (password.length() < 8) {
            throw new RegistrationException("Password must be at least 8 characters long.");
        }

        boolean hasNumber = password.matches(".*[0-9].*");
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");

        if (!hasNumber || !hasSpecialChar) {
            throw new RegistrationException("Password must contain at least one number and one special character.");
        }

        UserEntity user = new UserEntity();
        user.setEmailid(username);
        user.setPassword(passwordEncoder.encode(password));

        // --- THIS IS THE UPDATE ---
        // Set the role for every new user to ROLE_USER by default.
        user.setRole("ROLE_USER");
        // ------------------------

        loginRepo.save(user);
    }

    public boolean userExists(String emailid) {
        return loginRepo.findByEmailid(emailid).isPresent();
    }
}

