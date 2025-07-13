package com.colombia.eps.patient.infrastructure.config;

import com.colombia.eps.patient.domain.api.IPatientEncryptionServicePort;
import com.colombia.eps.patient.domain.api.IPatientServicePort;
import com.colombia.eps.patient.domain.spi.ICognitoPersistencePort;
import com.colombia.eps.patient.domain.spi.IPatientEncryptionPersistencePort;
import com.colombia.eps.patient.domain.spi.IPatientPersistencePort;
import com.colombia.eps.patient.domain.spi.ISesPersistencePort;
import com.colombia.eps.patient.domain.usecase.PatientEncryptionUseCase;
import com.colombia.eps.patient.domain.usecase.PatientUseCase;
import com.colombia.eps.patient.infrastructure.encryption.adapter.PatientEncryptionAdapter;
import com.colombia.eps.patient.infrastructure.encryption.systemencryption.Encryption;
import com.colombia.eps.patient.infrastructure.output.cognito.adapter.CognitoAdapter;
import com.colombia.eps.patient.infrastructure.output.dynamo.adapter.PatientDynamoAdapter;
import com.colombia.eps.patient.infrastructure.output.dynamo.config.DynamoDBTableValidator;
import com.colombia.eps.patient.infrastructure.output.dynamo.entity.PatientEntity;
import com.colombia.eps.patient.infrastructure.output.dynamo.mapper.IPatientEntityMapper;
import com.colombia.eps.patient.infrastructure.output.dynamo.repository.IPatientRepository;
import com.colombia.eps.patient.infrastructure.output.dynamo.repository.PatientRepository;
import com.colombia.eps.patient.infrastructure.output.ses.adapter.SesAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.sesv2.SesV2Client;

import java.net.URI;

