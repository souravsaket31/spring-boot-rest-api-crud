package com.springboot.restapi.service;

import com.springboot.restapi.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class EmployeeService {



    private List<Employee> staticEmployees = new ArrayList<>();

    public List<Employee> loadEmployees() {
        IntStream.range(0, 100).forEach(i -> {
            staticEmployees.add(new Employee(i, "FirstName-" + i, "LastName-" + i, "email" + i + "@example.com"));
        });
        return staticEmployees;
    }

    public List<Employee> getStaticEmployees() {
        return staticEmployees;
    }

    public void saveEmployee(Employee employee) {
        staticEmployees.add(employee);
    }
}