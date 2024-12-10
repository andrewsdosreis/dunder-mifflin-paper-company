package com.andrewsreis.dundermifflin.app.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@TestConfiguration
public class AwsConfiguration {
    private final AwsProperties awsProperties;
    private final AwsBasicCredentials credentials;
    private final TestContainersConfiguration testContainersConfiguration;
    private final LocalStackContainer localStackContainer;

    public AwsConfiguration(AwsProperties awsProperties, TestContainersConfiguration testContainersConfiguration, LocalStackContainer localStackContainer) {
        this.awsProperties = awsProperties;
        this.credentials = AwsBasicCredentials.create(awsProperties.getCredentials().getAccessKeyId(), awsProperties.getCredentials().getSecretAccessKey());
        this.testContainersConfiguration = testContainersConfiguration;
        this.localStackContainer = localStackContainer;
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(localStackContainer.getEndpointOverride(S3))
                .region(Region.of(localStackContainer.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }

    @Bean
    public String s3Bucket() {
        return awsProperties.getS3().getBucket();
    }

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .endpointOverride(URI.create("https://localhost.localstack.cloud:4566"))
                .region(Region.EU_WEST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("test", "test")
                ))
                .build();
    }

    @Bean
    public String sqsQueueUrl() {
        return "https://localhost.localstack.cloud:4566/000000000000/quotes";
    }

    @Bean
    public String triviaEndpoint() {
        return testContainersConfiguration.getTriviaApiEndpoint();
    }
}
