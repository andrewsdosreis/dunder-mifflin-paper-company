package com.andrewsreis.dundermifflin.exception;

public class EmployeeNotFoundException extends RuntimeException {
    private static final String MESSAGE_ID = "Employee not found with id: ";
    private static final String MESSAGE_NAME = "Employee not found with name: ";

    public EmployeeNotFoundException(Long id) {
        super(MESSAGE_ID.concat(id.toString()));
    }

    public EmployeeNotFoundException(String firstName, String lastName) {
        super(MESSAGE_NAME.concat(firstName).concat(" ").concat(lastName));
    }
}
