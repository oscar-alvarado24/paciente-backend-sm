package com.medicine.patient.infrastructure.output.dynamo.mapper;

import com.medicine.patient.domain.model.Patient;
import com.medicine.patient.infrastructure.output.dynamo.entity.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPatientEntityMapper {

    PatientEntity toPatientEntity(Patient patient);
    Patient toPatient(PatientEntity patientEntity);

}
