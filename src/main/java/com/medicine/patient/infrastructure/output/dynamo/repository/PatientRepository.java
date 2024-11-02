package com.medicine.patient.infrastructure.output.dynamo.repository;

import com.medicine.patient.infrastructure.output.dynamo.entity.PatientEntity;
import com.medicine.patient.infrastructure.util.Constants;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Optional;

public class PatientRepository implements IPatientRepository {
    /**
     * @param email of patient to search
     * @param table object to operate the table dynamo
     * @return optional on patient
     */
    @Override
    public Optional<PatientEntity> findPatientByEmail(String email, DynamoDbTable<PatientEntity> table) {
        Key key = Key.builder().partitionValue(email).build();
        return Optional.ofNullable(table.getItem(key));

    }

    /**
     * @param patient to save
     * @param table   object to operate the table dynamo
     * @return message of confirmation
     */
    @Override
    public String save(PatientEntity patient, DynamoDbTable<PatientEntity> table) {
        table.putItem(patient);
        return String.format(Constants.MSG_CREATE_PATIENT, patient.getFirstName(), patient.getFirstSurName());
    }
}
