package com.colombia.eps.patient.infrastructure.output.dynamo.config;

import com.colombia.eps.patient.infrastructure.exception.DynamoDbManagerException;
import com.colombia.eps.patient.infrastructure.output.dynamo.entity.PatientEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class DynamoDbManager implements AutoCloseable {
    private final DynamoDbClient dynamoDbClient;
    @Getter
    private final DynamoDbEnhancedClient enhancedClient;
    @Setter
    private boolean closeDynamoClients = false;

    @Override
    public void close() {
        if (this.closeDynamoClients) {
            this.dynamoDbClient.close();
        }
    }

    public DynamoDbTable<PatientEntity> createTable(String tableName){
        return this.enhancedClient.table(tableName, TableSchema.fromBean(PatientEntity.class));
    }
}
