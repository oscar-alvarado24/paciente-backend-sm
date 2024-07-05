package com.medicine.register.infrastructure.output.dynamo.mapper;

import com.medicine.register.domain.model.Patient;
import com.medicine.register.infrastructure.output.dynamo.entity.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPatientEntityMapper {

    PatientEntity toPatientEntity(Patient patient);
    Patient toPatient(PatientEntity patientEntity);

}
