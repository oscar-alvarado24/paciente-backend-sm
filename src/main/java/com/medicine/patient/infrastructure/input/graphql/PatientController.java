package com.medicine.register.infrastructure.input.graphql;

import com.medicine.register.application.dto.PatientRequest;
import com.medicine.register.application.dto.RequestResponse;
import com.medicine.register.application.handler.IPatientHandler;
import com.medicine.register.infrastructure.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.Arguments;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@Component
@RequiredArgsConstructor
@RestController
public class PatientController {

    private final IPatientHandler patientHandler;
    @MutationMapping
    public String createPatient (@Arguments PatientRequest patient) {
        patientHandler.createPatient(patient);
        return String.format(Constants.MSG_CREATE_PATIENT, patient.getFirstName(), patient.getFirstSurName());
    }

    @QueryMapping
    public RequestResponse getPatient (@Argument String email){
        return patientHandler.getPatient(email);
    }

    @QueryMapping
    public String validateEmail (@Argument String email){
        return patientHandler.validateEmail(email);
    }

}
