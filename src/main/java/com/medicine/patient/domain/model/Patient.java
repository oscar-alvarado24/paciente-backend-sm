package com.medicine.patient.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {
    private String firstName;
    private String secondName;
    private String firstSurName;
    private String secondSurName;
    private String address;
    private String email;
    private String landline;
    private String cellPhone;
    private String residencesType;
    private String descriptionResidence;
    private String neighborhood;
    private String password;
}
