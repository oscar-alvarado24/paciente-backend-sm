package com.colombia.eps.patient.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponse {
    private int id;
    private String firstName;
    private String secondName;
    private String firstSurName;
    private String secondSurName;
    private String address;
    private String email;
    private int citizenshipCard;
    private String cellPhone;
    private String residencesType;
    private String descriptionResidence;
    private String neighborhood;
}
