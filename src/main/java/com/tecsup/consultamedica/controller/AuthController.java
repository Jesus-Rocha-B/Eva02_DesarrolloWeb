package com.tecsup.consultamedica.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/web/farmacia/dashboard";
        }
        return "login";
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "error/403";
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }
}
