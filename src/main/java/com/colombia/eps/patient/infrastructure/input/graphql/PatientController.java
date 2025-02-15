package com.colombia.eps.patient.infrastructure.input.graphql;

import com.colombia.eps.patient.application.dto.PatientRequest;
import com.colombia.eps.patient.application.dto.PatientResponse;
import com.colombia.eps.patient.application.handler.IPatientHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.Arguments;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@Component
@RequiredArgsConstructor
@RestController
@Slf4j
public class PatientController {


    private final IPatientHandler patientHandler;
    @MutationMapping
    public String createPatient (@Arguments PatientRequest patient) {
        return patientHandler.createPatient(patient);
    }

    @QueryMapping
    public PatientResponse getPatient (@Argument String email){
        return patientHandler.getPatient(email);
    }

    @QueryMapping
    public String validateStatus(@Argument String email) {
        return patientHandler.validateStatus(email);
    }

    @MutationMapping
    public String changeStatus (@Argument int id, @Argument String status){
        return patientHandler.changeStatus(id, status);
    }
}
