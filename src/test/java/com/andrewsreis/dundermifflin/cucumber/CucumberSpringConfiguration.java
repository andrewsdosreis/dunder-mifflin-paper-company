package com.andrewsreis.dundermifflin.cucumber;

import com.andrewsreis.dundermifflin.app.configuration.AwsConfiguration;
import com.andrewsreis.dundermifflin.app.configuration.CacheConfiguration;
import com.andrewsreis.dundermifflin.app.configuration.DatabaseConfiguration;
import com.andrewsreis.dundermifflin.app.configuration.TestContainersConfiguration;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import({TestContainersConfiguration.class, AwsConfiguration.class, DatabaseConfiguration.class, CacheConfiguration.class})
public class CucumberSpringConfiguration {
}
