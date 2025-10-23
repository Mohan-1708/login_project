package com.login_project.Service;

import com.login_project.Entity.UserEntity;
import com.login_project.Repo.LoginRepo;
import org.springframework.context.annotation.Lazy; // <-- IMPORT THIS
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final LoginRepo loginRepo;
    private final PasswordEncoder passwordEncoder;

    // --- THIS IS THE FIX ---
    // By adding @Lazy, we tell Spring to create the PasswordEncoder bean later,
    // only when it's first used. This breaks the circular dependency at startup.
    public CustomUserDetailsService(LoginRepo loginRepo, @Lazy PasswordEncoder passwordEncoder) {
        this.loginRepo = loginRepo;
        this.passwordEncoder = passwordEncoder;
    }
    // -------------------------

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin@event.com".equals(username)) {
            String encodedAdminPassword = passwordEncoder.encode("Admin@11#");
            return new User(
                    "admin@event.com",
                    encodedAdminPassword,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        }

        UserEntity userEntity = loginRepo.findByEmailid(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userEntity.getRole());

        return new User(
                userEntity.getEmailid(),
                userEntity.getPassword(),
                Collections.singletonList(authority)
        );
    }
}

