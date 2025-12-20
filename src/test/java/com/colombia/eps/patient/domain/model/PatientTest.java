package com.colombia.eps.patient.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatientTest {

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient();
    }

    @Test
    void testId() {
        int id = 1;
        patient.setId(id);
        assertEquals(id, patient.getId());
    }

    @Test
    void testFirstName() {
        String firstName = "John";
        patient.setFirstName(firstName);
        assertEquals(firstName, patient.getFirstName());
    }

    @Test
    void testSecondName() {
        String secondName = "Fitzgerald";
        patient.setSecondName(secondName);
        assertEquals(secondName, patient.getSecondName());
    }

    @Test
    void testFirstSurName() {
        String firstSurName = "Kennedy";
        patient.setFirstSurName(firstSurName);
        assertEquals(firstSurName, patient.getFirstSurName());
    }

    @Test
    void testSecondSurName() {
        String secondSurName = "Smith";
        patient.setSecondSurName(secondSurName);
        assertEquals(secondSurName, patient.getSecondSurName());
    }

    @Test
    void testAddress() {
        String address = "123 Main St";
        patient.setAddress(address);
        assertEquals(address, patient.getAddress());
    }

    @Test
    void testEmail() {
        String email = "test@example.com";
        patient.setEmail(email);
        assertEquals(email, patient.getEmail());
    }

    @Test
    void testCellPhone() {
        String cellPhone = "123-456-7890";
        patient.setCellPhone(cellPhone);
        assertEquals(cellPhone, patient.getCellPhone());
    }

    @Test
    void testResidencesType() {
        String residencesType = "Apartment";
        patient.setResidencesType(residencesType);
        assertEquals(residencesType, patient.getResidencesType());
    }

    @Test
    void testDescriptionResidence() {
        String descriptionResidence = "A nice apartment";
        patient.setDescriptionResidence(descriptionResidence);
        assertEquals(descriptionResidence, patient.getDescriptionResidence());
    }

    @Test
    void testNeighborhood() {
        String neighborhood = "Downtown";
        patient.setNeighborhood(neighborhood);
        assertEquals(neighborhood, patient.getNeighborhood());
    }

    @Test
    void testPhoto() {
        String photo = "photo.jpg";
        patient.setPhoto(photo);
        assertEquals(photo, patient.getPhoto());
    }

    @Test
    void testStatus() {
        String status = "Active";
        patient.setStatus(status);
        assertEquals(status, patient.getStatus());
    }

    @Test
    void testProgram() {
        String program = "Program X";
        patient.setProgram(program);
        assertEquals(program, patient.getProgram());
    }

    @Test
    void testToString() {
        patient.setId(1);
        patient.setFirstName("John");
        patient.setSecondName("Fitzgerald");
        patient.setFirstSurName("Kennedy");
        patient.setSecondSurName("Smith");
        patient.setAddress("123 Main St");
        patient.setEmail("test@example.com");
        patient.setCellPhone("123-456-7890");
        patient.setResidencesType("Apartment");
        patient.setDescriptionResidence("A nice apartment");
        patient.setNeighborhood("Downtown");
        patient.setPhoto("photo.jpg");
        patient.setStatus("Active");
        patient.setProgram("Program X");

        String expectedToString = "Patient{" +
                "id=" + 1 +
                ", firstName='" + "John" + '\'' +
                ", secondName='" + "Fitzgerald" + '\'' +
                ", firstSurName='" + "Kennedy" + '\'' +
                ", secondSurName='" + "Smith" + '\'' +
                ", address='" + "123 Main St" + '\'' +
                ", email='" + "test@example.com" + '\'' +
                ", cellPhone='" + "123-456-7890" + '\'' +
                ", residencesType='" + "Apartment" + '\'' +
                ", descriptionResidence='" + "A nice apartment" + '\'' +
                ", neighborhood='" + "Downtown" + '\'' +
                ", photo='" + "photo.jpg" + '\'' +
                ", status='" + "Active" + '\'' +
                ", program='" + "Program X" + '\'' +
                '}';

        assertEquals(expectedToString, patient.toString());
    }
}
