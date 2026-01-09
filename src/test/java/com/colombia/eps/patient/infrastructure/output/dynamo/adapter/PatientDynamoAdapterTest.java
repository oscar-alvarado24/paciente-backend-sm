package com.colombia.eps.patient.infrastructure.output.dynamo.adapter;

import com.colombia.eps.patient.domain.model.Patient;
import com.colombia.eps.patient.infrastructure.exception.PatienNotCretedException;
import com.colombia.eps.patient.infrastructure.output.dynamo.mapper.IPatientEntityMapper;
import com.colombia.eps.patient.infrastructure.output.dynamo.repository.IPatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientDynamoAdapterTest {

    @Mock
    private IPatientRepository patientRepository;

    @Mock
    private IPatientEntityMapper patientEntityMapper;

    @Mock
    private DynamoDbClient dynamoDbClient;

    @Mock
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @InjectMocks
    private PatientDynamoAdapter patientDynamoAdapter;

    @Test
    void testCreatePatientShouldCatchException() {
        when(patientEntityMapper.toPatientEntity(any(Patient.class))).thenThrow(new RuntimeException("Simulación de error"));

        assertThrows(PatienNotCretedException.class, () -> patientDynamoAdapter.createPatient(new Patient()));
    }
}
