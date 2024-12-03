package com.andrewsreis.dundermifflin.core.usecases.employees;

import com.andrewsreis.dundermifflin.core.dataproviders.EmployeeDataProvider;
import com.andrewsreis.dundermifflin.core.dataproviders.PhotoDataProvider;
import com.andrewsreis.dundermifflin.core.usecases.DeleteEmployeeUseCase;
import org.springframework.stereotype.Service;

@Service
public class DeleteEmployeeService implements DeleteEmployeeUseCase {
    private final EmployeeDataProvider employeeDataProvider;
    private final PhotoDataProvider photoDataProvider;

    public DeleteEmployeeService(EmployeeDataProvider employeeDataProvider, PhotoDataProvider photoDataProvider) {
        this.employeeDataProvider = employeeDataProvider;
        this.photoDataProvider = photoDataProvider;
    }

    @Override
    public void delete(Long id) {
        var employee = employeeDataProvider.findById(id);
        photoDataProvider.deletePhoto(employee.photo().key());
        employeeDataProvider.delete(employee);
    }
}
