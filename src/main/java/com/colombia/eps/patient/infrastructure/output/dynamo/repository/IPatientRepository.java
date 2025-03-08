package com.colombia.eps.patient.infrastructure.output.dynamo.repository;

import com.colombia.eps.patient.infrastructure.output.dynamo.entity.PatientEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.Optional;

@Repository
public interface IPatientRepository {
    Optional<PatientEntity> findPatientByEmail(String email, DynamoDbTable<PatientEntity> table);
    String save(PatientEntity patient, DynamoDbTable<PatientEntity> table);

    Optional<PatientEntity> findPatientById(String id, DynamoDbTable<PatientEntity> table);

    String updatePatient(PatientEntity patient, DynamoDbTable<PatientEntity> table);
}