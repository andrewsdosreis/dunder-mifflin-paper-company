package com.andrewsreis.dundermifflin.core.usecases;

import com.andrewsreis.dundermifflin.core.domain.Employee;

import java.util.List;

public interface FindEmployeeUseCase {
    List<Employee> findAll();

    Employee find(Long id);
}