import static com.colombia.eps.library.AwsCredentialGenerate.createCredential;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BeanConfiguration {
    private final AwsCredentialsProvider localCredentials = StaticCredentialsProvider.create(
            AwsBasicCredentials.create("test", "test"));
    private final URI uri = URI.create("http://localhost:4566");
    @Value("${aws.region}") String regionName;


    @Bean
    public IPatientRepository patientRepository() {
        return new PatientRepository();
    }

    @Bean
    @Profile("local")
    public DynamoDbClient localDynamoDbClient(
            @Value("${aws.dynamodb.endpoint}") String endpoint,
            @Value("${aws.dynamodb.access-key}") String accessKey,
            @Value("${aws.dynamodb.secret-key}") String secretKey) {

        log.info("Configuring DynamoDB for local environment with endpoint: {}", endpoint);

        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
        );

        return DynamoDbClient.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(regionName))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    @Profile("!local")
    public DynamoDbClient cloudDynamoDbClient(
            @Value("${aws.region}") String region,
            @Value("${aws.dynamodb.access-key}") String accessKey,
            @Value("${aws.dynamodb.secret-key}") String secretKey,
            @Value("${aws.dynamodb.role}") String role,
            @Value("${aws.dynamodb.session-name}") String sessionName) {

        log.info("Configuring DynamoDB for development environment in region: {}", region);

        AwsCredentialsProvider credential = createCredential(accessKey, secretKey, role, sessionName, Region.of(regionName));

        return DynamoDbClient.builder()
                .region(Region.of(regionName))
                .credentialsProvider(credential)
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public DynamoDBTableValidator dynamoDBTableValidator(
            DynamoDbClient dynamoDbClient,
            @Value("${app.dynamodb.validation.enabled:true}") boolean validationEnabled) {

        DynamoDBTableValidator validator = new DynamoDBTableValidator(dynamoDbClient);
        validator.setValidationEnabled(validationEnabled);

        validator.registerEntity(PatientEntity.class);

        return validator;
    }

    @Bean
    public ICognitoPersistencePort cognitoPersistencePort(CognitoIdentityProviderClient cognitoClient) {
        return new CognitoAdapter(cognitoClient);
    }

    @Bean
    @Profile("local")
    public CognitoIdentityProviderClient localCognitoClient(){
        return CognitoIdentityProviderClient.builder()
                .region(Region.of(regionName))
                .credentialsProvider(this.localCredentials)
                .endpointOverride(this.uri)
                .build();
    }

    @Bean
    @Profile("!local")
    public CognitoIdentityProviderClient cloudCognitoClient(@Value("${aws.cognito.role}") String role,
                                                            @Value("${aws.cognito.access-key}") String accessKey,
                                                            @Value("${aws.cognito.secret-key}") String secretKey,
                                                            @Value("${aws.cognito.session-name}") String sessionName){
        AwsCredentialsProvider credential = createCredential(accessKey, secretKey, role, sessionName, Region.of(regionName));
        return CognitoIdentityProviderClient.builder().region(Region.of(regionName)).credentialsProvider(credential).build();
    }

    @Bean
    public IPatientPersistencePort patientPersistencePort(PatientRepository patientRepository, IPatientEntityMapper patientMapper,DynamoDbClient dynamoDbClient, DynamoDbEnhancedClient dynamoDbEnhancedClient){
        return new PatientDynamoAdapter(patientRepository, patientMapper,dynamoDbClient,dynamoDbEnhancedClient);
    }

    @Bean
    public IPatientServicePort patientServicePort(IPatientPersistencePort patientPersistencePort, ICognitoPersistencePort cognitoPersistencePort, ISesPersistencePort sesPersistencePort){
        return new PatientUseCase(patientPersistencePort, cognitoPersistencePort, sesPersistencePort);
    }

    @Bean
    public IPatientEncryptionPersistencePort patientEncryptionPersistencePort(Encryption encryption) {
        return new PatientEncryptionAdapter(encryption);
    }

    @Bean
    public IPatientEncryptionServicePort patientEncryptionServicePort(Encryption encryption){
        return new PatientEncryptionUseCase(patientEncryptionPersistencePort(encryption));
    }

    @Bean
    public ISesPersistencePort sesPersistencePort(SesClient sesClient, SesV2Client sesV2Client){
        return new SesAdapter(sesClient, sesV2Client);
    }

    @Bean
    @Profile("local")
    public SesV2Client localSesV2Client(){
        return SesV2Client.builder()
                .region(Region.of(regionName))
                .credentialsProvider(this.localCredentials)
                .endpointOverride(this.uri)
                .build();
    }

    @Bean
    @Profile("!local")
    public SesV2Client cloudSesV2Client(@Value("${aws.ses.role}") String role,
                                        @Value("${aws.ses.access-key}") String accessKey,
                                        @Value("${aws.ses.secret-key}") String secretKey,
                                        @Value("${aws.ses.session-name}") String sessionName) {
        AwsCredentialsProvider credential = createCredential(accessKey, secretKey, role, sessionName, Region.of(regionName));
        return SesV2Client.builder()
                .region(Region.of(regionName))
                .credentialsProvider(credential)
                .build();
    }

    @Bean
    @Profile("local")
    public SesClient localSesClient(){
        return SesClient.builder()
                .region(Region.of(regionName))
                .credentialsProvider(this.localCredentials)
                .endpointOverride(this.uri)
                .build();
    }

    @Bean
    @Profile("!local")
    public SesClient cloudSesClient(@Value("${aws.ses.role}") String role,
                                        @Value("${aws.ses.access-key}") String accessKey,
                                        @Value("${aws.ses.secret-key}") String secretKey,
                                        @Value("${aws.ses.session-name}") String sessionName) {
        AwsCredentialsProvider credential = createCredential(accessKey, secretKey, role, sessionName, Region.of(regionName));
        return SesClient.builder()
                .region(Region.of(regionName))
                .credentialsProvider(credential)
                .build();
    }
}
