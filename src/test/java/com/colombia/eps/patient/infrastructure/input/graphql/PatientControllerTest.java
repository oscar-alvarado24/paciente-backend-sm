package com.colombia.eps.patient.infrastructure.input.graphql;

import com.colombia.eps.patient.application.helper.CryptoUtil;
import com.colombia.eps.patient.domain.model.Patient;
import com.colombia.eps.patient.domain.spi.IPatientPersistencePort;
import com.colombia.eps.patient.infrastructure.input.graphql.config.TestConfig;
import com.colombia.eps.patient.infrastructure.output.dynamo.entity.PatientEntity;
import com.colombia.eps.patient.infrastructure.output.dynamo.entity.Status;
import com.colombia.eps.patient.infrastructure.output.dynamo.repository.IPatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.ResponseError;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.BillingMode;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.ListIdentitiesResponse;
import software.amazon.awssdk.services.ses.model.VerifyEmailIdentityRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test-integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(locations = "classpath:application-test.properties")
@Tag("integration")
@Import(TestConfig.class)
@Slf4j
@RequiredArgsConstructor
class PatientControllerTest {

    private static final String TABLE_NAME = "patient";
    private static final String EMAIL = "email@example.com";
    private static final String PHOTO = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAYEBQYFBAYGBQYHBwYIChAKCgkJChQODwwQFxQYGBcUFhYaHSUfGhsjHBYWICwgIyYnKSopGR8tMC0oMCUoKSj/2wBDAQcHBwoIChMKChMoGhYaKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCj/wAARCAABAAEDASIAAhEBAxEB/8QAFQABAQAAAAAAAAAAAAAAAAAAAAv/xAAUEAEAAAAAAAAAAAAAAAAAAAAA/8QAFQEBAQAAAAAAAAAAAAAAAAAAAAX/xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oADAMBAAIRAxEAPwCdABmX/9k=";
    private static final Patient patientToSave = new Patient();
    @SuppressWarnings("resource")
    @Container
    static GenericContainer<?> localstack = new GenericContainer<>(DockerImageName.parse("localstack/localstack")).withExposedPorts(4566).waitingFor(Wait.forHttp("/_localstack/health").forStatusCode(200));
    private static String id;
    private static String queueUrl;
    private static String dynamodbEndpoint;
    private static String sesSqsEndpoint;
    @LocalServerPort
    private int port;
    private GraphQlTester graphQlTester;
    @Autowired
    private IPatientPersistencePort patientPersistencePort;
    @Autowired
    private CognitoIdentityProviderClient cognitoClient;
    @Autowired
    private SqsClient sqsClient;
    @Autowired
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;
    @Autowired
    private SesClient sesClient;
    @Autowired
    private IPatientRepository patientRepository;
    @Mock
    private AdminCreateUserResponse adminCreateUserResponse;

    // Configuración dinámica de propiedades para que los clientes de AWS apunten a LocalStack
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        int mappedPort = localstack.getMappedPort(4566);
        dynamodbEndpoint = "http://localhost:" + mappedPort;
        sesSqsEndpoint = "http://localhost:" + mappedPort;

