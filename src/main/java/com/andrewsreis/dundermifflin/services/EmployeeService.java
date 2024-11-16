package com.andrewsreis.dundermifflin.services;

import com.andrewsreis.dundermifflin.entities.EmployeeEntity;
import com.andrewsreis.dundermifflin.exception.EmployeeNotFoundException;
import com.andrewsreis.dundermifflin.mappers.EmployeeMapper;
import com.andrewsreis.dundermifflin.models.Employee;
import com.andrewsreis.dundermifflin.repositories.EmployeeRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PhotoService photoService;

    public EmployeeService(EmployeeRepository employeeRepository, PhotoService photoService) {
        this.employeeRepository = employeeRepository;
        this.photoService = photoService;
    }

    public Employee getEmployeeById(Long id) {
        var employee = employeeRepository
                .findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return EmployeeMapper.toModel(employee);
    }

    public String getEmployeePhotoById(Long id) {
        var employee = employeeRepository
                .findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return EmployeeMapper.toEmployeePhoto(photoService.downloadEmployeePhoto(employee.getPhoto()));
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository
                .findAll()
                .stream()
                .map(EmployeeMapper::toModel)
                .toList();
    }

    public Employee createEmployee(String firstName, String lastName, String department, MultipartFile photo) {
        String fileName = buildFileName(firstName, lastName);
        String key = photoService.uploadEmployeePhoto(photo, fileName);
        var createdEmployee = new EmployeeEntity.Builder()
                .firstName(firstName)
                .lastName(lastName)
                .department(department)
                .photo(key)
                .build();

        createdEmployee = employeeRepository.save(createdEmployee);
        return EmployeeMapper.toModel(createdEmployee);
    }

    public void deleteEmployee(Long id) {
        var employee = employeeRepository
                .findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        photoService.deleteEmployeePhoto(employee.getPhoto());
        employeeRepository.delete(employee);
    }

    private @NotNull String buildFileName(String firstName, String lastName) {
        return firstName + "-" + lastName;
    }
}
