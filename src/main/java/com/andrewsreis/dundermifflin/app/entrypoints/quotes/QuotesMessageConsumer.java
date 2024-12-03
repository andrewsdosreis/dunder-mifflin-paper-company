package com.andrewsreis.dundermifflin.app.entrypoints.quotes;

import com.andrewsreis.dundermifflin.core.usecases.CreateQuoteUseCase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

@Component
@Profile("dev")
public class QuotesMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuotesMessageConsumer.class);

    private final SqsClient sqsClient;
    private final String sqsQueueUrl;
    private final CreateQuoteUseCase createQuoteUseCase;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public QuotesMessageConsumer(SqsClient sqsClient, String sqsQueueUrl, CreateQuoteUseCase createQuoteUseCase) {
        this.sqsClient = sqsClient;
        this.sqsQueueUrl = sqsQueueUrl;
        this.createQuoteUseCase = createQuoteUseCase;
    }

    @Scheduled(fixedDelay = 1000)
    public void pollMessages() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(sqsQueueUrl)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(20)
                .build();

        ReceiveMessageResponse response = sqsClient.receiveMessage(receiveMessageRequest);
        response.messages()
                .forEach(message -> {
                    processMessage(message);
                    deleteMessage(message);
                });
    }

    private void processMessage(Message message) {
        LOGGER.info("Received SQS message: {}", message.body());
        try {
            JsonNode jsonNode = objectMapper.readTree(message.body());
            String name = jsonNode.get("name").asText();
            String quote = jsonNode.get("quote").asText();
            createQuoteUseCase.create(name, quote);
        } catch (Exception e) {
            LOGGER.error("Error processing message: {}", message.body(), e);
        }
    }

    private void deleteMessage(Message message) {
        sqsClient.deleteMessage(builder -> builder.queueUrl(sqsQueueUrl).receiptHandle(message.receiptHandle()));
        LOGGER.info("Deleted SQS message with receipt handle: {}", message.receiptHandle());
    }
}
