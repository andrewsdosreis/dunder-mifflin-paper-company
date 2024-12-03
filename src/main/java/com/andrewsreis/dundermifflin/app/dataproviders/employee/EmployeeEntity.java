package com.andrewsreis.dundermifflin.app.dataproviders.employee;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @Column(name = "first_name", length = 50, nullable = false)
    private final String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private final String lastName;

    @Column(length = 50)
    private final String department;

    @Column
    private String photo;

    protected EmployeeEntity() {
        this.id = null;
        this.firstName = null;
        this.lastName = null;
        this.department = null;
        this.photo = null;
    }

    private EmployeeEntity(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.department = builder.department;
        this.photo = builder.photo;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDepartment() {
        return department;
    }

    public String getPhoto() {
        return photo;
    }

    // Builder
    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private String department;
        private String photo;

        public Builder() {
        }

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

        public Builder photo(String photo) {
            this.photo = photo;
            return this;
        }

        public EmployeeEntity build() {
            return new EmployeeEntity(this);
        }
    }
}
