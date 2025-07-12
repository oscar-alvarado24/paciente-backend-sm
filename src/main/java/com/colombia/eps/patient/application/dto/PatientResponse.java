package com.colombia.eps.patient.application.dto;

public record PatientResponse (
    int id,
    String firstName,
    String secondName,
    String firstSurName,
    String secondSurName,
    String address,
    String email,
    String cellPhone,
    String residencesType,
    String descriptionResidence,
    String neighborhood,
    String photo,
    String status)
{}
