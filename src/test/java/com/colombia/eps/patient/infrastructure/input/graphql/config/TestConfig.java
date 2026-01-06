package com.colombia.eps.patient.infrastructure.input.graphql.config;

import com.colombia.eps.patient.domain.api.IPatientServicePort;
import com.colombia.eps.patient.domain.spi.ICognitoPersistencePort;
import com.colombia.eps.patient.domain.spi.IPatientPersistencePort;
import com.colombia.eps.patient.domain.spi.ISesPersistencePort;
import com.colombia.eps.patient.domain.spi.ISqsPersistencePort;
import com.colombia.eps.patient.domain.usecase.PatientUseCase;
import com.colombia.eps.patient.infrastructure.output.cognito.adapter.CognitoAdapter;
import com.colombia.eps.patient.infrastructure.output.dynamo.adapter.PatientDynamoAdapter;
import com.colombia.eps.patient.infrastructure.output.dynamo.mapper.IPatientEntityMapper;
import com.colombia.eps.patient.infrastructure.output.dynamo.repository.IPatientRepository;
import com.colombia.eps.patient.infrastructure.output.ses.adapter.SesAdapter;
import com.colombia.eps.patient.infrastructure.output.sqs.adpter.SqsAdapter;
import lombok.RequiredArgsConstructor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;
import java.time.Duration;

@TestConfiguration
@RequiredArgsConstructor
@Profile("test-integration")
public class TestConfig {
    @Value("${dynamodb.endpoint}")
    private String dynamodbEndpoint;
    @Value("${dynamodb.region}")
    private String dynamodbRegion;

    @Value("${cloud.aws.endpoint}")
    private String sesSqsEndpoint;
    @Value("${aws.region}")
    private String awsRegion;
    @Value("${cognito.user.pool.id}")
    String userPoolId;
    @Value("${cognito.patient.group}")
    String patientGroup;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(dynamodbEndpoint))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
                .region(Region.of(dynamodbRegion))
                .overrideConfiguration(b -> b
                        .apiCallTimeout(Duration.ofSeconds(180)))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    // Cliente SES
    @Bean
    public SesClient sesClient(){
        return SesClient.builder()
                .endpointOverride(URI.create(sesSqsEndpoint))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
                .region(Region.of(awsRegion))
                .overrideConfiguration(b -> b
                        .apiCallTimeout(Duration.ofSeconds(180)))
                .build();
    }

    // Cliente SQS
    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .endpointOverride(URI.create(sesSqsEndpoint))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
                .region(Region.of(awsRegion))
                .overrideConfiguration(b -> b
                        .apiCallTimeout(Duration.ofSeconds(180)))
                .build();
    }

    @Bean
    @Primary
    public ICognitoPersistencePort cognitoPersistencePortForTest(CognitoIdentityProviderClient cognitoClient) {
        return new CognitoAdapter(cognitoClient,userPoolId, patientGroup);
    }

    @Bean
    @Primary
    public CognitoIdentityProviderClient cognitoClient() {
        return Mockito.mock(CognitoIdentityProviderClient.class);
    }
    @Bean
    public ISesPersistencePort sesPersistencePortForTest(SesClient sesClientForTest) {
        System.out.println("Creando SesAdapter para test"); // Log para debug
        return new SesAdapter(sesClientForTest);
    }

    @Bean
    public ISqsPersistencePort sqsPersistencePortForTest(SqsClient sqsClientForTest, @Value("${aws.sqs.queue.url}") String queueUrl) {
        System.out.println("Creando SqsAdapter para test, queueUrl: " + queueUrl); // Log para debug
        return new SqsAdapter(sqsClientForTest, queueUrl); // Asegúrate de que SqsAdapter acepte queueUrl como @Value o parámetro
    }

    @Bean
    public IPatientPersistencePort patientPersistencePortForTest(
            IPatientRepository patientRepository,
            IPatientEntityMapper patientEntityMapper, // Inyectado como bean
            DynamoDbClient dynamoDbClientForTest,
            DynamoDbEnhancedClient dynamoDbEnhancedClientForTest) {
        System.out.println("Creando PatientDynamoAdapter para test"); // Log para debug
        return new PatientDynamoAdapter(patientRepository, patientEntityMapper, dynamoDbClientForTest, dynamoDbEnhancedClientForTest);
    }

    // --- UseCase ---
    @Bean
    @Primary
    public IPatientServicePort patientServicePortForTest(
            IPatientPersistencePort patientPersistencePort,
            ICognitoPersistencePort cognitoPersistencePort,
            ISesPersistencePort sesPersistencePort,
            ISqsPersistencePort sqsPersistencePort) {
        System.out.println("Creando PatientUseCase para test"); // Log para debug
        return new PatientUseCase(patientPersistencePort, cognitoPersistencePort, sesPersistencePort, sqsPersistencePort);
    }
}
