package com.colombia.eps.patient.infrastructure.output.ses.config;

import com.colombia.eps.patient.infrastructure.exception.SesManagerException;
import com.colombia.eps.patient.infrastructure.helper.Constants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.sesv2.SesV2Client;

import static com.colombia.eps.library.AwsCredentialGenerate.createCredential;

@Slf4j
@Getter
@RequiredArgsConstructor
public class SesManager implements AutoCloseable{
    private final SesV2Client sesV2Client;
    private final SesClient sesV1Client;
    public SesManager() {
        try {
            String sesRole = System.getenv(Constants.VE_SES_ROL);
            String accessKeyId = System.getenv(Constants.VE_AKI_SES_USER);
            String secretAccessKey = System.getenv(Constants.VE_SAK_SES_USER);
            Region region = Region.of(System.getenv(Constants.VE_REGION));
            AwsCredentialsProvider credential = createCredential(accessKeyId, secretAccessKey, sesRole, Constants.ROLE_SESSION_NAME_SES, region);

            this.sesV2Client = SesV2Client.builder()
                    .region(region)
                    .credentialsProvider(credential)
                    .build();
            this.sesV1Client = SesClient.builder()
                    .region(region)
                    .credentialsProvider(credential)
                    .build();
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new SesManagerException(exception.getMessage());
        }
    }
    /**
     */
    @Override
    public void close() {
        if (this.sesV2Client != null) {
            this.sesV2Client.close();
        }
    }
}
