package com.andrewsreis.dundermifflin.controllers;

import com.andrewsreis.dundermifflin.models.Employee;
import com.andrewsreis.dundermifflin.models.EmployeeQuotes;
import com.andrewsreis.dundermifflin.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeesController.class);

    private final EmployeeService employeeService;

    public EmployeesController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getEmployees() {
        LOGGER.info("Fetching all employees");
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        LOGGER.info("Fetching employee with id: {}", id);
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping(value = "/{id}/photo", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> renderEmployeePhoto(@PathVariable Long id) {
        LOGGER.info("Rendering photo for employee with id: {}", id);
        return ResponseEntity.ok(employeeService.getEmployeePhotoById(id));
    }

    @GetMapping(value = "/{id}/quotes")
    public ResponseEntity<EmployeeQuotes> getEmployeeQuotes(@PathVariable Long id) {
        LOGGER.info("Fetching quotes for employee with id: {}", id);
        return ResponseEntity.ok(employeeService.getEmployeeQuotes(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Employee> createEmployee(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String department,
            @RequestPart("photo") MultipartFile photo
    ) {
        LOGGER.info("Creating employee with name: {} {}", firstName, lastName);
        Employee employee = employeeService.createEmployee(firstName, lastName, department, photo);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        LOGGER.info("Deleting employee with id: {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
