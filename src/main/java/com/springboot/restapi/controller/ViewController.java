package com.springboot.restapi.controller;

import com.springboot.restapi.model.Employee;
import com.springboot.restapi.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/views")
public class ViewController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/load")
    public String loadEmployees() {
        employeeService.loadEmployees();
        return "redirect:/views/employees";
    }

    @GetMapping("/employees")
    public String getAllEmployees(Model model) {
        List<Employee> employees = employeeService.getStaticEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("employee", new Employee());
        return "employees";
    }

    @PostMapping("/employees")
    public String createEmployee(@ModelAttribute Employee employee) {
        employeeService.saveEmployee(employee);
        return "redirect:/views/employees";
    }
}
