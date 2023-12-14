package com.prigozhaeva.aerocalculations.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthorizationController {
    @PostMapping(value = "/login")
    public String authorized() {
        return "main-views/main";
    }
    @GetMapping(value = "/login")
    public String login() {
        return "authorization";
    }
}
