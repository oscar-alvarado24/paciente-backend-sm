package com.colombia.eps.patient.application.mapper;

import com.colombia.eps.patient.application.dto.PatientRequest;
import com.colombia.eps.patient.application.dto.PatientResponse;
import com.colombia.eps.patient.domain.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPatientMapper {
     Patient toPatient (PatientRequest patientRequest);

    PatientResponse toRequestResponse (Patient patient);
}
