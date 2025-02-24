package com.colombia.eps.patient.infrastructure.output.ses.adapter;

import com.colombia.eps.patient.domain.spi.ISesPersistencePort;
import com.colombia.eps.patient.infrastructure.exception.NotCreateUserInUserPoolException;
import com.colombia.eps.patient.infrastructure.output.ses.config.SesManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sesv2.model.CreateEmailIdentityRequest;


@RequiredArgsConstructor
@Slf4j
public class SesAdapter implements ISesPersistencePort {
    /**
     * @param emailAddress of patient to verify
     */
    @Override
    public void verifyEmailAddress(String emailAddress) {
        try (SesManager sesManager = new SesManager()) {
            CreateEmailIdentityRequest request = CreateEmailIdentityRequest.builder()
                    .emailIdentity(emailAddress)
                    .build();

            sesManager.getSesClient().createEmailIdentity(request);
            log.info("Verification initiated for: {}", emailAddress);
        } catch (Exception exception) {
            log.error(exception.getMessage(),exception);
            throw new NotCreateUserInUserPoolException.VerifyEmailAddressException(exception.getMessage());
        }
    }
}
