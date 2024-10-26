package com.andrewsreis.dundermifflin.repositories;

import com.andrewsreis.dundermifflin.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

}
