package com.colombia.eps.patient.infrastructure.output.cognito.adapter;

import com.colombia.eps.patient.domain.model.Patient;
import com.colombia.eps.patient.domain.spi.ICognitoPersistencePort;
import com.colombia.eps.patient.infrastructure.exception.CreatePatientInUserPoolException;
import com.colombia.eps.patient.infrastructure.exception.AddUserToGroupException;
import com.colombia.eps.patient.infrastructure.exception.CreateUserInUserPoolException;
import com.colombia.eps.patient.infrastructure.helper.ExceptionMessage;
import com.colombia.eps.patient.infrastructure.helper.StackTraceAnalyzer;
import com.colombia.eps.patient.infrastructure.helper.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class CognitoAdapter implements ICognitoPersistencePort {
    private final CognitoIdentityProviderClient cognitoClient;

    /**
     * develop a process for create a patient that consists in create a user in user pool and add this user a patient group
     * @param patient data of patient to create
     */
    @Override
    public void createPatientInUserPool(Patient patient) {
        try {
            String userPoolId = System.getenv(Constants.VE_USER_POOL_ID);
            String email = patient.getEmail();
            String password = patient.getFirstName().toUpperCase() + patient.getFirstSurName().toLowerCase() + patient.getId() % 10000 + Constants.ASTERISK;
            AdminCreateUserResponse createUser = createNewUser(this.cognitoClient, userPoolId, email, password);
            if (createUser != null) {
                addUserToGroup(this.cognitoClient, userPoolId, email, patient.getFirstName(), patient.getFirstSurName());
            }
        } catch (CreateUserInUserPoolException | AddUserToGroupException exception){
            throw exception;
        } catch (Exception exception) {
            log.error(ExceptionMessage.builder()
                    .message(exception.getMessage())
                    .type(exception.getClass().getName())
                    .hour(LocalDateTime.now().toString())
                    .line(StackTraceAnalyzer.getErrorInfo(exception, CognitoAdapter.class.getPackageName()))
                    .build()
                    .toString());
            throw new CreatePatientInUserPoolException();
        }
    }

    /**
     * create a new user in user pool
     * @param cognitoClient client of cognito
     * @param userPoolId id of user pool
     * @param email email of user
     * @param password password of user
     * @return response of create user
     */
    private AdminCreateUserResponse createNewUser(CognitoIdentityProviderClient cognitoClient, String userPoolId, String email, String password) {
        try {
            List<AttributeType> userAttributes = new ArrayList<>();
            userAttributes.add(AttributeType.builder().name(Constants.EMAIL).value(email).build());
            userAttributes.add(AttributeType.builder().name(Constants.EMAIL_VERIFIED).value(Constants.TRUE).build());

            AdminCreateUserRequest userRequest = AdminCreateUserRequest.builder().userPoolId(userPoolId).username(email).temporaryPassword(password).userAttributes(userAttributes).messageAction("SUPPRESS").build();

            return cognitoClient.adminCreateUser(userRequest);
        } catch (Exception exception) {
            log.error(ExceptionMessage.builder()
                    .message(exception.getMessage())
                    .type(exception.getClass().getName())
                    .hour(LocalDateTime.now().toString())
                    .line(StackTraceAnalyzer.getErrorInfo(exception, CognitoAdapter.class.getPackageName()))
                    .build()
                    .toString());
            throw new CreateUserInUserPoolException(String.format(Constants.MSG_NOT_CREATE_USER_IN_USER_POOL, email));
        }
    }

    /**
     * add a user to group
     * @param cognitoClient client of cognito
     * @param userPoolId id of user pool
     * @param username username of user
     * @param name name of user
     * @param surname surname of user
     */
    private void addUserToGroup(CognitoIdentityProviderClient cognitoClient, String userPoolId, String username, String name, String surname) {
        try {
            String groupName = System.getenv(Constants.PATIENT_GROUP);
            AdminAddUserToGroupRequest addUserToGroupRequest = AdminAddUserToGroupRequest.builder().userPoolId(userPoolId).username(username).groupName(groupName).build();

            cognitoClient.adminAddUserToGroup(addUserToGroupRequest);
        } catch (Exception exception) {
            log.error(ExceptionMessage.builder()
                    .message(exception.getMessage())
                    .type(exception.getClass().getName())
                    .hour(LocalDateTime.now().toString())
                    .line(StackTraceAnalyzer.getErrorInfo(exception, CognitoAdapter.class.getPackageName()))
                    .build()
                    .toString());
            throw new AddUserToGroupException(String.format(Constants.MSG_NOT_ADD_PATIENT_TO_PATIENT_GROUP, name, surname));
        }
    }

}