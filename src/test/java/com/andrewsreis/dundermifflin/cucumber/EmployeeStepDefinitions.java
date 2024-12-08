package com.andrewsreis.dundermifflin.cucumber;

import com.andrewsreis.dundermifflin.app.dataproviders.employee.EmployeeEntity;
import com.andrewsreis.dundermifflin.app.dataproviders.employee.EmployeeJpaRepository;
import com.andrewsreis.dundermifflin.app.dataproviders.photo.PhotoDataProvider;
import com.andrewsreis.dundermifflin.app.entrypoints.employees.models.EmployeeResponse;
import com.andrewsreis.dundermifflin.core.domain.Photo;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class EmployeeStepDefinitions {

    private final EmployeeJpaRepository employeeJpaRepository;
    private final TestRestTemplate restTemplate;
    private final PhotoDataProvider photoDataProvider;

    @Autowired
    public EmployeeStepDefinitions(EmployeeJpaRepository employeeJpaRepository, TestRestTemplate restTemplate, PhotoDataProvider photoDataProvider) {
        this.employeeJpaRepository = employeeJpaRepository;
        this.restTemplate = restTemplate;
        this.photoDataProvider = photoDataProvider;
    }

    private ResponseEntity<EmployeeResponse> latestResponse;
    private EmployeeResponse latestResponseBody;

    @Given("the system is initialized with no employees")
    public void systemInitializedWithNoEmployees() {
        employeeJpaRepository.deleteAll();
        Assertions.assertEquals(0, employeeJpaRepository.count());
    }

    @Given("an employee with firstName {string} lastName {string} department {string} and photo {string} exists")
    @When("I create an employee with firstName {string} lastName {string} department {string} and photo {string}")
    public void iCreateAnEmployee(String firstName, String lastName, String department, String photoFilename) throws IOException {
        byte[] photoBytes = loadTestPhoto(photoFilename);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("firstName", firstName);
        body.add("lastName", lastName);
        body.add("department", department);
        body.add("photo", new ByteArrayResourceWithFilename(photoBytes, photoFilename));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Execute POST employee API to create a new customer
        latestResponse = restTemplate.exchange("/employees", HttpMethod.POST, requestEntity, EmployeeResponse.class);
        latestResponseBody = latestResponse.getBody();

        Assertions.assertEquals(HttpStatus.CREATED, latestResponse.getStatusCode());
        Assertions.assertNotNull(latestResponseBody);
    }

    @Then("the employee should be persisted in the database with firstName {string} lastName {string} department {string} and photo {string}")
    @Then("I should see the employee details with firstName {string} lastName {string} department {string} and photo {string}")
    public void theEmployeeShouldBePersistedInDb(String expectedFirstName, String expectedLastName, String expectedDepartment, String expectedPhotoFilename) {
        EmployeeEntity employee = employeeJpaRepository.findById(latestResponseBody.id()).orElse(null);

        Assertions.assertNotNull(employee, "No createdEmployee found");
        Assertions.assertEquals(expectedFirstName, employee.getFirstName());
        Assertions.assertEquals(expectedLastName, employee.getLastName());
        Assertions.assertEquals(expectedDepartment, employee.getDepartment());
        Assertions.assertEquals(expectedPhotoFilename, employee.getPhoto());
    }

    @When("I retrieve the employee by id")
    @Then("I can retrieve the employee by id")
    public void iCanRetrieveTheEmployeeById() {
        Long employeeId = Objects.requireNonNull(latestResponse.getBody()).id();
        latestResponse = restTemplate.getForEntity("/employees/" + employeeId, EmployeeResponse.class);
        latestResponseBody = latestResponse.getBody();

        Assertions.assertEquals(HttpStatus.OK, latestResponse.getStatusCode());
        Assertions.assertNotNull(latestResponseBody);
    }

    @Then("the employee's trivia should be available")
    public void theEmployeeTriviaShouldBeAvailable() {
        Assertions.assertFalse(latestResponseBody.trivia().isEmpty(), "No trivia found in response");
        Assertions.assertEquals(latestResponseBody.trivia(), loadTestTrivia(latestResponseBody.firstName()));
    }

    @Then("the employee's photo should be accessible in S3")
    public void theEmployeePhotoShouldBeAccessibleInS3() {
        Photo photo = photoDataProvider.downloadPhoto(latestResponseBody.photoLocation());

        Assertions.assertNotNull(photo.image(), "Photo image data should not be null");
    }

    // ----- Helper methods -----
    private byte[] loadTestPhoto(String photoFilename) throws IOException {
        try (InputStream in = getClass().getResourceAsStream("/static/" + photoFilename)) {
            if (in == null) {
                throw new IOException("Resource not found: " + photoFilename);
            }
            return in.readAllBytes();
        }
    }

    private String loadTestTrivia(String firstName) {
        return switch (firstName) {
            case "Jim" -> "Jim has appeared in every single episode of The Office with the exception of \"Mafia,\" in which only his voice can be heard.";
            case "Michael" -> "His role models are Bob Hope, Abraham Lincoln, Bono, and God. Michael lost his virginity when he was 28. His signature saying is \"That's what she said\". Michael is almost incapable of keeping a secret. His deepest fear is being alone.";
            default -> null;
        };
    }

    // Custom Resource for multipart upload.
    private static class ByteArrayResourceWithFilename extends org.springframework.core.io.ByteArrayResource {
        private final String filename;

        public ByteArrayResourceWithFilename(byte[] byteArray, String filename) {
            super(byteArray);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return filename;
        }
    }
}
