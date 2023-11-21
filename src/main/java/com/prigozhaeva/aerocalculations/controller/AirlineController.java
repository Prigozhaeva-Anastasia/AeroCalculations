package com.prigozhaeva.aerocalculations.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.http.HttpRequest;

@Controller
@RequestMapping(value = "/airlines")
public class AirlineController {
    public AirlineController() {
    }
    @GetMapping(value = "/index")
    public String airlines() {
        return "airline-views/airlines";
    }
}
