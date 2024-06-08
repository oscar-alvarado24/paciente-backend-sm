package com.medicine.register.infrastructure.output.dynamo.mapper;

import com.medicine.register.domain.model.Patient;
import com.medicine.register.infrastructure.output.dynamo.entity.PatientEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-07T20:14:39-0500",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.1.1.jar, environment: Java 19.0.2 (Amazon.com Inc.)"
)
@Component
public class IPatientEntityMapperImpl implements IPatientEntityMapper {

    @Override
    public PatientEntity toPatientEntity(Patient patient) {
        if ( patient == null ) {
            return null;
        }

        PatientEntity patientEntity = new PatientEntity();

        patientEntity.setEmail( patient.getEmail() );
        patientEntity.setFirstName( patient.getFirstName() );
        patientEntity.setSecondName( patient.getSecondName() );
        patientEntity.setFirstSurName( patient.getFirstSurName() );
        patientEntity.setSecondSurName( patient.getSecondSurName() );
        patientEntity.setAddress( patient.getAddress() );
        patientEntity.setLandline( patient.getLandline() );
        patientEntity.setCellPhone( patient.getCellPhone() );
        patientEntity.setDescriptionResidence( patient.getDescriptionResidence() );
        patientEntity.setNeighborhood( patient.getNeighborhood() );

        return patientEntity;
    }

    @Override
    public Patient toPatient(PatientEntity patientEntity) {
        if ( patientEntity == null ) {
            return null;
        }

        Patient patient = new Patient();

        patient.setFirstName( patientEntity.getFirstName() );
        patient.setSecondName( patientEntity.getSecondName() );
        patient.setFirstSurName( patientEntity.getFirstSurName() );
        patient.setSecondSurName( patientEntity.getSecondSurName() );
        patient.setAddress( patientEntity.getAddress() );
        patient.setEmail( patientEntity.getEmail() );
        patient.setLandline( patientEntity.getLandline() );
        patient.setCellPhone( patientEntity.getCellPhone() );
        patient.setDescriptionResidence( patientEntity.getDescriptionResidence() );
        patient.setNeighborhood( patientEntity.getNeighborhood() );

        return patient;
    }
}
