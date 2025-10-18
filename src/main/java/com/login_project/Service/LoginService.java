package com.login_project.Service;

import com.login_project.Entity.UserEntity;
import com.login_project.Exception.RegistrationException;
import com.login_project.Repo.LoginRepo;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@Service
public class LoginService {

    private final LoginRepo loginRepo;
    private final  PasswordEncoder passwordEncoder;
    public LoginService(LoginRepo loginRepo , PasswordEncoder passwordEncoder) {
        this.loginRepo = loginRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean login(String username, String password) {

        Optional<UserEntity> obj = loginRepo.findByEmailid(username);
        if(obj.isPresent()) {
            UserEntity user = obj.get();
            if(user.getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }



    public void register(String username, String password, String confirmPassword) {
        // --- ALL VALIDATION LOGIC IS NOW HERE ---

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

        // --- If all checks pass, proceed to save ---
        UserEntity user = new UserEntity();
        user.setEmailid(username);
        user.setPassword(passwordEncoder.encode(password)); // Hashing is done here!
        System.out.println(user);
        loginRepo.save(user);
    }

    public boolean userExists(String emailid) {
        return loginRepo.findByEmailid(emailid).isPresent();
    }



}
