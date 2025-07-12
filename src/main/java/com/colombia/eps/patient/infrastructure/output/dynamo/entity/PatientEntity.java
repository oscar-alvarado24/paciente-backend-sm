package com.colombia.eps.patient.infrastructure.output.dynamo.entity;


import com.colombia.eps.patient.infrastructure.helper.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@DynamoDbBean
public class PatientEntity {

    private String id;
    private String email;
    private String firstName;
    private String secondName;
    private String firstSurName;
    private String secondSurName;
    private String address;
    private String landline;
    private String cellPhone;
    private String residencesType;
    private String descriptionResidence;
    private String neighborhood;
    private Status status;
    private String photo;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = Constants.EMAIL_INDEX)
    public String getEmail() {
        return email;
    }
}
