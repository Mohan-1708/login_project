package com.login_project.Service;

import com.login_project.Entity.UserEntity;
import com.login_project.Repo.LoginRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private LoginRepo loginRepo;
    public LoginService(LoginRepo loginRepo) {
        this.loginRepo = loginRepo;
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

    public boolean userExists(String emailid) {

        return loginRepo.findByEmailid(emailid).isPresent();
    }

    // Your existing register method
    public void register(UserEntity user) {
        // ... hashing logic should go here ...
        loginRepo.save(user);
    }
}
