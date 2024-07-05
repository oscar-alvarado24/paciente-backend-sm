package com.medicine.register.domain.spi;

import com.medicine.register.application.dto.RequestResponse;
import com.medicine.register.domain.model.Patient;

public interface IPatientPersistencePort {
    void createPatient (Patient patient);
    Patient getPatient (String email);

    String validateEmail(String email);
}
