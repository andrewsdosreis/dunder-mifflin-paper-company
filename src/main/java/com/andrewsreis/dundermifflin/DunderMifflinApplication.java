package com.andrewsreis.dundermifflin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DunderMifflinApplication {

    public static void main(String[] args) {
        SpringApplication.run(DunderMifflinApplication.class, args);
    }

}
