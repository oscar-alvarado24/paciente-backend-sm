package com.colombia.eps.patient.application.mapper;

import com.colombia.eps.patient.application.dto.PatientRequest;
import com.colombia.eps.patient.application.dto.PatientResponse;
import com.colombia.eps.patient.application.helper.CryptoUtil;
import com.colombia.eps.patient.application.helper.exception.BadStatusException;
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
        String status;
        switch (patient.getStatus()) {
            case "ACTIVE" -> status = ApplicationConstants.PATIENT_EXIST;
            case "INACTIVE" -> status = ApplicationConstants.PATIENT_INACTIVE;
            case "RETIRED" -> status = ApplicationConstants.PATIENT_RETIRED;
            default -> throw new BadStatusException();
        }
        String secondName = patient.getSecondName() == null || patient.getSecondName().isBlank()? "" : CryptoUtil.encrypt(patient.getSecondName());
        String descriptionResidence = patient.getDescriptionResidence() == null || patient.getDescriptionResidence().isBlank() ? "" : CryptoUtil.encrypt(patient.getDescriptionResidence());
        String photo = patient.getPhoto() == null || patient.getPhoto().isBlank() ? "" : CryptoUtil.encrypt(patient.getPhoto());
        
        return new PatientResponse(
                CryptoUtil.encrypt(String.valueOf(patient.getId())),
                CryptoUtil.encrypt(patient.getFirstName()),
                secondName,
                CryptoUtil.encrypt(patient.getFirstSurName()),
                CryptoUtil.encrypt(patient.getSecondSurName()),
                CryptoUtil.encrypt(patient.getAddress()),
                CryptoUtil.encrypt(patient.getEmail()),
                CryptoUtil.encrypt(patient.getCellPhone()),
                CryptoUtil.encrypt(patient.getResidencesType()),
                descriptionResidence,
                CryptoUtil.encrypt(patient.getNeighborhood()),
                photo,
                status);
    }
}
