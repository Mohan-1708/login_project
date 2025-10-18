package com.login_project.Controller;

import com.login_project.Entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import com.login_project.Service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api")
public class Login_Register {

    private LoginService loginService;
    public Login_Register(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {

        if(loginService.login(username, password)) {
            return "success";
        }
        else {
            return "failure";
        }
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String confirmPassword, // 1. Add 'confirmPassword' parameter
                           Model model) {

        // 2. Check if passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            return "register"; // Return to register.html page
        }

        // 3. CHECK IF USER ALREADY EXISTS (This is the main fix)
        if (loginService.userExists(username)) {
            model.addAttribute("error", "An account with this email already exists.");
            return "register"; // Return to register.html page
        }

        // --- If all checks pass, proceed to save ---

        UserEntity user = new UserEntity();
        user.setEmailid(username);

        // 4. (Security Warning) You must HASH the password before saving!
        // user.setPassword(passwordEncoder.encode(password));
        user.setPassword(password); // Using plain text for now, but please fix this.

        loginService.register(user);

        // 5. Redirect to login on success
        return "redirect:/api/login";
    }
}
