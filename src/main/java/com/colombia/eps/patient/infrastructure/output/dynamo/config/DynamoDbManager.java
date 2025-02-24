package com.colombia.eps.patient.infrastructure.output.dynamo.config;

import com.colombia.eps.patient.infrastructure.exception.DynamoDbManagerException;
import com.colombia.eps.patient.infrastructure.output.dynamo.entity.PatientEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import com.colombia.eps.patient.infrastructure.util.Constants;

import static com.colombia.eps.library.AwsCredentialGenerate.createCredential;

@Slf4j
public class DynamoDbManager implements AutoCloseable {
    private final DynamoDbClient dynamoDbClient;
    @Getter
    private final DynamoDbEnhancedClient enhancedClient;


    public DynamoDbManager() {
        try {
            String dynamoRole = System.getenv(Constants.DYNAMO_ROL);
            String accessKeyId = System.getenv(Constants.VE_AKI_DYNAMO_USER);
            String secretAccessKey = System.getenv(Constants.VE_SAK_DYNAMO_USER);
            Region region = Region.of(System.getenv(Constants.VE_REGION));
            AwsCredentialsProvider credential = createCredential(accessKeyId, secretAccessKey, dynamoRole, Constants.ROLE_SESSION_NAME_DYNAMO, region);

            this.dynamoDbClient = DynamoDbClient.builder()
                    .region(region)
                    .credentialsProvider(credential)
                    .build();
            this.enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(dynamoDbClient)
                    .build();

        }
        catch (Exception exception){
            log.error(exception.getMessage(), exception);
            throw new DynamoDbManagerException(exception.getMessage());
        }
    }

    @Override
    public void close() {
        if (this.dynamoDbClient != null) {
            this.dynamoDbClient.close();
        }
    }

    public DynamoDbTable<PatientEntity> createTable(String tableName){
        return this.enhancedClient.table(tableName, TableSchema.fromBean(PatientEntity.class));
    }
}

