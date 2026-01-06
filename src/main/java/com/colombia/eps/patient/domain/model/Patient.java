package com.colombia.eps.patient.domain.model;

public class Patient {
    private int id;
    private String firstName;
    private String secondName;
    private String firstSurName;
    private String secondSurName;
    private String address;
    private String email;
    private String cellPhone;
    private String landline;
    private String residencesType;
    private String descriptionResidence;
    private String neighborhood;
    private String photo;
    private String status;
    private String program;

    public Patient() { /*  constructor empty for mapper process*/ }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getFirstSurName() {
        return firstSurName;
    }

    public void setFirstSurName(String firstSurName) {
        this.firstSurName = firstSurName;
    }

    public String getSecondSurName() {
        return secondSurName;
    }

    public void setSecondSurName(String secondSurName) {
        this.secondSurName = secondSurName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public String getResidencesType() {
        return residencesType;
    }

    public void setResidencesType(String residencesType) {
        this.residencesType = residencesType;
    }

    public String getDescriptionResidence() {
        return descriptionResidence;
    }

    public void setDescriptionResidence(String descriptionResidence) {
        this.descriptionResidence = descriptionResidence;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", firstSurName='" + firstSurName + '\'' +
                ", secondSurName='" + secondSurName + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", cellPhone='" + cellPhone + '\'' +
                ", residencesType='" + residencesType + '\'' +
                ", descriptionResidence='" + descriptionResidence + '\'' +
                ", neighborhood='" + neighborhood + '\'' +
                ", photo='" + photo + '\'' +
                ", status='" + status + '\'' +
                ", program='" + program + '\'' +
                '}';
    }
}
