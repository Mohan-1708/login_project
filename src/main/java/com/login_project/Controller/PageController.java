package com.login_project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class PageController {
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Renders login.html
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // Renders register.html
    }

    // This could be a protected page you see after successful login
    @GetMapping("/home")
    public String homePage() {
        return "success"; // Renders success.html for now
    }
}