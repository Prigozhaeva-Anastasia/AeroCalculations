//package com.prigozhaeva.aerocalculations.runner;
//
//import com.prigozhaeva.aerocalculations.entity.Employee;
//import com.prigozhaeva.aerocalculations.entity.User;
//import com.prigozhaeva.aerocalculations.service.EmployeeService;
//import com.prigozhaeva.aerocalculations.service.RoleService;
//import com.prigozhaeva.aerocalculations.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//
//@Component
//public class MyRunner implements CommandLineRunner {
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private RoleService roleService;
//    @Autowired
//    private EmployeeService employeeService;
//
//    public static final String ADMIN = "Администратор";
//    public static final String ACCOUNTANT = "Бухгалтер";
//    public static final String FINANCE = "Сотрудник финансового отдела";
//    @Override
//    public void run(String... args) throws Exception {
//
//        roleService.createRole(ADMIN);
//        roleService.createRole(ACCOUNTANT);
//        roleService.createRole(FINANCE);
//
//        User user1 = userService.createUser("astapovich@gmail.com", "AdminA1111", ADMIN);
//        User user2 = userService.createUser( "bondarenko@gmail.com", "margoPR2003", ACCOUNTANT);
//        User user3 = userService.createUser( "gromov@gmail.com", "olgGR2003", FINANCE);
//
//        Employee employee1 = employeeService.createEmployee(user1.getId(), "Астапович", "Александра", "Владимировна", "+375448965214", "cистемный администратор", "/images/employees/Astapovich Aleksandra.jpg");
//        Employee employee2 = employeeService.createEmployee(user2.getId(), "Бондаренко", "Маргарита", "Александровна", "+375293514265", "бухгалтер", "/images/employees/Bondarenko Margarita.jpg");
//        Employee employee3 = employeeService.createEmployee(user3.getId(), "Громов", "Олег", "Андреевич", "+375335742145", "сотрудник финансового отдела", "/images/employees/default.jpg");
//    }
//}
