package com.andrewsreis.dundermifflin.core.domain;

import org.apache.logging.log4j.util.Strings;

import java.util.List;

public record Employee(
        Long id,
        String firstName,
        String lastName,
        String department,
        Photo photo,
        String trivia,
        List<Quote> quotes
) {
    public Employee withAggregatedData(Photo photo, String trivia, List<Quote> quotes) {
        return new Employee(
                this.id(),
                this.firstName(),
                this.lastName(),
                this.department(),
                photo,
                trivia,
                quotes
        );
    }

    public static class Builder {
        private Long id = null;
        private String firstName;
        private String lastName;
        private String department;
        private Photo photo;
        private String trivia = Strings.EMPTY;
        private List<Quote> quotes = List.of();

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder photo(Photo photo) {
            this.photo = photo;
            return this;
        }

        public Builder trivia(String trivia) {
            this.trivia = trivia;
            return this;
        }

        public Builder quotes(List<Quote> quotes) {
            this.quotes = quotes != null ? List.copyOf(quotes) : List.of();
            return this;
        }

        public Employee build() {
            if (firstName == null || lastName == null || department == null) {
                throw new IllegalStateException("firstName, lastName and department are required fields");
            }
            return new Employee(id, firstName, lastName, department, photo, trivia, quotes);
        }
    }
}
