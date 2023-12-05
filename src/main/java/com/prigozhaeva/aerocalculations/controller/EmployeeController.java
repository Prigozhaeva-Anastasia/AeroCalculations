package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.entity.Employee;
import com.prigozhaeva.aerocalculations.entity.User;
import com.prigozhaeva.aerocalculations.service.EmployeeService;
import com.prigozhaeva.aerocalculations.service.RoleService;
import com.prigozhaeva.aerocalculations.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;

@Controller
@RequestMapping(value = "/employees")
public class EmployeeController {
    private EmployeeService employeeService;
    private UserService userService;
    private RoleService roleService;

    public EmployeeController(EmployeeService employeeService, UserService userService, RoleService roleService) {
        this.employeeService = employeeService;
        this.userService = userService;
        this.roleService = roleService;
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
    @GetMapping(value = "/formCreate")
    public String formEmployees(Model model) {
        model.addAttribute(USER, new User());
        model.addAttribute(LIST_ROLES, roleService.fetchAll());
        return "employee-views/formCreate";
    }

    @PostMapping(value = "/registration")
    public String registration(@Valid User user, BindingResult bindingResult) {
        User userDB = userService.findUserByEmail(user.getEmail());
        if (userDB != null) bindingResult.rejectValue("email", null, "Сотрудник с таким email уже был зарегистрирован");
        if (user.getRoles().size() == 0) bindingResult.rejectValue("roles", null, "Поле не должно быть пустым");
        if (bindingResult.hasErrors()) return "employee-views/formCreate";
        userService.createOrUpdateUser(user);
        return "redirect:/employees/index";
    }
}
