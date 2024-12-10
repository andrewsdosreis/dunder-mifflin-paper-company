package com.andrewsreis.dundermifflin.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.net.URI;

public class QuotesStepDefinitions {
    private final SqsClient sqsClient;

    private String messagePayload;

    public QuotesStepDefinitions() {
        this.sqsClient = SqsClient.builder()
                .endpointOverride(URI.create("https://localhost.localstack.cloud:4566"))
                .region(Region.EU_WEST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("test", "test")
                ))
                .build();
    }

    @Given("I have a message payload:")
    public void iHaveAMessagePayload(String payload) {
        this.messagePayload = payload.trim();
    }

    @When("I send the message to the {string} SQS queue")
    public void iSendTheMessageToTheSqsQueue(String queueName) {
        String queueUrl = "https://localhost.localstack.cloud:4566/000000000000/" + queueName;

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messagePayload)
                .build();

        sqsClient.sendMessage(request);
    }

    @When("I wait {int} seconds for the message to be processed")
    public void iWaitSecondsForTheMessageToBeProcessed(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000L);
    }
}
