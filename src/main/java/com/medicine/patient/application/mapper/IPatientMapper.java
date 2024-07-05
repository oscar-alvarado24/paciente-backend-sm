package com.medicine.register.application.mapper;

import com.medicine.register.application.dto.PatientRequest;
import com.medicine.register.application.dto.RequestResponse;
import com.medicine.register.domain.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPatientMapper {
     Patient toPatient (PatientRequest patientRequest);

    RequestResponse toRequestResponse (Patient patient);
}
