package com.andrewsreis.dundermifflin.app.dataproviders.employee;

import com.andrewsreis.dundermifflin.app.exception.EmployeeNotFoundException;
import com.andrewsreis.dundermifflin.core.domain.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeDataProvider implements com.andrewsreis.dundermifflin.core.dataproviders.EmployeeDataProvider {
    private final EmployeeJpaRepository repository;

    public EmployeeDataProvider(EmployeeJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Employee findById(Long id) {
        var employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
        return EmployeeMapper.toDomain(employee);
    }

    @Override
    public Employee findByName(String firstName, String lastName) {
        var employee = repository.findByFirstNameAndLastName(firstName, lastName).orElseThrow(() -> new EmployeeNotFoundException(firstName, lastName));
        return EmployeeMapper.toDomain(employee);
    }

    @Override
    public List<Employee> findAll() {
        var employees = repository.findAll();
        return EmployeeMapper.toDomain(employees);
    }

    @Override
    public Employee save(Employee employee) {
        var employeeToCreate = EmployeeMapper.toEntity(employee);
        var employeeCreated = repository.save(employeeToCreate);
        return EmployeeMapper.toDomain(employeeCreated);
    }

    @Override
    public void delete(Employee employee) {
        var employeeToDelete = EmployeeMapper.toEntity(employee);
        repository.delete(employeeToDelete);
    }
}
