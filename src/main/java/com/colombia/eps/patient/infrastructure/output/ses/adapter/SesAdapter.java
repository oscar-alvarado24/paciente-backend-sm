package com.colombia.eps.patient.infrastructure.output.ses.adapter;

import com.colombia.eps.patient.domain.spi.ISesPersistencePort;
import com.colombia.eps.patient.infrastructure.exception.CreateSesIdentityException;
import com.colombia.eps.patient.infrastructure.exception.GetVerificationStatusInSesException;
import com.colombia.eps.patient.infrastructure.exception.SendEmailVerificationException;
import com.colombia.eps.patient.infrastructure.exception.ValidateStatusSesRegistrationException;
import com.colombia.eps.patient.infrastructure.helper.Constants;
import com.colombia.eps.patient.infrastructure.helper.ExceptionMessage;
import com.colombia.eps.patient.infrastructure.helper.StackTraceAnalyzer;
import com.colombia.eps.patient.infrastructure.output.ses.config.SesManager;
import com.colombia.eps.patient.infrastructure.output.ses.helper.SesStatus;
import com.colombia.eps.patient.infrastructure.output.ses.helper.StatusLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.GetIdentityVerificationAttributesRequest;
import software.amazon.awssdk.services.ses.model.GetIdentityVerificationAttributesResponse;
import software.amazon.awssdk.services.ses.model.IdentityVerificationAttributes;
import software.amazon.awssdk.services.ses.model.VerifyEmailIdentityRequest;
import software.amazon.awssdk.services.sesv2.model.CreateEmailIdentityRequest;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Slf4j
public class SesAdapter implements ISesPersistencePort {
    /**
     * @param emailAddress of patient to verify
     */
    @Override
    public void createSesIdentity(String emailAddress) {
        try (SesManager sesManager = new SesManager()) {
            createIdentity(emailAddress, sesManager);
            log.info("Verification initiated for: {}", emailAddress);
        } catch (Exception exception) {
            log.error(ExceptionMessage.builder()
                    .message(exception.getMessage())
                    .type(exception.getClass().getName())
                    .hour(LocalDateTime.now().toString())
                    .line(StackTraceAnalyzer.getErrorInfo(exception, SesAdapter.class.getPackageName()))
                    .build()
                    .toString());
            throw new CreateSesIdentityException();
        }
    }

    /**
     * @param emailAddress of patient to validate status
     */
    public String validateStatusSesRegistration(String emailAddress) {
        try (SesManager sesManager = new SesManager()) {
            String status = getVerificationStatus(emailAddress, sesManager.getSesV1Client());
            String response;
            if (Constants.SUCCESS.equals(status)) {
                log.debug(StatusLog.SUCCESS.format( emailAddress));
                response= SesStatus.SUCCESS.getSentence();
            } else {
                log.debug(StatusLog.NOT_SUCCESS.format(emailAddress));
                sendEmailVerification(emailAddress, sesManager.getSesV1Client());
                response= SesStatus.NOT_SUCCESS.getSentence();
            }
            return response;
        } catch (GetVerificationStatusInSesException | SendEmailVerificationException exception) {
            throw exception;
        } catch (Exception exception) {
            log.error(ExceptionMessage.builder()
                    .message(exception.getMessage())
                    .type(exception.getClass().getName())
                    .hour(LocalDateTime.now().toString())
                    .line(StackTraceAnalyzer.getErrorInfo(exception, SesAdapter.class.getPackageName()))
                    .build()
                    .toString());
            throw new ValidateStatusSesRegistrationException();
        }
    }

    /**
     * Create an identity for an email address.
     *
     * @param email       The email address to create an identity for.
     * @param sesManager  The SesManager instance to use for creating the identity.
     */
    private void createIdentity(String email, SesManager sesManager){
        CreateEmailIdentityRequest request = CreateEmailIdentityRequest.builder()
                .emailIdentity(email)
                .build();
        sesManager.getSesV2Client().createEmailIdentity(request);
    }

    /**
     * Verifier the verification status of an email address.
     *
     * @param emailAddress The email address to verify.
     * @return verification status of an email address
     */
    private String getVerificationStatus(String emailAddress, SesClient sesClient) {
        try {
            GetIdentityVerificationAttributesRequest request =
                    GetIdentityVerificationAttributesRequest.builder()
                            .identities(emailAddress)
                            .build();

            GetIdentityVerificationAttributesResponse response =
                    sesClient.getIdentityVerificationAttributes(request);

            IdentityVerificationAttributes attributes =
                    response.verificationAttributes().get(emailAddress);

            if (attributes != null) {
                return attributes.verificationStatus().toString();

            } else {
                log.debug(StatusLog.NOT_FOUND.format(emailAddress));
                return Constants.NOT_FOUND;
            }

        } catch (Exception exception) {
            log.error(ExceptionMessage.builder()
                    .message(exception.getMessage())
                    .type(exception.getClass().getName())
                    .hour(LocalDateTime.now().toString())
                    .line(StackTraceAnalyzer.getErrorInfo(exception, SesAdapter.class.getPackageName()))
                    .build()
                    .toString()) ;
            throw new GetVerificationStatusInSesException();
        }
    }

    public void sendEmailVerification(String emailAddress, SesClient sesClient) {
        try {
            VerifyEmailIdentityRequest request = VerifyEmailIdentityRequest.builder()
                    .emailAddress(emailAddress)
                    .build();

            sesClient.verifyEmailIdentity(request);
        } catch (Exception exception) {
            log.error(ExceptionMessage.builder()
                    .message(exception.getMessage())
                    .type(exception.getClass().getName())
                    .hour(LocalDateTime.now().toString())
                    .line(StackTraceAnalyzer.getErrorInfo(exception, SesAdapter.class.getPackageName()))
                    .build()
                    .toString()) ;
            throw new SendEmailVerificationException();
        }
    }
}
