package com.andrewsreis.dundermifflin.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration(value = "aws")
public class AwsConfiguration {

    private final AwsProperties awsProperties;

    public AwsConfiguration(AwsProperties awsProperties) {
        this.awsProperties = awsProperties;
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(awsProperties.getS3().getEndpoint()))
                .region(Region.of(awsProperties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(createAwsBasicCredentials()))
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
                .region(Region.of(awsProperties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(createAwsBasicCredentials()))
                .endpointOverride(URI.create(awsProperties.getSqs().getEndpoint()))
                .build();
    }

    @Bean
    public String sqsQueueUrl() {
        return awsProperties.getSqs().getEndpoint().concat(awsProperties.getSqs().getQueue());
    }

    private AwsBasicCredentials createAwsBasicCredentials() {
        return AwsBasicCredentials.create(
                awsProperties.getCredentials().getAccessKey(),
                awsProperties.getCredentials().getSecretKey()
        );
    }
}
