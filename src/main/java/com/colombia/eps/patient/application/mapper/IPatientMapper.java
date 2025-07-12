package com.colombia.eps.patient.application.mapper;

import com.colombia.eps.patient.application.dto.PatientRequest;
import com.colombia.eps.patient.application.dto.PatientResponse;
import com.colombia.eps.patient.application.helper.ApplicationConstants;
import com.colombia.eps.patient.domain.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPatientMapper {
     Patient toPatient (PatientRequest patientRequest);

    default PatientResponse toRequestResponse (Patient patient){
        String status = "";
        switch (patient.getStatus()) {
            case "ACTIVE" -> status = ApplicationConstants.PATIENT_EXIST;
            case "INACTIVE" -> status = ApplicationConstants.PATIENT_INACTIVE;
            case "RETIRED" -> status = ApplicationConstants.PATIENT_RETIRED;
        }
        return new PatientResponse(
                patient.getId(),
                patient.getFirstName(),
                patient.getSecondName(),
                patient.getFirstSurName(),
                patient.getSecondSurName(),
                patient.getAddress(),
                patient.getEmail(),
                patient.getCellPhone(),
                patient.getResidencesType(),
                patient.getDescriptionResidence(),
                patient.getNeighborhood(),
                patient.getPhoto(),
                status);
    }
}
