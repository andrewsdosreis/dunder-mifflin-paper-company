package com.andrewsreis.dundermifflin.controller;

import com.andrewsreis.dundermifflin.configuration.AwsConfiguration;
import com.andrewsreis.dundermifflin.configuration.CacheConfiguration;
import com.andrewsreis.dundermifflin.configuration.DatabaseConfiguration;
import com.andrewsreis.dundermifflin.configuration.TestContainersConfiguration;
import com.andrewsreis.dundermifflin.models.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Import({TestContainersConfiguration.class, AwsConfiguration.class, DatabaseConfiguration.class, CacheConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeesControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateAndRetrieveEmployee() {
        // Create Employee
        HttpEntity<?> requestEntity = createEmployeeRequestEntity();
        String EMPLOYEE_URI = "/employees";

        ResponseEntity<Employee> responseEntity = restTemplate.postForEntity(EMPLOYEE_URI, requestEntity, Employee.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Employee createdEmployee = responseEntity.getBody();
        assertThat(createdEmployee).isNotNull();
        assertThat(createdEmployee.firstName()).isEqualTo("Michael");

        // Retrieve Employee
        ResponseEntity<Employee> getResponse = restTemplate.getForEntity(EMPLOYEE_URI + "/" + createdEmployee.id(), Employee.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().firstName()).isEqualTo("Michael");
    }

    @Test
    void shouldReturnNotFoundForNonexistentEmployee() {
        ResponseEntity<String> response = restTemplate.getForEntity("/employees/999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Employee not found");
    }

    private HttpEntity<?> createEmployeeRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("firstName", "Michael");
        body.add("lastName", "Scott");
        body.add("department", "Manager");

        ClassPathResource photoResource = new ClassPathResource("michael.png");
        body.add("photo", photoResource);

        return new HttpEntity<>(body, headers);
    }
}
