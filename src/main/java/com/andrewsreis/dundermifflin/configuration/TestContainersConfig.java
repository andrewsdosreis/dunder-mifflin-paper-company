package com.andrewsreis.dundermifflin.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import javax.sql.DataSource;

@Configuration
@Profile({"dev", "test"})
public class TestContainersConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public PostgreSQLContainer<?> postgresSQLContainer() {
        return new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("dundermifflin")
                .withUsername("user")
                .withPassword("password")
                .waitingFor(Wait.forListeningPort())
                .withCreateContainerCmdModifier(createContainerCmd -> createContainerCmd.withName("dunder-mifflin-database"))
                .withInitScript("db/init.sql");
    }

    @Bean
    public DataSource dataSource(PostgreSQLContainer<?> postgresSQLContainer) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setJdbcUrl(postgresSQLContainer.getJdbcUrl());
        dataSource.setUsername(postgresSQLContainer.getUsername());
        dataSource.setPassword(postgresSQLContainer.getPassword());
        return dataSource;
    }
}
