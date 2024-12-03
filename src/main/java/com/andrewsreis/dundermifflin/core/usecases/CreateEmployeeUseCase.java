package com.andrewsreis.dundermifflin.core.usecases;

import com.andrewsreis.dundermifflin.core.domain.Employee;
import org.springframework.web.multipart.MultipartFile;

public interface CreateEmployeeUseCase {
    Employee create(String firstName, String lastName, String department, MultipartFile photo);
}
