package com.medicine.patient.infrastructure.output.dynamo.repository;

import com.medicine.patient.infrastructure.output.dynamo.entity.PatientEntity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableScan
@Repository
public interface IPatientRepository extends CrudRepository<PatientEntity,String> {
    Optional<PatientEntity> findByEmail(String email);
}
