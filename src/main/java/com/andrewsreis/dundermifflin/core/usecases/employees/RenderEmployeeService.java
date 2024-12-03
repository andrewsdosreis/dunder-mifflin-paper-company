package com.andrewsreis.dundermifflin.core.usecases.employees;

import com.andrewsreis.dundermifflin.core.dataproviders.EmployeeDataProvider;
import com.andrewsreis.dundermifflin.core.dataproviders.PhotoDataProvider;
import com.andrewsreis.dundermifflin.core.domain.Employee;
import com.andrewsreis.dundermifflin.core.domain.Photo;
import com.andrewsreis.dundermifflin.core.usecases.RenderEmployeePhotoUseCase;
import com.andrewsreis.dundermifflin.core.utils.ImageUtil;
import org.springframework.stereotype.Service;

@Service
public class RenderEmployeeService implements RenderEmployeePhotoUseCase {
    private final EmployeeDataProvider employeeDataProvider;
    private final PhotoDataProvider photoDataProvider;

    public RenderEmployeeService(EmployeeDataProvider employeeDataProvider, PhotoDataProvider photoDataProvider) {
        this.employeeDataProvider = employeeDataProvider;
        this.photoDataProvider = photoDataProvider;
    }

    @Override
    public String render(Long id) {
        Employee employee = employeeDataProvider.findById(id);
        Photo photo = photoDataProvider.downloadPhoto(employee.photo().key());
        return ImageUtil.beautify(photo.image());
    }
}
