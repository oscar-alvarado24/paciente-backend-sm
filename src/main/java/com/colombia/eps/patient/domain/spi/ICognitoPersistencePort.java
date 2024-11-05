package com.colombia.eps.patient.domain.spi;

import com.colombia.eps.patient.domain.model.Patient;

public interface ICognitoPersistencePort {
    void createPatientInUserPool(Patient patient);
}
