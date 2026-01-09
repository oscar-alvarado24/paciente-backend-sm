package com.colombia.eps.patient.infrastructure.output.ses.adapter;

import com.colombia.eps.patient.domain.spi.ISesPersistencePort;
import com.colombia.eps.patient.infrastructure.exception.CreateSesIdentityException;
import com.colombia.eps.patient.infrastructure.exception.GetVerificationStatusInSesException;
import com.colombia.eps.patient.infrastructure.exception.IdentityNotFoundException;
import com.colombia.eps.patient.infrastructure.exception.SendEmailVerificationException;
import com.colombia.eps.patient.infrastructure.helper.Constants;
import com.colombia.eps.patient.infrastructure.helper.ExceptionMessage;
import com.colombia.eps.patient.infrastructure.helper.StackTraceAnalyzer;
import com.colombia.eps.patient.infrastructure.output.ses.helper.SesStatus;
import com.colombia.eps.patient.infrastructure.output.ses.helper.StatusLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.GetIdentityVerificationAttributesRequest;
import software.amazon.awssdk.services.ses.model.GetIdentityVerificationAttributesResponse;
import software.amazon.awssdk.services.ses.model.IdentityVerificationAttributes;
import software.amazon.awssdk.services.ses.model.VerifyEmailIdentityRequest;

import java.time.LocalDateTime;
import java.util.Optional;


@RequiredArgsConstructor
@Slf4j
public class SesAdapter implements ISesPersistencePort {
    private final SesClient sesClient;
    /**
     * @param emailAddress of patient to verify
     */
    @Override
    public void createSesIdentity(String emailAddress) {
        try {
            log.debug("Verification initiated for: {}", emailAddress);
            createIdentity(emailAddress);
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
        String status = getVerificationStatus(emailAddress);
        String response;
        if (Constants.SUCCESS.equals(status)) {
            log.debug(StatusLog.SUCCESS.format( emailAddress));
            response= SesStatus.SUCCESS.getSentence();
        } else if (Constants.NOT_FOUND.equals(status)) {
            log.debug(StatusLog.NOT_FOUND.format(emailAddress));
            throw new IdentityNotFoundException();
        } else {
            log.debug(StatusLog.NOT_SUCCESS.format(emailAddress));
            sendEmailVerification(emailAddress);
            response= SesStatus.NOT_SUCCESS.getSentence();
        }
        return response;
    }

    /**
     * Create an identity for an email address.
     *
     * @param email       The email address to create an identity for.
     */
    private void createIdentity(String email){
        VerifyEmailIdentityRequest request = VerifyEmailIdentityRequest.builder()
                .emailAddress(email)
                .build();
        this.sesClient.verifyEmailIdentity(request);
    }

    /**
     * Verifier the verification status of an email address.
     *
     * @param emailAddress The email address to verify.
     * @return verification status of an email address
     */
    private String getVerificationStatus(String emailAddress) {
        try {
            GetIdentityVerificationAttributesRequest request =
                    GetIdentityVerificationAttributesRequest.builder()
                            .identities(emailAddress)
                            .build();

            GetIdentityVerificationAttributesResponse response =
                    this.sesClient.getIdentityVerificationAttributes(request);

            Optional<IdentityVerificationAttributes> attributes =
                    Optional.ofNullable(response.verificationAttributes().get(emailAddress));

            if (attributes.isPresent()) {
                return attributes.get().verificationStatus().toString();

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

    private void sendEmailVerification(String emailAddress) {
        try {
            VerifyEmailIdentityRequest request = VerifyEmailIdentityRequest.builder()
                    .emailAddress(emailAddress)
                    .build();

            this.sesClient.verifyEmailIdentity(request);
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
