package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.entity.Airline;
import com.prigozhaeva.aerocalculations.entity.Employee;
import com.prigozhaeva.aerocalculations.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;

@Controller
@RequestMapping(value = "/employees")
public class EmployeeController {
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/index")
    public String employees(Model model, @RequestParam(name = KEYWORD, defaultValue = "") String keyword) {
        List<Employee> employees = new CopyOnWriteArrayList<>(employeeService.findEmployeesByFioOrEmail(keyword));
        model.addAttribute(LIST_EMPLOYEES, employees);
        model.addAttribute(KEYWORD, keyword);
        return "employee-views/employees";
    }

    @GetMapping(value = "/delete")
    public String deleteEmployee(Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return "redirect:/employees/index";
    }
}
