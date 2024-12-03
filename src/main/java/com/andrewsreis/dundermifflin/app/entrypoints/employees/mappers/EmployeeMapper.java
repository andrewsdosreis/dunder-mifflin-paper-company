package com.andrewsreis.dundermifflin.app.entrypoints.employees.mappers;

import com.andrewsreis.dundermifflin.app.entrypoints.employees.models.EmployeeResponse;
import com.andrewsreis.dundermifflin.core.domain.Employee;
import com.andrewsreis.dundermifflin.core.domain.Quote;

import java.util.List;

public class EmployeeMapper {

    public static EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.id(),
                employee.firstName(),
                employee.lastName(),
                employee.department(),
                employee.photo().key(),
                employee.trivia(),
                employee.quotes().stream().map(Quote::quote).toList()
        );
    }

    public static List<EmployeeResponse> toResponse(List<Employee> employees) {
        return employees.stream().map(EmployeeMapper::toResponse).toList();
    }
}
