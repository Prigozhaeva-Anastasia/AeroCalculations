package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.entity.Aircraft;
import com.prigozhaeva.aerocalculations.entity.Employee;
import com.prigozhaeva.aerocalculations.entity.Role;
import com.prigozhaeva.aerocalculations.entity.User;
import com.prigozhaeva.aerocalculations.service.EmployeeService;
import com.prigozhaeva.aerocalculations.service.RoleService;
import com.prigozhaeva.aerocalculations.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

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
    @PreAuthorize("hasAuthority('Admin')")
    public String employees(Model model, @RequestParam(name = KEYWORD, defaultValue = "") String keyword) {
        List<Employee> employees = new CopyOnWriteArrayList<>(employeeService.findEmployeesByFioOrEmail(keyword));
        model.addAttribute(LIST_EMPLOYEES, employees);
        model.addAttribute(KEYWORD, keyword);
        return "employee-views/employees";
    }
    @GetMapping(value = "/delete")
    @PreAuthorize("hasAuthority('Admin')")
    public String deleteEmployee(Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return "redirect:/employees/index";
    }
    @GetMapping(value = "/formCreate")
    @PreAuthorize("hasAuthority('Admin')")
    public String formEmployees(Model model) {
        model.addAttribute(USER, new User());
        model.addAttribute(LIST_ROLES, roleService.fetchAll());
        return "employee-views/formCreate";
    }

    @PostMapping(value = "/registration")
    @PreAuthorize("hasAuthority('Admin')")
    public String registration(@Valid User user, BindingResult bindingResult) {
        User userDB = userService.findUserByEmail(user.getEmail());
        if (userDB != null) bindingResult.rejectValue("email", null, "Сотрудник с таким email уже был зарегистрирован");
        if (user.getRoles().size() == 0) bindingResult.rejectValue("roles", null, "Поле не должно быть пустым");
        if (bindingResult.hasErrors()) return "employee-views/formCreate";
        userService.createOrUpdateUser(user);
        return "redirect:/employees/index";
    }

    @GetMapping(value = "/sortByLastName")
    @PreAuthorize("hasAuthority('Admin')")
    public String sortByLastName(Model model) {
        List<Employee> employees = employeeService.fetchAll();
        Collections.sort(employees, Comparator.comparing(Employee::getLastName));
        model.addAttribute(LIST_EMPLOYEES, employees);
        return "employee-views/employees";
    }

    @PostMapping(value = "/filterByPosition")
    @PreAuthorize("hasAuthority('Admin')")
    public String filterByPosition(Model model, String position) {
        List<Employee> employees = employeeService.fetchAll().stream()
                .filter(element->element.getPosition().equals(position))
                .collect(Collectors.toList());
        model.addAttribute(LIST_EMPLOYEES, employees);
        return "employee-views/employees";
    }

    @GetMapping(value = "/personalData")
    @PreAuthorize("hasAnyAuthority('Admin', 'Accountant', 'Finance department employee')")
    public String personalData(Model model, Principal principal) {
        Employee employee = employeeService.findEmployeeByEmail(principal.getName());
        model.addAttribute(EMPLOYEE, employee);
        return "employee-views/personalData";
    }

    @GetMapping(value = "/formUpdate")
    @PreAuthorize("hasAnyAuthority('Admin', 'Accountant', 'Finance department employee')")
    public String updatePersonalData(Model model, Principal principal) {
        Employee employee = employeeService.findEmployeeByEmail(principal.getName());
        List<Role> roles = roleService.fetchAll();
        model.addAttribute("confirmation", "");
        model.addAttribute(EMPLOYEE, employee);
        model.addAttribute(LIST_ROLES, roles);
        return "employee-views/formUpdate";
    }

    @PostMapping(value = "/update")
    @PreAuthorize("hasAnyAuthority('Admin', 'Accountant', 'Finance department employee')")
    public String update(@ModelAttribute("employee") Employee employee, @RequestParam("confirmation") String confirmation) {
        employeeService.updatePersonalData(employee, confirmation);
        return "redirect:/employees/personalData";
    }
}
