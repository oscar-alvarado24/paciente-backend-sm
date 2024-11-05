package com.colombia.eps.patient.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PatientRequest {
    @NotBlank(message = "El campo primer nombre no puede estar vacio")
    private String firstName;
    private String secondName;
    @NotBlank(message = "El campo primer apellido no puede estar vacio")
    private String firstSurName;
    @NotBlank(message = "El campo segundo apellido no puede estar vacio")
    private String secondSurName;
    @Min(value = 10000, message = "La cedula debe tener minimo 5 numeros")
    private int citizenshipCard;
    @NotBlank(message = "El campo direccion no puede estar vacio")
    private String address;
    @NotBlank(message = "El campo email no puede estar vacio")
    @Email(message = "Por favor, ingrese una dirección de correo electrónico válida")
    private String email;
    @NotBlank(message = "El campo celular no puede estar vacio")
    private String cellPhone;
    @NotBlank(message = "El campo tipo de residencia no puede estar vacio")
    private String residencesType;
    private String descriptionResidence;
    @NotBlank(message = "El campo barrio no puede estar vacio")
    private String neighborhood;
}

