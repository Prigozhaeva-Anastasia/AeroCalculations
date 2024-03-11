package com.prigozhaeva.aerocalculations.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/rushHours")
public class RushHourController {
    @GetMapping(value = "/index")
    public String rushHours(Model model, String weekDay) {

        return "rushHour-views/rushHours";
    }
}
