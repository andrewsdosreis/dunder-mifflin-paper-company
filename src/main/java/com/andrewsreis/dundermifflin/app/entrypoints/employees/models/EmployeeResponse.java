package com.andrewsreis.dundermifflin.app.entrypoints.employees.models;

import java.util.List;

public record EmployeeResponse(
        Long id,
        String firstName,
        String lastName,
        String department,
        String photoLocation,
        String trivia,
        List<String> quotes
) {
}
