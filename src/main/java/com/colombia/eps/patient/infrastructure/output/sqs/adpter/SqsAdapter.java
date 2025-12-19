package com.colombia.eps.patient.infrastructure.output.sqs.adpter;

import com.colombia.eps.patient.domain.model.Patient;
import com.colombia.eps.patient.domain.spi.ISqsPersistencePort;
import com.colombia.eps.patient.infrastructure.exception.SendQueueFailedException;
import com.colombia.eps.patient.infrastructure.helper.Constants;
import com.colombia.eps.patient.infrastructure.output.sqs.entity.SqsEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
@Slf4j
public class SqsAdapter implements ISqsPersistencePort {

    private final SqsClient sqsClient;
    private final String queueUrl;

    public SqsAdapter(SqsClient sqsClient, @Value("${aws.sqs.queue.url}") String queueUrl) {
        this.sqsClient = sqsClient;
        this.queueUrl = queueUrl;
    }

    @Override
    public String sendMessage(Patient patient, String email, String name, String id) {
        try {
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
            String formatedDate = date.format(formatter);
            String messageBody = new SqsEntity(name, id, formatedDate, patient.getProgram(), email, Constants.WELCOME, Constants.EMAIL, Constants.SES_VERIFIED).toString();

            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(messageBody)
                    .build();

            SendMessageResponse response = sqsClient.sendMessage(sendMessageRequest);

            log.info("Mensaje enviado a SQS con ID: {}", response.messageId());
            return String.format(Constants.PATIENT_CREATED_SUCCESFULLY, patient.getFirstName(), patient.getFirstSurName());

        } catch (Exception e) {
            log.error("Error enviando mensaje a SQS: {}", e.getMessage(), e);
            throw new SendQueueFailedException(Constants.ERROR_SEND_QUEUE);
        }
    }
}
