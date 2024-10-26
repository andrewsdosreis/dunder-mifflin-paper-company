package com.andrewsreis.dundermifflin.controllers;

import com.andrewsreis.dundermifflin.entities.EmployeeEntity;
import com.andrewsreis.dundermifflin.repositories.EmployeeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeesController {

    private final EmployeeRepository employeeRepository;

    public EmployeesController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public List<EmployeeEntity> getEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public EmployeeEntity getEmployee(@PathVariable Long id) {
        return employeeRepository.findById(id).orElse(null);
    }
}
