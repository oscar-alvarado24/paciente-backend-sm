package com.medicine.register.infrastructure.output.dynamo.mapper;

import com.medicine.register.domain.model.Patient;
import com.medicine.register.infrastructure.output.dynamo.entity.PatientEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-25T10:13:01-0500",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240103-0614, environment: Java 17.0.9 (Eclipse Adoptium)"
)
@Component
public class IPatientEntityMapperImpl implements IPatientEntityMapper {

    @Override
    public PatientEntity toPatientEntity(Patient patient) {
        if ( patient == null ) {
            return null;
        }

        PatientEntity patientEntity = new PatientEntity();

        patientEntity.setAddress( patient.getAddress() );
        patientEntity.setCellPhone( patient.getCellPhone() );
        patientEntity.setDescriptionResidence( patient.getDescriptionResidence() );
        patientEntity.setEmail( patient.getEmail() );
        patientEntity.setFirstName( patient.getFirstName() );
        patientEntity.setFirstSurName( patient.getFirstSurName() );
        patientEntity.setId( patient.getId() );
        patientEntity.setLandline( patient.getLandline() );
        patientEntity.setNeighborhood( patient.getNeighborhood() );
        patientEntity.setPassword( patient.getPassword() );
        patientEntity.setSecondName( patient.getSecondName() );
        patientEntity.setSecondSurName( patient.getSecondSurName() );

        return patientEntity;
    }

    @Override
    public Patient toPatient(PatientEntity patientEntity) {
        if ( patientEntity == null ) {
            return null;
        }

        Patient patient = new Patient();

        patient.setAddress( patientEntity.getAddress() );
        patient.setCellPhone( patientEntity.getCellPhone() );
        patient.setDescriptionResidence( patientEntity.getDescriptionResidence() );
        patient.setEmail( patientEntity.getEmail() );
        patient.setFirstName( patientEntity.getFirstName() );
        patient.setFirstSurName( patientEntity.getFirstSurName() );
        patient.setId( patientEntity.getId() );
        patient.setLandline( patientEntity.getLandline() );
        patient.setNeighborhood( patientEntity.getNeighborhood() );
        patient.setPassword( patientEntity.getPassword() );
        patient.setSecondName( patientEntity.getSecondName() );
        patient.setSecondSurName( patientEntity.getSecondSurName() );

        return patient;
    }
}
