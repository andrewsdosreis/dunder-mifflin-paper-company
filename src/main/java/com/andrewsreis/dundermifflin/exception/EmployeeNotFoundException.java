package com.andrewsreis.dundermifflin.exception;

public class EmployeeNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Employee not found with id: ";

    public EmployeeNotFoundException(Long id) {
        super(MESSAGE.concat(id.toString()));
    }
}
