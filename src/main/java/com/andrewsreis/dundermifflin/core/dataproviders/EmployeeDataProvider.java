package com.andrewsreis.dundermifflin.core.dataproviders;

import com.andrewsreis.dundermifflin.core.domain.Employee;

import java.util.List;

public interface EmployeeDataProvider {
    List<Employee> findAll();

    Employee findById(Long id);

    Employee findByName(String firstName, String lastName);

    Employee save(Employee employee);

    void delete(Employee employee);
}
