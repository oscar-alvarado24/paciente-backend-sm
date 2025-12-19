package com.colombia.eps.patient.domain.spi;

import com.colombia.eps.patient.domain.model.Patient;

public interface ISqsPersistencePort {

    String sendMessage(Patient patient, String email, String name, String identity);
}
