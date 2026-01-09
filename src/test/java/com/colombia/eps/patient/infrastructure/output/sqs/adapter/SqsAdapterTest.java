package com.colombia.eps.patient.infrastructure.output.sqs.adapter;

import com.colombia.eps.patient.domain.model.Patient;
import com.colombia.eps.patient.infrastructure.exception.SendQueueFailedException;
import com.colombia.eps.patient.infrastructure.output.sqs.adpter.SqsAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SqsAdapterTest {
    @Mock
    private SqsClient sqsClient;

    @InjectMocks
    private SqsAdapter sqsAdapter;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(sqsAdapter, "queueUrl", "https://sqs.us-east-1.amazonaws.com/123456789/test-queue");
    }
    @Test
    void testCatchExceptionAndGenerateSendQueueFailedException(){
        Patient patient = new Patient();
        patient.setFirstName("Juan");
        patient.setFirstSurName("Pérez");

        when(sqsClient.sendMessage(any(SendMessageRequest.class)))
                .thenThrow(new RuntimeException("SQS error"));

        // When & Then
        assertThrows(SendQueueFailedException.class,
                () -> sqsAdapter.sendMessage(patient, "email@test.com", "name", "id"));

        verify(sqsClient).sendMessage(any(SendMessageRequest.class));
    }
}
