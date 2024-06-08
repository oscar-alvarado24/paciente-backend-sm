package com.medicine.register.application.mapper;

import com.medicine.register.application.dto.PatientRequest;
import com.medicine.register.application.dto.RequestResponse;
import com.medicine.register.domain.model.Patient;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-07T20:14:39-0500",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.1.1.jar, environment: Java 19.0.2 (Amazon.com Inc.)"
)
@Component
public class IPatientMapperImpl implements IPatientMapper {

    @Override
    public Patient toPatient(PatientRequest patientRequest) {
        if ( patientRequest == null ) {
            return null;
        }

        Patient patient = new Patient();

        patient.setId( patientRequest.getId() );
        patient.setFirstName( patientRequest.getFirstName() );
        patient.setSecondName( patientRequest.getSecondName() );
        patient.setFirstSurName( patientRequest.getFirstSurName() );
        patient.setSecondSurName( patientRequest.getSecondSurName() );
        patient.setAddress( patientRequest.getAddress() );
        patient.setEmail( patientRequest.getEmail() );
        patient.setLandline( patientRequest.getLandline() );
        patient.setCellPhone( patientRequest.getCellPhone() );
        patient.setResidencesType( patientRequest.getResidencesType() );
        patient.setDescriptionResidence( patientRequest.getDescriptionResidence() );
        patient.setNeighborhood( patientRequest.getNeighborhood() );

        return patient;
    }

    @Override
    public RequestResponse toRequestResponse(Patient patient) {
        if ( patient == null ) {
            return null;
        }

        RequestResponse requestResponse = new RequestResponse();

        requestResponse.setFirstName( patient.getFirstName() );
        requestResponse.setSecondName( patient.getSecondName() );
        requestResponse.setFirstSurName( patient.getFirstSurName() );
        requestResponse.setSecondSurName( patient.getSecondSurName() );
        requestResponse.setAddress( patient.getAddress() );
        requestResponse.setEmail( patient.getEmail() );
        requestResponse.setLandline( patient.getLandline() );
        requestResponse.setCellPhone( patient.getCellPhone() );
        requestResponse.setResidencesType( patient.getResidencesType() );
        requestResponse.setDescriptionResidence( patient.getDescriptionResidence() );
        requestResponse.setNeighborhood( patient.getNeighborhood() );

        return requestResponse;
    }
}
