package com.andrewsreis.dundermifflin;

import org.springframework.boot.SpringApplication;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.utility.TestcontainersConfiguration;

@ActiveProfiles("test")
public class TestDunderMifflinApplication {

    public static void main(String[] args) {
        SpringApplication.from(DunderMifflinApplication::main).with(TestcontainersConfiguration.class).run(args);
    }
}
