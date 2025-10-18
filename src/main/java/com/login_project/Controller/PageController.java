package com.login_project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class PageController {
    @GetMapping("/success")
    public String successPage() {
        return "success"; // Renders success.html
    }

    @GetMapping("/failure")
    public String failurePage() {
        return "failure"; // Renders failure.html
    }
}