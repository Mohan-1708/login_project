package com.login_project.Service;

import com.login_project.Entity.UserEntity;
import com.login_project.Repo.LoginRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final LoginRepo loginRepo;

    public CustomUserDetailsService(LoginRepo loginRepo) {
        this.loginRepo = loginRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = loginRepo.findByEmailid(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));


        return new User(userEntity.getEmailid(), userEntity.getPassword(), new ArrayList<>());
    }
}
