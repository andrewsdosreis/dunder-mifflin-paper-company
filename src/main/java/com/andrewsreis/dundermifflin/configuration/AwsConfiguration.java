package com.andrewsreis.dundermifflin.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration(value = "aws")
@Profile({"dev"})
public class AwsConfiguration {

    private final AwsProperties awsProperties;
    private final AwsBasicCredentials credentials;

    public AwsConfiguration(AwsProperties awsProperties) {
        this.awsProperties = awsProperties;
        this.credentials = AwsBasicCredentials.create(
                awsProperties.getCredentials().getAccessKeyId(),
                awsProperties.getCredentials().getSecretAccessKey());
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(awsProperties.getS3().getEndpoint()))
                .region(Region.of(awsProperties.getRegion()))
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
                .region(Region.of(awsProperties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .endpointOverride(URI.create(awsProperties.getSqs().getEndpoint()))
                .build();
    }

    @Bean
    public String sqsQueueUrl() {
        return awsProperties.getSqs().getEndpoint().concat(awsProperties.getSqs().getQueue());
    }

    @Bean
    public String triviaEndpoint() {
        return awsProperties.getApiGateway().getEndpoint();
    }
}
