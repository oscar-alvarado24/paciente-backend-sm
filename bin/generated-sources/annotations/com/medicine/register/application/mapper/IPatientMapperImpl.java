package com.medicine.patient.application.mapper;

import com.medicine.patient.application.dto.PatientRequest;
import com.medicine.patient.application.dto.RequestResponse;
import com.medicine.patient.domain.model.Patient;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-25T10:13:01-0500",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240103-0614, environment: Java 17.0.9 (Eclipse Adoptium)"
)
@Component
public class IPatientMapperImpl implements IPatientMapper {

    @Override
    public Patient toPatient(PatientRequest patientRequest, String password) {
        if ( patientRequest == null && password == null ) {
            return null;
        }

        Patient patient = new Patient();

        if ( patientRequest != null ) {
            patient.setAddress( patientRequest.getAddress() );
            patient.setCellPhone( patientRequest.getCellPhone() );
            patient.setDescriptionResidence( patientRequest.getDescriptionResidence() );
            patient.setEmail( patientRequest.getEmail() );
            patient.setFirstName( patientRequest.getFirstName() );
            patient.setFirstSurName( patientRequest.getFirstSurName() );
            patient.setId( patientRequest.getId() );
            patient.setLandline( patientRequest.getLandline() );
            patient.setNeighborhood( patientRequest.getNeighborhood() );
            patient.setPassword( patientRequest.getPassword() );
            patient.setResidencesType( patientRequest.getResidencesType() );
            patient.setSecondName( patientRequest.getSecondName() );
            patient.setSecondSurName( patientRequest.getSecondSurName() );
        }

        return patient;
    }

    @Override
    public RequestResponse toRequestResponse(Patient patient) {
        if ( patient == null ) {
            return null;
        }

        RequestResponse requestResponse = new RequestResponse();

        requestResponse.setAddress( patient.getAddress() );
        requestResponse.setCellPhone( patient.getCellPhone() );
        requestResponse.setDescriptionResidence( patient.getDescriptionResidence() );
        requestResponse.setEmail( patient.getEmail() );
        requestResponse.setFirstName( patient.getFirstName() );
        requestResponse.setFirstSurName( patient.getFirstSurName() );
        requestResponse.setId( patient.getId() );
        requestResponse.setLandline( patient.getLandline() );
        requestResponse.setNeighborhood( patient.getNeighborhood() );
        requestResponse.setResidencesType( patient.getResidencesType() );
        requestResponse.setSecondName( patient.getSecondName() );
        requestResponse.setSecondSurName( patient.getSecondSurName() );

        return requestResponse;
    }
}
