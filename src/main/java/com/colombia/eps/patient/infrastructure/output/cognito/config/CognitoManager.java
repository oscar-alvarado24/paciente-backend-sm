package com.colombia.eps.patient.infrastructure.output.cognito.config;

import com.colombia.eps.library.GenerateCredentials;
import com.colombia.eps.patient.infrastructure.util.Constants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Slf4j
public class CognitoManager implements AutoCloseable {
    @Getter
    private CognitoIdentityProviderClient cognitoClient;

    public CognitoManager() {
        try {
            String cognitoRole = System.getenv(Constants.VE_COGNITO_ROL);
            String accessKeyId = System.getenv(Constants.VE_AKI_COGNITO_USER);
            String secretAccessKey = System.getenv(Constants.VE_SAK_COGNITO_USER);
            Region region = Region.of(System.getenv(Constants.VE_REGION));
            StaticCredentialsProvider credential = GenerateCredentials.createCredencials(accessKeyId, secretAccessKey, cognitoRole, Constants.ROLE_SESSION_NAME_COGNITO, region);

            this.cognitoClient = CognitoIdentityProviderClient.builder().region(region).credentialsProvider(credential).build();
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    /**
     * @throws Exception generated during close process
     */
    @Override
    public void close() throws Exception {
        if (cognitoClient != null) {
            cognitoClient.close();
        }
    }
}
