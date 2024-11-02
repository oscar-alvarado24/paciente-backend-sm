package com.medicine.patient.infrastructure.input.graphql;

import com.medicine.patient.application.dto.PatientRequest;
import com.medicine.patient.application.dto.RequestResponse;
import com.medicine.patient.application.handler.IPatientHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.Arguments;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
@RequiredArgsConstructor
@Controller
public class PatientController {

    private static final Logger log = LoggerFactory.getLogger(PatientController.class);
    private final IPatientHandler patientHandler;
    @MutationMapping
    public String createPatient (@Valid @Arguments PatientRequest patient) {
        return patientHandler.createPatient(patient);
    }

    @QueryMapping
    public RequestResponse getPatient (@Argument String email){
        log.info("El correo recivido es: " + email);
        return patientHandler.getPatient(email);
    }

    @QueryMapping
    public String validateEmail (@Argument String email){
        log.info("El correo recivido es: " + email);
        return patientHandler.validateEmail(email);
    }

}
