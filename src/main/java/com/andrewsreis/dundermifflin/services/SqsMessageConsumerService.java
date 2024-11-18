
package com.andrewsreis.dundermifflin.services;

import com.andrewsreis.dundermifflin.cache.entities.Quote;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.List;

@Service
public class SqsMessageConsumerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqsMessageConsumerService.class);

    private final SqsClient sqsClient;
    private final String sqsQueueUrl;
    private final QuoteService quoteService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SqsMessageConsumerService(SqsClient sqsClient, String sqsQueueUrl, QuoteService quoteService) {
        this.sqsClient = sqsClient;
        this.sqsQueueUrl = sqsQueueUrl;
        this.quoteService = quoteService;
    }

    @Scheduled(fixedDelay = 1000)
    public void pollMessages() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(sqsQueueUrl)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(20)
                .build();

        ReceiveMessageResponse response = sqsClient.receiveMessage(receiveMessageRequest);
        List<Message> messages = response.messages();

        for (Message message : messages) {
            processMessage(message);
            deleteMessage(message);
        }
    }

    private void processMessage(Message message) {
        LOGGER.info("Received SQS message: {}", message.body());
        try {
            JsonNode jsonNode = objectMapper.readTree(message.body());
            String name = jsonNode.get("name").asText();
            String quote = jsonNode.get("quote").asText();
            quoteService.saveQuote(new Quote(name, quote));
        } catch (Exception e) {
            LOGGER.error("Error processing message: {}", message.body(), e);
        }
    }

    private void deleteMessage(Message message) {
        sqsClient.deleteMessage(builder -> builder.queueUrl(sqsQueueUrl).receiptHandle(message.receiptHandle()));
        LOGGER.info("Deleted SQS message with receipt handle: {}", message.receiptHandle());
    }
}