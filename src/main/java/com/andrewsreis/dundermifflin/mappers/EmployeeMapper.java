package com.andrewsreis.dundermifflin.mappers;

import com.andrewsreis.dundermifflin.entities.EmployeeEntity;
import com.andrewsreis.dundermifflin.models.Employee;
import com.andrewsreis.dundermifflin.utils.ImageUtil;

import java.awt.image.BufferedImage;

public abstract class EmployeeMapper {
    public static Employee toModel(EmployeeEntity entity, String trivia) {
        return new Employee(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getDepartment(),
                entity.getPhoto(),
                trivia
        );
    }

    public static String toEmployeePhoto(BufferedImage photo) {
        return ImageUtil.beautify(photo);
    }

    public static EmployeeEntity toEntity(Employee model) {
        return new EmployeeEntity.Builder()
                .id(model.id())
                .firstName(model.firstName())
                .lastName(model.lastName())
                .department(model.department())
                .build();
    }
}