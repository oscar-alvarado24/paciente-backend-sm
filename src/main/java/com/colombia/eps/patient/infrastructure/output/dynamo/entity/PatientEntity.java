package com.colombia.eps.patient.infrastructure.output.dynamo.entity;


import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@DynamoDbBean
public class PatientEntity {

    private String email;
    @Getter
    private String firstName;
    @Getter
    private String secondName;
    @Getter
    private String firstSurName;
    @Getter
    private String secondSurName;
    @Getter
    private String address;
    @Getter
    private String landline;
    @Getter
    private String cellPhone;
    @Getter
    private String patient;
    @Getter
    private String descriptionResidence;
    @Getter
    private String neighborhood;

    @DynamoDbPartitionKey
    public String getEmail() {
        return email;
    }
}
