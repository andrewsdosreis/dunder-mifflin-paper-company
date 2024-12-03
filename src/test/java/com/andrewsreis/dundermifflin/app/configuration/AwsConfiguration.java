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

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@TestConfiguration
public class AwsConfiguration {
    private final AwsProperties awsProperties;
    private final AwsBasicCredentials credentials;
    private final TestContainersConfiguration testContainersConfiguration;

    public AwsConfiguration(AwsProperties awsProperties, TestContainersConfiguration testContainersConfiguration) {
        this.awsProperties = awsProperties;
        this.credentials = AwsBasicCredentials.create(awsProperties.getCredentials().getAccessKeyId(), awsProperties.getCredentials().getSecretAccessKey());
        this.testContainersConfiguration = testContainersConfiguration;
    }

    @Bean
    public S3Client s3Client(LocalStackContainer localStackContainer) {
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
    public SqsClient sqsClient(LocalStackContainer localStackContainer) {
        return SqsClient.builder()
                .endpointOverride(localStackContainer.getEndpointOverride(SQS))
                .region(Region.of(localStackContainer.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Bean
    public String sqsQueueUrl(LocalStackContainer localStackContainer) {
        return "http://localhost.localstack.cloud:4566/000000000000/quotes" ;
    }

    @Bean
    public String triviaEndpoint() {
        return testContainersConfiguration.getTriviaApiEndpoint();
    }
}
