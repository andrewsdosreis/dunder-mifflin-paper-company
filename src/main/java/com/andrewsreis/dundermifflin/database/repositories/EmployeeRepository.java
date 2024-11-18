package com.andrewsreis.dundermifflin.database.repositories;

import com.andrewsreis.dundermifflin.database.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

}