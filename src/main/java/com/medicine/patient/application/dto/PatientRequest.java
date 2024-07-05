package com.medicine.register.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PatientRequest {
    private String id;
    private String firstName;
    private String secondName;
    private String firstSurName;
    private String secondSurName;
    private String address;
    @Email(message = "Por favor, ingrese una dirección de correo electrónico válida")
    private String email;
    private String landline;
    private String cellPhone;
    private String residencesType;
    private String descriptionResidence;
    private String neighborhood;
}

