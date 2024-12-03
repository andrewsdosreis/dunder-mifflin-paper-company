package com.andrewsreis.dundermifflin.app.entrypoints.employees;

import com.andrewsreis.dundermifflin.app.entrypoints.employees.mappers.EmployeeMapper;
import com.andrewsreis.dundermifflin.app.entrypoints.employees.models.EmployeeResponse;
import com.andrewsreis.dundermifflin.core.domain.Employee;
import com.andrewsreis.dundermifflin.core.usecases.CreateEmployeeUseCase;
import com.andrewsreis.dundermifflin.core.usecases.DeleteEmployeeUseCase;
import com.andrewsreis.dundermifflin.core.usecases.FindEmployeeUseCase;
import com.andrewsreis.dundermifflin.core.usecases.RenderEmployeePhotoUseCase;
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

    private final CreateEmployeeUseCase createEmployeeUseCase;
    private final FindEmployeeUseCase findEmployeeUseCase;
    private final DeleteEmployeeUseCase deleteEmployeeUseCase;
    private final RenderEmployeePhotoUseCase renderEmployeePhotoUseCase;

    public EmployeesController(CreateEmployeeUseCase createEmployeeUseCase, FindEmployeeUseCase findEmployeeUseCase, DeleteEmployeeUseCase deleteEmployeeUseCase, RenderEmployeePhotoUseCase renderEmployeePhotoUseCase) {
        this.createEmployeeUseCase = createEmployeeUseCase;
        this.findEmployeeUseCase = findEmployeeUseCase;
        this.deleteEmployeeUseCase = deleteEmployeeUseCase;
        this.renderEmployeePhotoUseCase = renderEmployeePhotoUseCase;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<EmployeeResponse>> getEmployees() {
        LOGGER.info("Fetching all employees");
        var response = EmployeeMapper.toResponse(findEmployeeUseCase.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EmployeeResponse> getEmployee(@PathVariable Long id) {
        LOGGER.info("Fetching employee with id: {}", id);
        var response = EmployeeMapper.toResponse(findEmployeeUseCase.find(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}/photo", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> renderEmployeePhoto(@PathVariable Long id) {
        LOGGER.info("Rendering photo for employee with id: {}", id);
        return ResponseEntity.ok(renderEmployeePhotoUseCase.render(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EmployeeResponse> createEmployee(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String department,
            @RequestPart("photo") MultipartFile photo
    ) {
        LOGGER.info("Creating employee with name: {} {}", firstName, lastName);
        Employee employee = createEmployeeUseCase.create(firstName, lastName, department, photo);
        var response = EmployeeMapper.toResponse(employee);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        LOGGER.info("Deleting employee with id: {}", id);
        deleteEmployeeUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
