package com.andrewsreis.dundermifflin.database.repositories;

import com.andrewsreis.dundermifflin.database.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    Optional<EmployeeEntity> findByFirstNameAndLastName(String firstName, String lastName);
}
