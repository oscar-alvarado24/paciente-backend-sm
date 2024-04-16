package com.medicine.register.infrastructure.output.dynamo.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "pacientes")
public class PatientEntity {

    @DynamoDBHashKey
    private String id;

    @DynamoDBAttribute
    private String firstName;
    @DynamoDBAttribute
    private String secondName;
    @DynamoDBAttribute
    private String firstSurName;
    @DynamoDBAttribute
    private String secondSurName;
    @DynamoDBAttribute
    private String address;
    @DynamoDBAttribute
    private String email;
    @DynamoDBAttribute
    private String landline;
    @DynamoDBAttribute
    private String cellPhone;
    @DynamoDBAttribute
    private String patient;
    @DynamoDBAttribute
    private String descriptionResidence;
    @DynamoDBAttribute
    private String neighborhood;
    @DynamoDBAttribute
    private String password;
}
