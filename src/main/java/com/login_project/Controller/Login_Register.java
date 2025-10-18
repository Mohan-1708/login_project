package com.login_project.Controller;


import com.login_project.Exception.RegistrationException;

import org.springframework.ui.Model;
import com.login_project.Service.LoginService;

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

    @PostMapping("/register") // Keep the full path here
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           Model model) {
        try {

            loginService.register(username, password, confirmPassword);
            System.out.println("Register successful");

            return "redirect:/api/login";
        } catch (RegistrationException e) {

            model.addAttribute("error", e.getMessage());

            return "register";
        }
    }


}
