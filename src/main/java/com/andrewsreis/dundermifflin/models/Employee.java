package com.andrewsreis.dundermifflin.models;

public record Employee(
        Long id,
        String firstName,
        String lastName,
        String department,
        String photo,
        String trivia
) {
}
