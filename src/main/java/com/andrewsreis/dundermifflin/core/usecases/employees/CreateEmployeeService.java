package com.andrewsreis.dundermifflin.core.usecases.employees;

import com.andrewsreis.dundermifflin.core.dataproviders.EmployeeDataProvider;
import com.andrewsreis.dundermifflin.core.dataproviders.PhotoDataProvider;
import com.andrewsreis.dundermifflin.core.dataproviders.TriviaDataProvider;
import com.andrewsreis.dundermifflin.core.domain.Employee;
import com.andrewsreis.dundermifflin.core.domain.Photo;
import com.andrewsreis.dundermifflin.core.usecases.CreateEmployeeUseCase;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CreateEmployeeService implements CreateEmployeeUseCase {
    private final EmployeeDataProvider employeeDataProvider;
    private final PhotoDataProvider photoDataProvider;
    private final TriviaDataProvider triviaDataProvider;

    public CreateEmployeeService(EmployeeDataProvider employeeDataProvider, PhotoDataProvider photoDataProvider, TriviaDataProvider triviaDataProvider) {
        this.employeeDataProvider = employeeDataProvider;
        this.photoDataProvider = photoDataProvider;
        this.triviaDataProvider = triviaDataProvider;
    }

    @Override
    public Employee create(String firstName, String lastName, String department, MultipartFile photo) {
        Photo uploadedPhoto = photoDataProvider.uploadPhoto(photo, setFileName(firstName, lastName));
        String trivia = triviaDataProvider.getTrivia(firstName, lastName);
        Employee employeeToCreate = new Employee.Builder()
                .firstName(firstName)
                .lastName(lastName)
                .department(department)
                .photo(uploadedPhoto)
                .build();
        Employee createdEmployee = employeeDataProvider.save(employeeToCreate);
        return createdEmployee.withAggregatedData(uploadedPhoto, trivia, List.of());
    }

    private @NotNull String setFileName(String firstName, String lastName) {
        return firstName + "_" + lastName;
    }
}
