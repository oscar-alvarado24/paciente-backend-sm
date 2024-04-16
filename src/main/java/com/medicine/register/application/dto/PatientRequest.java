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
    @NotEmpty(message = "Debe ingresar su cedula")
    @NotNull(message = "Debe ingresar su cedula")
    private String id;
    @NotEmpty(message = "Debe ingresar un nombre")
    @NotNull(message = "Debe ingresar un nombre")
    private String firstName;
    private String secondName;
    @NotEmpty(message = "Debe ingresar su primer apellido")
    @NotNull(message = "Debe ingresar su primer apellido")
    private String firstSurName;
    @NotEmpty(message = "Debe ingresar su segundo apellido")
    @NotNull(message = "Debe ingresar su segundo apellido")
    private String secondSurName;
    @NotEmpty(message = "La direccion no puede estar vacío")
    @NotNull(message = "La direccion no puede estar vacío")
    private String address;
    @Email(message = "Por favor, ingrese una dirección de correo electrónico válida")
    @NotEmpty(message = "El correo electrónico no puede estar vacío")
    @NotNull(message = "El correo electrónico no puede estar vacío")
    private String email;
    private String landline;
    @NotEmpty(message = "El celular no puede estar vacío")
    @NotNull(message = "El celular no puede estar vacío")
    private String cellPhone;
    @NotEmpty(message = "El tipo de residencia no puede estar vacío")
    @NotNull(message = "El tipo de residencia no puede estar vacío")
    private String residencesType;
    private String descriptionResidence;
    @NotEmpty(message = "El barrio no puede estar vacío")
    @NotNull(message = "El barrio no puede estar vacío")
    private String neighborhood;
    @NotEmpty(message = "La contraseña no puede estar vacío")
    @NotNull(message = "La contraseña no puede estar vacío")
    private String password;
}

