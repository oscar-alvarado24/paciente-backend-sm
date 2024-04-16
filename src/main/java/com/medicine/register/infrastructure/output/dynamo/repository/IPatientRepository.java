package com.medicine.register.infrastructure.output.dynamo.repository;

import com.medicine.register.infrastructure.output.dynamo.entity.PatientEntity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@EnableScan
@Repository
public interface IPatientRepository extends CrudRepository<PatientEntity,String> {
}
