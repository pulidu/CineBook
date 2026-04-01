package lk.ijse.cinebook.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint - No authentication needed";
    }

    @GetMapping("/customer")
    public String customerEndpoint() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return "Hello Customer: " + email + "! You have access to customer endpoints";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return "Hello Admin: " + email + "! You have access to admin endpoints";
    }
}