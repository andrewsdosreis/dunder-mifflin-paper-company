package com.andrewsreis.dundermifflin.services;

import com.andrewsreis.dundermifflin.database.entities.EmployeeEntity;
import com.andrewsreis.dundermifflin.database.repositories.EmployeeRepository;
import com.andrewsreis.dundermifflin.exception.EmployeeNotFoundException;
import com.andrewsreis.dundermifflin.mappers.EmployeeMapper;
import com.andrewsreis.dundermifflin.models.Employee;
import com.andrewsreis.dundermifflin.models.EmployeeQuotes;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PhotoService photoService;
    private final TriviaService triviaService;
    private final QuoteService quoteService;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            PhotoService photoService,
            TriviaService triviaService,
            QuoteService quoteService) {
        this.employeeRepository = employeeRepository;
        this.photoService = photoService;
        this.triviaService = triviaService;
        this.quoteService = quoteService;
    }

    public Employee getEmployeeById(Long id) {
        var employee = employeeRepository
                .findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return EmployeeMapper.toModel(
                employee,
                getEmployeeTrivia(employee.getFirstName(), employee.getLastName())
        );
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
                .map(employeeEntity ->
                        EmployeeMapper.toModel(
                                employeeEntity,
                                getEmployeeTrivia(employeeEntity.getFirstName(), employeeEntity.getLastName())
                        )
                )
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

        return EmployeeMapper.toModel(
                createdEmployee,
                getEmployeeTrivia(createdEmployee.getFirstName(), createdEmployee.getLastName())
        );
    }

    public void deleteEmployee(Long id) {
        var employee = employeeRepository
                .findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        photoService.deleteEmployeePhoto(employee.getPhoto());
        employeeRepository.delete(employee);
    }

    public EmployeeQuotes getEmployeeQuotes(Long id) {
        var employee = employeeRepository
                .findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return EmployeeMapper.toEmployeeQuotes(
                quoteService.findAllQuotesFromEmployee(employee.getFirstName(), employee.getLastName()),
                employee.getFirstName(),
                employee.getLastName()
        );
    }

    private @NotNull String buildFileName(String firstName, String lastName) {
        return firstName + "-" + lastName;
    }

    private String getEmployeeTrivia(String firstName, String lastName) {
        return triviaService.getTrivia(firstName + "_" + lastName);
    }
}
