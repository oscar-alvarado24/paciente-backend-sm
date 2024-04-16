package com.medicine.register.infrastructure.output.dynamo.mapper;

import com.medicine.register.domain.model.Patient;
import com.medicine.register.infrastructure.output.dynamo.entity.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPatientEntityMapper {

    default PatientEntity toPatientEntity(Patient patient){
        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setPatient(patient.getPassword());
    }

    Patient toPatient(PatientEntity patientEntity);
}
