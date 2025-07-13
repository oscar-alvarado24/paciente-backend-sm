package com.colombia.eps.patient.infrastructure.output.dynamo.config;

import com.colombia.eps.patient.infrastructure.exception.DynamoTableNotCreatedException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DynamoDBTableValidator {

    private final DynamoDbClient dynamoDbClient;
    private final List<Class<?>> entityClasses = new ArrayList<>();

    @Setter
    private boolean validationEnabled = true;

    public void registerEntity(Class<?> entityClass) {
        if (entityClass.isAnnotationPresent(DynamoDbBean.class)) {
            entityClasses.add(entityClass);
        }
    }

    @PostConstruct
    public void validateTables() {
        if (!validationEnabled) {
            log.info("DynamoDB table validation is disabled");
            return;
        }

        for (Class<?> entityClass : entityClasses) {
            validateTableExists(entityClass.getSimpleName().toLowerCase().replace("entity",""));
        }
    }


    private void validateTableExists(String tableName) {
        try {
            dynamoDbClient.describeTable(DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build());
            log.info("Table {} exists", tableName);
        } catch (ResourceNotFoundException e) {
            log.error("Table {} does not exist", tableName);
            throw new DynamoTableNotCreatedException("Required DynamoDB table '" + tableName + "' does not exist");
        }
    }
}
