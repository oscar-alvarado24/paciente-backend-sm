package com.colombia.eps.patient.infrastructure.output.ses.adapter;

import com.colombia.eps.patient.infrastructure.exception.IdentityNotFoundException;
import com.colombia.eps.patient.infrastructure.exception.SendEmailVerificationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.GetIdentityVerificationAttributesRequest;
import software.amazon.awssdk.services.ses.model.GetIdentityVerificationAttributesResponse;
import software.amazon.awssdk.services.ses.model.IdentityVerificationAttributes;
import software.amazon.awssdk.services.ses.model.VerifyEmailIdentityRequest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SesAdapterTest {
    @Mock
    private SesClient sesClient;

    @InjectMocks
    private SesAdapter sesAdapter;

    @Test
    void testValidateStatusSesRegistrationWithIdentityNotFound() {

        when(sesClient.getIdentityVerificationAttributes(any(GetIdentityVerificationAttributesRequest.class)))
                .thenReturn(GetIdentityVerificationAttributesResponse.builder()
                        .verificationAttributes(new HashMap<>())
                        .build());
        assertThrows(IdentityNotFoundException.class, () -> sesAdapter.validateStatusSesRegistration("test@example.com"));
    }

    @Test
    void testValidateStatusSesRegistrationWithIdentityHaveStatusPending() {
        Map<String, IdentityVerificationAttributes> verificationAttributesMap = new HashMap<>();
        verificationAttributesMap.put("test@example.com", IdentityVerificationAttributes.builder().verificationStatus("Pending").build());
        when(sesClient.getIdentityVerificationAttributes(any(GetIdentityVerificationAttributesRequest.class)))
                .thenReturn(GetIdentityVerificationAttributesResponse.builder()
                        .verificationAttributes(new HashMap<>(verificationAttributesMap))
                        .build());
        String response = sesAdapter.validateStatusSesRegistration("test@example.com");
        assert(response.equals("Validacion_reenviada"));
    }

    @Test
    void testCatchExceptionAndGenerateSendEmailVerificationExceptionWhenSendEmailVerificationHaveError(){
        Map<String, IdentityVerificationAttributes> verificationAttributesMap = new HashMap<>();
        verificationAttributesMap.put("test@example.com", IdentityVerificationAttributes.builder().verificationStatus("Pending").build());
        when(sesClient.getIdentityVerificationAttributes(any(GetIdentityVerificationAttributesRequest.class)))
                .thenReturn(GetIdentityVerificationAttributesResponse.builder()
                        .verificationAttributes(new HashMap<>(verificationAttributesMap))
                        .build());
        when(sesClient.verifyEmailIdentity(any(VerifyEmailIdentityRequest.class ))).thenThrow(new RuntimeException("Error sending email verification"));
        assertThrows(SendEmailVerificationException.class, ()->sesAdapter.validateStatusSesRegistration("test@example.com"));
    }
}
