package com.prigozhaeva.aerocalculations.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Calendar;

import static com.prigozhaeva.aerocalculations.constant.Constant.YEAR;

@Controller
public class MainController {
    @GetMapping(value = "/")
    @PreAuthorize("hasAnyAuthority('Admin', 'Accountant', 'Finance department employee')")
    public String main(Model model) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        model.addAttribute(YEAR, year);
        return "main-views/main";
    }
}
