package com.andrewsreis.dundermifflin.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@TestConfiguration
public class DatabaseConfiguration {
    @Bean
    public DataSource dataSource(PostgreSQLContainer<?> postgresSQLContainer) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(postgresSQLContainer.getJdbcUrl());
        dataSource.setUsername(postgresSQLContainer.getUsername());
        dataSource.setPassword(postgresSQLContainer.getPassword());
        return dataSource;
    }
}
