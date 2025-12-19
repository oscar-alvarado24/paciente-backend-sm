package com.colombia.eps.patient.infrastructure.output.sqs.adpter;

import com.colombia.eps.patient.domain.model.Patient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@Testcontainers
class SqsAdapterIT {

    @Container
    static LocalStackContainer localStack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.14.2"))
            .withServices(SQS);

    private static SqsAdapter sqsAdapter;

    @BeforeAll
    static void setUp() {
        SqsClient sqsClient = SqsClient.builder()
                .endpointOverride(localStack.getEndpointOverride(SQS))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
                .region(Region.of(localStack.getRegion()))
                .build();

        CreateQueueResponse queue = sqsClient.createQueue(CreateQueueRequest.builder().queueName("test-queue").build());

        sqsAdapter = new SqsAdapter(sqsClient, queue.queueUrl());
    }

    @Test
    void testSendMessage() {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setProgram("Test Program");
        patient.setFirstName("John");
        patient.setFirstSurName("Doe");

        assertDoesNotThrow(() -> sqsAdapter.sendMessage(patient, "test@example.com", "John Doe", "123"));
    }
}
