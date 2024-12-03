package com.andrewsreis.dundermifflin.app.dataproviders.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeJpaRepository extends JpaRepository<EmployeeEntity, Long> {
    Optional<EmployeeEntity> findByFirstNameAndLastName(String firstName, String lastName);
}
