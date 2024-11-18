package com.andrewsreis.dundermifflin.models;

import java.util.List;

public record EmployeeQuotes(
        String name,
        String lastName,
        List<String> quotes
) {
}
