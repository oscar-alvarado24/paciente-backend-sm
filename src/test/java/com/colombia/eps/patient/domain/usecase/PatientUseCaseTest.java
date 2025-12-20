package com.colombia.eps.patient.domain.usecase;

import com.colombia.eps.patient.domain.model.Patient;
import com.colombia.eps.patient.domain.spi.ICognitoPersistencePort;
import com.colombia.eps.patient.domain.spi.IPatientPersistencePort;
import com.colombia.eps.patient.domain.spi.ISesPersistencePort;
import com.colombia.eps.patient.domain.spi.ISqsPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PatientUseCaseTest {

    @Mock
    private IPatientPersistencePort patientPersistencePort;

    @Mock
    private ICognitoPersistencePort cognitoPersistencePort;

    @Mock
    private ISesPersistencePort sesPersistencePort;

    @Mock
    private ISqsPersistencePort sqsPersistencePort;

    @InjectMocks
    private PatientUseCase patientUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePatient() {
        Patient patient = new Patient();
        patient.setEmail("test@example.com");
        String email = "admin@example.com";
        String name = "Admin";
        String identity = "12345";

        when(sqsPersistencePort.sendMessage(patient, email, name, identity)).thenReturn("Message sent");

        String result = patientUseCase.createPatient(patient, email, name, identity);

        verify(sesPersistencePort, times(1)).createSesIdentity(patient.getEmail());
        verify(cognitoPersistencePort, times(1)).createPatientInUserPool(patient);
        verify(patientPersistencePort, times(1)).createPatient(patient);
        verify(sqsPersistencePort, times(1)).sendMessage(patient, email, name, identity);
        assertEquals("Message sent", result);
    }

    @Test
    void testGetPatient() {
        String email = "test@example.com";
        Patient patient = new Patient();
        when(patientPersistencePort.getPatient(email)).thenReturn(patient);

        Patient result = patientUseCase.getPatient(email);

        verify(patientPersistencePort, times(1)).getPatient(email);
        assertEquals(patient, result);
    }

    @Test
    void testChangeStatus() {
        int id = 1;
        String status = "Inactive";
        when(patientPersistencePort.changeStatus(id, status)).thenReturn("Status changed");

        String result = patientUseCase.changeStatus(id, status);

        verify(patientPersistencePort, times(1)).changeStatus(id, status);
        assertEquals("Status changed", result);
    }

    @Test
    void testSavePhoto() {
        String email = "test@example.com";
        String photo = "base64photo";
        when(patientPersistencePort.savePhoto(email, photo)).thenReturn("Photo saved");

        String result = patientUseCase.savePhoto(email, photo);

        verify(patientPersistencePort, times(1)).savePhoto(email, photo);
        assertEquals("Photo saved", result);
    }

    @Test
    void testUpdatePatient() {
        int id = 1;
        Patient patient = new Patient();
        when(patientPersistencePort.updatePatient(id, patient)).thenReturn("Patient updated");

        String result = patientUseCase.updatePatient(id, patient);

        verify(patientPersistencePort, times(1)).updatePatient(id, patient);
        assertEquals("Patient updated", result);
    }

    @Test
    void testValidateStatusSesRegistration() {
        String email = "test@example.com";
        when(sesPersistencePort.validateStatusSesRegistration(email)).thenReturn("SES status validated");

        String result = patientUseCase.validateStatusSesRegistration(email);

        verify(sesPersistencePort, times(1)).validateStatusSesRegistration(email);
        assertEquals("SES status validated", result);
    }
}
