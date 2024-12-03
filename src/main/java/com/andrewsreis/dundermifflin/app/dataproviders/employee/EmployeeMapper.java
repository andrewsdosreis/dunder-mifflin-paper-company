package com.andrewsreis.dundermifflin.app.dataproviders.employee;

import com.andrewsreis.dundermifflin.core.domain.Employee;
import com.andrewsreis.dundermifflin.core.domain.Photo;

import java.util.List;

public abstract class EmployeeMapper {

    public static Employee toDomain(EmployeeEntity entity) {
        return new Employee.Builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .department(entity.getDepartment())
                .photo(new Photo(entity.getPhoto(), null))
                .build();
    }

    public static List<Employee> toDomain(List<EmployeeEntity> entities) {
        return entities.stream().map(EmployeeMapper::toDomain).toList();
    }

    public static EmployeeEntity toEntity(Employee employee) {
        return new EmployeeEntity.Builder()
                .id(employee.id())
                .firstName(employee.firstName())
                .lastName(employee.lastName())
                .department(employee.department())
                .photo(employee.photo().key())
                .build();
    }
}
