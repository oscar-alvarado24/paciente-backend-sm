package com.colombia.eps.patient.infrastructure.output.cognito;

import com.colombia.eps.library.GenerateCredentials;
import com.colombia.eps.patient.domain.model.Patient;
import com.colombia.eps.patient.domain.spi.ICognitoPersistencePort;
import com.colombia.eps.patient.infrastructure.exception.NotAddUserToGroupException;
import com.colombia.eps.patient.infrastructure.exception.NotCreateUserInUserPoolException;
import com.colombia.eps.patient.infrastructure.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class CognitoAdapter implements ICognitoPersistencePort {

    /**
     * @param patient data of patient to create
     */
    @Override
    public void createPatientInUserPool(Patient patient) {


            AdminCreateUserResponse createUser = createNewUser(cognitoClient, userPoolId, email, password);
            if (createUser != null) {
                addUserToGroup(cognitoClient, userPoolId, email);
            }
        }

    private AdminCreateUserResponse createNewUser(CognitoIdentityProviderClient cognitoClient,
                                     String userPoolId,
                                     String email,
                                     String password) {
        try {
            List<AttributeType> userAttributes = new ArrayList<>();
            userAttributes.add(AttributeType.builder()
                    .name("email")
                    .value(email)
                    .build());
            userAttributes.add(AttributeType.builder()
                    .name("email_verified")
                    .value("true")
                    .build());

            AdminCreateUserRequest userRequest = AdminCreateUserRequest.builder()
                    .userPoolId(userPoolId)
                    .username(email)
                    .temporaryPassword(password)
                    .userAttributes(userAttributes)
                    .messageAction("SUPPRESS")
                    .build();

             return cognitoClient.adminCreateUser(userRequest);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new NotCreateUserInUserPoolException(String.format(Constants.MSG_NOT_CRESTE_USER_IN_USER_POOL, email));
        }
    }

    private void addUserToGroup(CognitoIdentityProviderClient cognitoClient,
                                String userPoolId, String username) {
        try {
            String groupName = System.getenv(Constants.PATIENT_GROUP);
            AdminAddUserToGroupRequest addUserToGroupRequest = AdminAddUserToGroupRequest.builder()
                    .userPoolId(userPoolId)
                    .username(username)
                    .groupName(groupName)
                    .build();

            cognitoClient.adminAddUserToGroup(addUserToGroupRequest);
        }catch (Exception exception) {
            log.error(exception.getMessage());
            throw new NotAddUserToGroupException(String.format(Constants.MSG_NOT_ADD_USER_TO_GROUP, username.replace("_", " ")));
        }
    }

}