        registry.add("dynamodb.endpoint", () -> dynamodbEndpoint);
        registry.add("cloud.aws.endpoint", () -> sesSqsEndpoint);
        registry.add("aws.sqs.queue.url", () -> queueUrl);
    }

    @BeforeAll
    static void setUpAwsServices() {
        createAwsResources();
    }

    private static void createAwsResources() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create("test", "test");
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(awsCredentials);

        String endpoint = dynamodbEndpoint != null ? dynamodbEndpoint : "http://localhost:" + localstack.getMappedPort(4566);

        log.info("🔧 Creating AWS resources with endpoint: {}", endpoint);

        try (SqsClient tempSqsClient = SqsClient.builder().endpointOverride(URI.create(endpoint)).credentialsProvider(credentialsProvider).region(Region.US_EAST_1).build()) {

            try {
                String queueName = "patient-queue-" + UUID.randomUUID();
                tempSqsClient.createQueue(CreateQueueRequest.builder().queueName(queueName).build());

                String internalQueueUrl = tempSqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build()).queueUrl();

                // Asegurar que la URL use el endpoint correcto
                queueUrl = internalQueueUrl.replace("sqs.us-east-1.localhost.localstack.cloud:4566", "localhost:" + localstack.getMappedPort(4566));

                log.info("✅ Queue URL: {}", queueUrl);
            } catch (Exception e) {
                log.warn("⚠️ Could not create SQS queue: {}", e.getMessage());
            }
        } catch (Exception e) {
            log.error("❌ Error creating SQS client: {}", e.getMessage());
        }

        try (DynamoDbClient tempDynamoDbClient = DynamoDbClient.builder().endpointOverride(URI.create(endpoint)).credentialsProvider(credentialsProvider).region(Region.US_EAST_1).build()) {

            try {
                tempDynamoDbClient.createTable(CreateTableRequest.builder().tableName(TABLE_NAME).keySchema(KeySchemaElement.builder().attributeName("id").keyType(KeyType.HASH).build()).attributeDefinitions(AttributeDefinition.builder().attributeName("id").attributeType(ScalarAttributeType.S).build(), AttributeDefinition.builder().attributeName("email").attributeType(ScalarAttributeType.S).build()).globalSecondaryIndexes(software.amazon.awssdk.services.dynamodb.model.GlobalSecondaryIndex.builder().indexName("email-index").keySchema(KeySchemaElement.builder().attributeName("email").keyType(KeyType.HASH).build()).projection(software.amazon.awssdk.services.dynamodb.model.Projection.builder().projectionType("ALL").build()).build()).billingMode(BillingMode.PAY_PER_REQUEST).build());

                log.info("✅ DynamoDB table '{}' created", TABLE_NAME);
            } catch (ResourceInUseException e) {
                log.info("ℹ️ Table '{}' already exists", TABLE_NAME);
            } catch (Exception e) {
                log.error("❌ Error creating DynamoDB table: {}", e.getMessage());
            }
        } catch (Exception e) {
            log.error("❌ Error creating DynamoDB client: {}", e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        if (id != null) {
            Key key = Key.builder().partitionValue(id).build();
            dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(PatientEntity.class)).deleteItem(key);
        }
    }

    @BeforeEach
    void setUp() {
        id = null;
        String baseUrl = "http://localhost:" + port + "/graphql";
        WebTestClient webClient = WebTestClient.bindToServer().baseUrl(baseUrl).build().mutate().responseTimeout(Duration.ofSeconds(200))
                .build();
        this.graphQlTester = HttpGraphQlTester.create(webClient);
    }

    //Test for create method
    @Test
    void shouldCreatePatientSuccessfullyAndInteractWithAllServices() {
        when(cognitoClient.adminCreateUser(any(AdminCreateUserRequest.class)))
                .thenReturn(adminCreateUserResponse);
        when(cognitoClient.adminAddUserToGroup(any(AdminAddUserToGroupRequest.class)))
                .thenReturn(AdminAddUserToGroupResponse.builder().build());

        String responseMessage = graphQlTester.document("""
            mutation {
                createPatient(
                    id: 12345
                    firstName: "Juan"
                    secondName: "Carlos"
                    firstSurName: "Pérez"
                    secondSurName: "Gómez"
                    address: "Calle 123"
                    email: "juan.perez@example.com"
                    cellPhone: "3001234567"
                    residencesType: "Casa"
                    descriptionResidence: "Cerca del parque"
                    neighborhood: "Centro"
                    program: "Programa1"
                )
            }
            """).execute()
                .path("createPatient")
                .entity(String.class)
                .get();

        assertThat(responseMessage).contains("Paciente Juan Pérez creado satisfactoriamente");
        verify(cognitoClient, times(1)).adminCreateUser(any(AdminCreateUserRequest.class));
        verify(cognitoClient, times(1)).adminAddUserToGroup(any(AdminAddUserToGroupRequest.class));

        ListIdentitiesResponse response = sesClient.listIdentities();
        assertThat(response.identities()).contains("juan.perez@example.com");

        PatientEntity savedPatient = patientRepository.findPatientByEmail(
                "juan.perez@example.com",
                dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(PatientEntity.class))
        ).orElse(null);

        assertThat(savedPatient).isNotNull();
        assertThat(savedPatient.getFirstName()).isEqualTo("Juan");
        assertThat(savedPatient.getEmail()).isEqualTo("juan.perez@example.com");

        await().atMost(2L, TimeUnit.SECONDS).until(getQueueMessage(savedPatient.getProgram()));
        id = savedPatient.getId();
    }

    @Test
    @DirtiesContext
    void shouldReturnErrorCreateSesIdentityWhenStopContainerInCreatePatientFlow() {
        localstack.stop();

        GraphQlTester.Response response = createPatient(12345, EMAIL);

        response.errors().satisfy(errors -> {
            assertThat(errors).hasSize(1);

            ResponseError error = errors.get(0);

            assertThat(error.getMessage()).contains("Error al crear la identidad");

            Map<String, Object> extensions = error.getExtensions();
            assertThat(extensions).isNotNull();
            assertThat(extensions).containsEntry("code", "CREATE_SES_IDENTITY");
            assertThat(extensions).containsEntry("classification", "INTERNAL_ERROR");
            assertThat(extensions).containsKey("timestamp");
        });

        response.path("createPatient").valueIsNull();
        startLocalStackContainer();

    }

    @Test
    void shouldReturnErrorWhenPatientExistWithTheSameId() {
        savePatientInDBFromUseCase();
        GraphQlTester.Response response = createPatient(Integer.parseInt(id), "otherEmail@example.com");

        response.errors().satisfy(errors -> {
            assertThat(errors).hasSize(1);

            ResponseError error = errors.get(0);

            assertThat(error.getMessage()).contains("id");
            assertThat(error.getMessage()).doesNotContain("email");

            Map<String, Object> extensions = error.getExtensions();
            assertThat(extensions).isNotNull();
            assertThat(extensions).containsEntry("code", "PATIENT_ALREADY_EXIST");
            assertThat(extensions).containsEntry("classification", "BAD_REQUEST");
            assertThat(extensions).containsKey("timestamp");
        });

        response.path("createPatient").valueIsNull();
    }

    @Test
    void shouldReturnErrorWhenPatientExistWithTheSameEmail() {
        savePatientInDBFromUseCase();

        GraphQlTester.Response response = createPatient(369258, patientToSave.getEmail());

        response.errors().satisfy(errors -> {
            assertThat(errors).hasSize(1);

            ResponseError error = errors.get(0);

            assertThat(error.getMessage()).contains("email");
            assertThat(error.getMessage()).doesNotContain("id");
            Map<String, Object> extensions = error.getExtensions();
            assertThat(extensions).isNotNull();
            assertThat(extensions).containsEntry("code", "PATIENT_ALREADY_EXIST");
            assertThat(extensions).containsEntry("classification", "BAD_REQUEST");
            assertThat(extensions).containsKey("timestamp");
        });

        response.path("createPatient").valueIsNull();
    }

    @Test
    void shouldReturnErrorWhenPatientExistWithTheSameIdAndEmail() {
        savePatientInDBFromUseCase();
        GraphQlTester.Response response = createPatient(Integer.parseInt(id), patientToSave.getEmail());

        response.errors().satisfy(errors -> {
            assertThat(errors).hasSize(1);

            ResponseError error = errors.get(0);

            assertThat(error.getMessage()).contains(" y id ");

            Map<String, Object> extensions = error.getExtensions();
            assertThat(extensions).isNotNull();
            assertThat(extensions).containsEntry("code", "PATIENT_ALREADY_EXIST");
            assertThat(extensions).containsEntry("classification", "BAD_REQUEST");
            assertThat(extensions).containsKey("timestamp");
        });

        response.path("createPatient").valueIsNull();
    }

    @Test
    void shouldReturnCreateUserInUserPoolException(){
        when(cognitoClient.adminCreateUser(any(AdminCreateUserRequest.class)))
                .thenThrow(CognitoIdentityProviderException.builder().message("Error creating user").build());
        GraphQlTester.Response response = createPatient(12345, EMAIL);

        response.errors().satisfy(errors -> {
            assertThat(errors).hasSize(1);

            ResponseError error = errors.get(0);

            assertThat(error.getMessage()).contains("No se pudo crear en Cognito el paciente");

            Map<String, Object> extensions = error.getExtensions();
            assertThat(extensions).isNotNull();
            assertThat(extensions).containsEntry("code", "CREATE_USER_IN_USER_POOL");
            assertThat(extensions).containsEntry("classification", "INTERNAL_ERROR");
            assertThat(extensions).containsKey("timestamp");
        });
    }

    @Test
    void shouldReturnAddUserToGroupException() {
        when(cognitoClient.adminCreateUser(any(AdminCreateUserRequest.class)))
                .thenReturn(adminCreateUserResponse);
        when(cognitoClient.adminAddUserToGroup(any(AdminAddUserToGroupRequest.class)))
                .thenThrow(CognitoIdentityProviderException.builder().message("Error adding the patient to the group").build());
        GraphQlTester.Response response = createPatient(12345, EMAIL);

        verify(cognitoClient, times(1)).adminCreateUser(any(AdminCreateUserRequest.class));
        verify(cognitoClient, times(1)).adminAddUserToGroup(any(AdminAddUserToGroupRequest.class));

        response.errors().satisfy(errors -> {
            assertThat(errors).hasSize(1);

            ResponseError error = errors.get(0);

            assertThat(error.getMessage()).contains("No se pudo agregar al paciente");

            Map<String, Object> extensions = error.getExtensions();
            assertThat(extensions).isNotNull();
            assertThat(extensions).containsEntry("code", "ADD_USER_TO_GROUP");
            assertThat(extensions).containsEntry("classification", "INTERNAL_ERROR");
            assertThat(extensions).containsKey("timestamp");
        });
    }

    //Test for get patient method
    @Test
    @SuppressWarnings("unchecked")
    void shouldGetPatientByEmailSuccessfully() {
        savePatientInDBFromUseCase();
        Map<String, Object> response = (Map<String, Object>) graphQlTester.document("""
                query getPatient($email: String!) {
                    getPatient(email: $email) {
                        firstName
                        email
                        status
                    }
                }
                """).variable("email", patientToSave.getEmail()).execute().path("getPatient").entity(Map.class).get(); // Obtiene el objeto paciente

        assertThat(response).isNotNull();
        assertThat(CryptoUtil.decrypt(String.valueOf(response.get("firstName")))).isEqualTo(patientToSave.getFirstName());
        assertThat(CryptoUtil.decrypt(String.valueOf(response.get("email")))).isEqualTo(patientToSave.getEmail());
        assertThat(response).containsEntry("status", "usuario_activo");
    }

    @Test
    void shouldReturnErrorWhenGetPatientByEmailNotFound() {
        GraphQlTester.Response response = graphQlTester.document("""
                query {
                    getPatient(email: "email_not_registred@example.com") {
                        firstName
                        email
                        status
                    }
                }
                """).execute();

        response.errors().satisfy(errors -> {
            assertThat(errors).hasSize(1);

            ResponseError error = errors.get(0);

            assertThat(error.getMessage()).contains("El paciente con ");

            Map<String, Object> extensions = error.getExtensions();
            assertThat(extensions).isNotNull();
            assertThat(extensions).containsEntry("code", "PATIENT_NOT_FOUND");
            assertThat(extensions).containsEntry("classification", "NOT_FOUND");
            assertThat(extensions).containsKey("timestamp");
        });

        response.path("getPatient").valueIsNull();
    }

    @Test
    @DirtiesContext
    void shouldReturnErrorWhenCannotCommunicateWithDynamoInTheGetPatientFlow() {
        localstack.stop();
        GraphQlTester.Response response = graphQlTester.document("""
                query {
                    getPatient(email: "email@example.com") {
                        firstName
                        email
                        status
                    }
                }
                """).execute();

        response.errors().satisfy(errors -> {
            assertThat(errors).hasSize(1);

            ResponseError error = errors.get(0);

            assertThat(error.getMessage()).contains("Error al obtener los datos");

            Map<String, Object> extensions = error.getExtensions();
            assertThat(extensions).isNotNull();
            assertThat(extensions).containsEntry("code", "GET_PATIENT");
            assertThat(extensions).containsEntry("classification", "INTERNAL_ERROR");
            assertThat(extensions).containsKey("timestamp");
        });

        response.path("getPatient").valueIsNull();
        startLocalStackContainer();
    }

    @Test
    @SuppressWarnings({"unchecked"})
    void shouldReturnPatientWithStatusInactiveSecondNameAndWithoutDescriptionResidence(){
        PatientEntity patient = new PatientEntity();
        patient.setId("24680");
        patient.setFirstName("Pedro");
        patient.setSecondName("Manuel");
        patient.setFirstSurName("Lopez");
        patient.setSecondSurName("Pérez");
        patient.setEmail("pedro.peres@example.com");
        patient.setCellPhone("3001234567");
        patient.setResidencesType("Casa");
        patient.setAddress("calle 12 # 34-56");
        patient.setNeighborhood("Centro");
        patient.setProgram("Programa1");
        patient.setStatus(Status.INACTIVE);
        patient.setPhoto(PHOTO);
        dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(PatientEntity.class)).putItem(patient);
        id= patient.getId();
        Map<String, Object> response = (Map<String, Object>) graphQlTester.document("""
                query getPatient($email: String!) {
                    getPatient(email: $email) {
                        secondName
                        photo
                        status
                    }
                }
                """).variable("email", patient.getEmail()).execute().path("getPatient").entity(Map.class).get(); // Obtiene el objeto paciente

        // Then: Verificar la respuesta de GraphQL
        assertThat(response).isNotNull();
        assertThat(CryptoUtil.decrypt(String.valueOf(response.get("secondName")))).isEqualTo(patient.getSecondName());
        assertThat(CryptoUtil.decrypt(String.valueOf(response.get("photo")))).isEqualTo(patient.getPhoto());
        assertThat(response).containsEntry("status", "usuario_inactivo");
    }

    @Test
    @SuppressWarnings({"unchecked"})
    void shouldReturnPatientWithStatusRetired(){
        PatientEntity patient = new PatientEntity();
        patient.setId("24680");
        patient.setFirstName("Pedro");
        patient.setSecondName("  ");
        patient.setFirstSurName("Lopez");
        patient.setSecondSurName("Pérez");
        patient.setEmail("pedro.peres@example.com");
        patient.setCellPhone("3001234567");
        patient.setResidencesType("Casa");
        patient.setAddress("calle 12 # 34-56");
        patient.setNeighborhood("Centro");
        patient.setProgram("Programa1");
        patient.setStatus(Status.RETIRED);
        patient.setDescriptionResidence(" ");
        dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(PatientEntity.class)).putItem(patient);
        id= patient.getId();
        Map<String, Object> response = (Map<String, Object>) graphQlTester.document("""
                query getPatient($email: String!) {
                    getPatient(email: $email) {
                        secondName
                        photo
                        status
                        descriptionResidence
                    }
                }
                """).variable("email", patient.getEmail()).execute().path("getPatient").entity(Map.class).get();

        assertThat(response).isNotNull();
        assertThat(String.valueOf(response.get("secondName"))).isEmpty();
        assertThat(String.valueOf(response.get("photo"))).isEmpty();
        assertThat(String.valueOf(response.get("descriptionResidence"))).isEmpty();
        assertThat(response).containsEntry("status", "usuario_retirado");
    }

    //Test for validate patient status in ses
    @Test
    void shouldValidateSesRegistrationStatus() {
        String emailToVerify = "verify.me@example.com";
        sesClient.verifyEmailIdentity(VerifyEmailIdentityRequest.builder().emailAddress(emailToVerify).build());

        String statusMessage = graphQlTester.document("""
                        query validateStatusSesRegistration($email: String!) {
                            validateStatusSesRegistration(email: $email)
                        }
                        """).variable("email", CryptoUtil.encrypt(emailToVerify))
                .execute().path("validateStatusSesRegistration").entity(String.class).get();

        assertThat(statusMessage).isIn("Email_verificado", "Verificación pendiente"); // Ajusta según tu lógica
    }

    @Test
    @DirtiesContext
    void shouldReturnErrorWhenStopContainerBeforeValidateSesRegistrationStatusFlow() {
        localstack.stop();
        GraphQlTester.Response response = graphQlTester.document("""
                query validateStatusSesRegistration($email: String!) {
                    validateStatusSesRegistration(email: $email)
                }
                """).variable("email", CryptoUtil.encrypt("not_registered@example.com")).execute();

        response.errors().satisfy(errors -> {
            assertThat(errors).hasSize(1);

            ResponseError error = errors.get(0);

            assertThat(error.getMessage()).contains("Error al obtener el estado de verificación");

            Map<String, Object> extensions = error.getExtensions();
            assertThat(extensions).isNotNull();
            assertThat(extensions).containsEntry("code", "GET_VERIFICATION_STATUS_IN_SES");
            assertThat(extensions).containsEntry("classification", "INTERNAL_ERROR");
            assertThat(extensions).containsKey("timestamp");
        });

        response.path("validateStatusSesRegistration").valueIsNull();

        startLocalStackContainer();
    }

    //Test for change patient status in Dynamo
    @Test
    void shouldChangePatientStatusSuccessfully() {
        savePatientInDBFromUseCase();
        graphQlTester.document("""
                mutation {
                    changeStatus(id: 12345, status: "INACTIVE")
                }
                """).execute().path("changeStatus").entity(String.class).matches(result -> result.contains("Paciente actualizado de forma correcta"));

        Patient patient = patientPersistencePort.getPatient(patientToSave.getEmail());
        log.info("Patient: {}", patient);
        assertThat(patient).isNotNull();
        assertEquals(Status.INACTIVE.name(), patient.getStatus());
    }

    @Test
    void shouldReturnErrorWhenChangeStatusForPatientNotFound() {
        GraphQlTester.Response response = graphQlTester.document("""
                mutation {
                    changeStatus(id: 12345, status: "INACTIVE")
                }
                """).execute();
        response.errors().satisfy(errors -> {
            assertThat(errors).hasSize(1);

            ResponseError error = errors.get(0);

            assertThat(error.getMessage()).contains("El paciente con ");

            Map<String, Object> extensions = error.getExtensions();
            assertThat(extensions).isNotNull();
            assertThat(extensions).containsEntry("code", "PATIENT_NOT_FOUND");
            assertThat(extensions).containsEntry("classification", "NOT_FOUND");
            assertThat(extensions).containsKey("timestamp");
        });

        response.path("changeStatus").valueIsNull();
    }

    @Test
    @DirtiesContext
    void shouldReturnErrorWhenCannotCommunicateWithDynamoInChangeStatusFlow() {
        localstack.stop();
        GraphQlTester.Response response = graphQlTester.document("""
                mutation {
                    changeStatus(id: 12345, status: "INACTIVE")
                }
                """).execute();

        response.errors().satisfy(errors -> {
            assertThat(errors).hasSize(1);

            ResponseError error = errors.get(0);

            assertThat(error.getMessage()).contains("Error al actualizar el estado");

            Map<String, Object> extensions = error.getExtensions();
            assertThat(extensions).isNotNull();
            assertThat(extensions).containsEntry("code", "CHANGE_PATIENT_STATUS");
            assertThat(extensions).containsEntry("classification", "INTERNAL_ERROR");
            assertThat(extensions).containsKey("timestamp");
        });

        response.path("changeStatus").valueIsNull();
        startLocalStackContainer();
    }

    //Test for save photo method
    @Test
    void shouldSavePhotoSuccessfully() {
        savePatientInDBFromUseCase();

        GraphQlTester.Response response = graphQlTester.document("""
                mutation savePhoto ($email: String!, $photo: String!){
                    savePhoto(email: $email, photo: $photo)
                }
                """).variable("email", patientToSave.getEmail()).variable("photo", PHOTO).execute();

        response.errors().verify();

        String responseMessage = response.path("savePhoto").entity(String.class).get();
        assertThat(responseMessage).contains("Paciente actualizado de forma correcta");
        PatientEntity updatedPatient = patientRepository.findPatientByEmail(patientToSave.getEmail(), dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(PatientEntity.class))).orElse(null);
        assertThat(updatedPatient).isNotNull();
        assertThat(updatedPatient.getPhoto()).isEqualTo(PHOTO);
    }

    @Test
    void shouldReturnErrorWhenSavingPhotoForNonExistentPatient() {
        GraphQlTester.Response response = graphQlTester.document("""
                mutation {
                    savePhoto(email: "noexiste@example.com", photo: "%s")
                }
                """.formatted(PHOTO)).execute();

        validateNotFoundException(response, "savePhoto");
    }

    @Test
    @DirtiesContext
    void shouldReturnErrorWhenCannotCommunicateWithDynamoInSavePhotoFlow() {
        localstack.stop();
        GraphQlTester.Response response = graphQlTester.document("""
                mutation {
                    savePhoto(email: "noexiste@example.com", photo: "data:image/jpeg;base64,/9j/4AAQSkZJRg...")
                }
                """).execute();

        response.errors().satisfy(errors -> {
            assertThat(errors).hasSize(1);

            ResponseError error = errors.get(0);

            assertThat(error.getMessage()).contains("Error al guardar la ");

            Map<String, Object> extensions = error.getExtensions();
            assertThat(extensions).isNotNull();
            assertThat(extensions).containsEntry("code", "SAVE_PHOTO_TO_PATIENT");
            assertThat(extensions).containsEntry("classification", "INTERNAL_ERROR");
            assertThat(extensions).containsKey("timestamp");
        });

        response.path("savePhoto").valueIsNull();
        startLocalStackContainer();
    }

    //Test for update patient method
    @Test
    void shouldUpdatePatientSuccessfully() {
        savePatientInDBFromUseCase();

        GraphQlTester.Response response = graphQlTester.document("""
                mutation {
                    updatePatient(
                        id: 12345
                        firstName: "Actualizado"
                        secondName: "Nuevo"
                        firstSurName: "Apellido"
                        secondSurName: "Segundo"
                        address: "Calle Nueva 456"
                        email: "update.test@example.com"
                        landline: "6011111111"
                        cellPhone: "3002222222"
                        residencesType: "Casa"
                        descriptionResidence: "Cerca de la montaña"
                        neighborhood: "Sur"
                        program: "Programa1"
                    )
                }
                """).execute();

        response.errors().verify();

        String responseMessage = response.path("updatePatient").entity(String.class).get();
        assertThat(responseMessage).contains("Paciente actualizado de forma correcta");
        PatientEntity updatedPatient = patientRepository.findPatientByEmail("update.test@example.com", dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(PatientEntity.class))).orElseThrow(() -> new AssertionError("Paciente no encontrado después de actualizar"));

        assertThat(updatedPatient.getFirstName()).isEqualTo("Actualizado");
        assertThat(updatedPatient.getSecondName()).isEqualTo("Nuevo");
        assertThat(updatedPatient.getFirstSurName()).isEqualTo("Apellido");
        assertThat(updatedPatient.getSecondSurName()).isEqualTo("Segundo");
        assertThat(updatedPatient.getAddress()).isEqualTo("Calle Nueva 456");
        assertThat(updatedPatient.getLandline()).isEqualTo("6011111111");
        assertThat(updatedPatient.getCellPhone()).isEqualTo("3002222222");
        assertThat(updatedPatient.getResidencesType()).isEqualTo("Casa");
        assertThat(updatedPatient.getDescriptionResidence()).isEqualTo("Cerca de la montaña");
        assertThat(updatedPatient.getNeighborhood()).isEqualTo("Sur");
    }

    @Test
    void shouldReturnErrorWhenUpdatingNonExistentPatient() {
        GraphQlTester.Response response = graphQlTester.document("""
                mutation {
                    updatePatient(
                        id: 12345
                        firstName: "Actualizado"
                        secondName: "Nuevo"
                        firstSurName: "Apellido"
                        secondSurName: "Segundo"
                        address: "Calle Nueva 456"
                        email: "noexiste@example.com"
                        landline: "6011111111"
                        cellPhone: "3002222222"
                        residencesType: "Casa"
                        descriptionResidence: "Cerca de la montaña"
                        neighborhood: "Sur"
                        program: "Programa1"
                    )
                }
                """).execute();

        validateNotFoundException(response, "updatePatient");
    }

    @Test
    @DirtiesContext
    void shouldReturnErrorWhenCannotCommunicateWithDynamoInUpdatePatientFlow() {
        localstack.stop();
        GraphQlTester.Response response = graphQlTester.document("""
                mutation {
                    updatePatient(
                        id: 12345
                        firstName: "Actualizado"
                        secondName: "Nuevo"
                        firstSurName: "Apellido"
                        secondSurName: "Segundo"
                        address: "Calle Nueva 456"
                        email: "update.test@example.com"
                        landline: "6011111111"
                        cellPhone: "3002222222"
                        residencesType: "Casa"
                        descriptionResidence: "Cerca de la montaña"
                        neighborhood: "Sur"
                        program: "Programa1"
                    )
                }
                """).execute();

        response.errors().satisfy(errors -> {
            assertThat(errors).hasSize(1);

            ResponseError error = errors.get(0);

            assertThat(error.getMessage()).contains("Error al actualizar el paciente");

            Map<String, Object> extensions = error.getExtensions();
            assertThat(extensions).isNotNull();
            assertThat(extensions).containsEntry("code", "UPDATE_PATIENT");
            assertThat(extensions).containsEntry("classification", "INTERNAL_ERROR");
            assertThat(extensions).containsKey("timestamp");
        });

        response.path("updatePatient").valueIsNull();

        startLocalStackContainer();
    }

    private Callable<Boolean> getQueueMessage(String program) {
        return () -> {

            log.info("🔍 [Intento {}] Intentando recibir mensaje de SQS desde: {}",
                    System.currentTimeMillis(), queueUrl);

            try {
                ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage(
                        ReceiveMessageRequest.builder()
                                .queueUrl(queueUrl)
                                .maxNumberOfMessages(1)
                                .waitTimeSeconds(5)
                                .build()
                );

                String messageBody = receiveMessageResponse.messages().get(0).body();

                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> messageMap = objectMapper.readValue(
                        messageBody,
                        new TypeReference<Map<String, Object>>() {}
                );

                boolean hasMessageType = messageMap.containsKey("message_type") &&
                        "welcome_patient".equals(messageMap.get("message_type"));
                boolean hasChannels = messageMap.containsKey("channels") &&
                        "email".equals(messageMap.get("channels"));
                boolean hasPlanType = messageMap.containsKey("plan_type") &&
                        program.equals(messageMap.get("plan_type"));


                if (hasMessageType && hasChannels && hasPlanType) {
                    log.info("✅ Mensaje SQS VÁLIDO encontrado!");
                    return true;
                } else {
                    log.warn("⚠️ Mensaje encontrado pero no válido: {}", messageMap);
                    return false;  // Mensaje inválido, seguir buscando
                }

            } catch (Exception e) {
                log.error("❌ Error al procesar mensaje SQS: {} - {}",
                        e.getClass().getName(), e.getMessage());
                return false;
            }
        };
    }

    private void savePatientInDBFromUseCase() {
        id = "12345";
        patientToSave.setId(Integer.parseInt(id));
        patientToSave.setFirstName("Maria");
        patientToSave.setFirstSurName("Lopez");
        patientToSave.setSecondSurName("Pérez");
        patientToSave.setEmail("maria.lopez@example.com");
        patientToSave.setCellPhone("3001234567");
        patientToSave.setResidencesType("Casa");
        patientToSave.setAddress("calle 12 # 34-56");
        patientToSave.setDescriptionResidence("Cerca del parque");
        patientToSave.setNeighborhood("Centro");
        patientToSave.setProgram("Programa1");
        patientToSave.setStatus("ACTIVE");
        patientPersistencePort.createPatient(patientToSave);
    }

    private GraphQlTester.Response createPatient(int idSave, String email) {
        return graphQlTester.document("""
                mutation createPatient($email: String!, $id: Int!){
                    createPatient(
                        id: $id
                        firstName: "Maria"
                        firstSurName: "Lopez"
                        secondSurName: "Gómez"
                        address: "Calle 123"
                        email: $email
                        cellPhone: "3001234567"
                        residencesType: "Casa"
                        descriptionResidence: "Cerca del parque"
                        neighborhood: "Centro"
                        program: "Programa1"
                    )
                }
                """).variable("email", email).variable("id", idSave).execute();
    }

    private void validateNotFoundException(GraphQlTester.Response response, String path) {
        response.errors().satisfy(errors -> {
            assertThat(errors).hasSize(1);

            ResponseError error = errors.get(0);

            assertThat(error.getMessage()).contains("El paciente con ");

            Map<String, Object> extensions = error.getExtensions();
            assertThat(extensions).isNotNull();
            assertThat(extensions).containsEntry("code", "PATIENT_NOT_FOUND");
            assertThat(extensions).containsEntry("classification", "NOT_FOUND");
            assertThat(extensions).containsKey("timestamp");
        });
        response.path(path).valueIsNull();
    }

    private void startLocalStackContainer() {
        localstack.start();
        localstack.waitingFor(Wait.forHttp("/_localstack/health").forStatusCode(200));

        int newMappedPort = localstack.getMappedPort(4566);
        dynamodbEndpoint = "http://localhost:" + newMappedPort;
        sesSqsEndpoint = "http://localhost:" + newMappedPort;

        createAwsResources();
    }
}