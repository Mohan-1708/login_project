package com.login_project.Controller;


import com.login_project.Exception.RegistrationException;

import org.springframework.ui.Model;
import com.login_project.Service.LoginService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api")
public class Login_Register {

    private LoginService loginService;
    public Login_Register(LoginService loginService) {
        this.loginService = loginService;
    }




    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           Model model,
                           RedirectAttributes redirectAttributes) { // 2. Add it to the method arguments
        try {
            System.out.println(username);
            System.out.println(password);
            loginService.register(username, password, confirmPassword);
            System.out.println("Register successful");

            // 3. Add the success message as a "flash attribute"
            // This attribute will survive the redirect.
            redirectAttributes.addFlashAttribute("successMessage", "Registration completed!");

            return "redirect:/login";
        } catch (RegistrationException e) {
            model.addAttribute("error", e.getMessage());
            System.out.println(e.getMessage());
            return "register";
        }
    }


}
