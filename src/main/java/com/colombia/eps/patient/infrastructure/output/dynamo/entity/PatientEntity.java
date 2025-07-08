package com.colombia.eps.patient.infrastructure.output.dynamo.entity;


import com.colombia.eps.patient.infrastructure.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@DynamoDbBean
public class PatientEntity {

    private String id;
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
    private String residencesType;
    @Getter
    private String descriptionResidence;
    @Getter
    private String neighborhood;
    @Getter
    private Status status;
    @Getter
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
