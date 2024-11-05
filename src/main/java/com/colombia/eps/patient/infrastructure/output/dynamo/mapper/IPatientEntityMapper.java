package com.colombia.eps.patient.infrastructure.output.dynamo.mapper;

import com.colombia.eps.patient.domain.model.Patient;
import com.colombia.eps.patient.infrastructure.output.dynamo.entity.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPatientEntityMapper {

    PatientEntity toPatientEntity(Patient patient);
    Patient toPatient(PatientEntity patientEntity);

}
