package com.colombia.eps.patient.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PatientRequest(
        @NotBlank(message = "El campo primer nombre no puede estar vacio")
        String firstName,

        String secondName,

        @NotBlank(message = "El campo primer apellido no puede estar vacio")
        String firstSurName,

        @NotBlank(message = "El campo segundo apellido no puede estar vacio")
        String secondSurName,

        @Min(value = 10000, message = "La cedula debe tener minimo 5 digitos")
        int id,

        @NotBlank(message = "El campo direccion no puede estar vacio")
        String address,

        @NotBlank(message = "El campo email no puede estar vacio")
        @Email(message = "Por favor, ingrese una dirección de correo electrónico válida")
        String email,

        @NotBlank(message = "El campo celular no puede estar vacio")
        String cellPhone,

        @NotBlank(message = "El campo tipo de residencia no puede estar vacio")
        String residencesType,

        String descriptionResidence,

        @NotBlank(message = "El campo barrio no puede estar vacio")
        String neighborhood
) {}


