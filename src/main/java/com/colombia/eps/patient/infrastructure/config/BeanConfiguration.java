package com.colombia.eps.patient.infrastructure.config;

import com.colombia.eps.patient.domain.api.IPatientEncryptionServicePort;
import com.colombia.eps.patient.domain.api.IPatientServicePort;
import com.colombia.eps.patient.domain.spi.ICognitoPersistencePort;
import com.colombia.eps.patient.domain.spi.IPatientEncryptionPersistencePort;
import com.colombia.eps.patient.domain.spi.IPatientPersistencePort;
import com.colombia.eps.patient.domain.usecase.PatientEncryptionUseCase;
import com.colombia.eps.patient.domain.usecase.PatientUseCase;
import com.colombia.eps.patient.infrastructure.encryption.adapter.PatientEncryptionAdapter;
import com.colombia.eps.patient.infrastructure.encryption.systemencryption.Encryption;
import com.colombia.eps.patient.infrastructure.output.cognito.CognitoAdapter;
import com.colombia.eps.patient.infrastructure.output.dynamo.adapter.PatientDynamoAdapter;
import com.colombia.eps.patient.infrastructure.output.dynamo.mapper.IPatientEntityMapper;
import com.colombia.eps.patient.infrastructure.output.dynamo.repository.IPatientRepository;
import com.colombia.eps.patient.infrastructure.output.dynamo.repository.PatientRepository;
import com.colombia.eps.patient.infrastructure.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackages = Constants.BASE_PACKAGES_REPOSITORY)
public class BeanConfiguration {
    private final IPatientEntityMapper patientMapper;

    @Bean
    public IPatientRepository patientRepository() {
        return new PatientRepository();
    }

    @Bean
    public ICognitoPersistencePort cognitoPersistencePort() {
        return new CognitoAdapter();
    }
    @Bean
    public IPatientPersistencePort patientPersistencePort(){
        return new PatientDynamoAdapter(patientRepository(),patientMapper);
    }

    @Bean
    public IPatientServicePort patientServicePort(){
        return new PatientUseCase(patientPersistencePort(),cognitoPersistencePort());
    }

    @Bean
    public IPatientEncryptionPersistencePort patientEncryptionPersistencePort(Encryption encryption){
        return new PatientEncryptionAdapter(encryption);
    }

    @Bean
    public IPatientEncryptionServicePort patientEncryptionServicePort(Encryption encryption){
        return new PatientEncryptionUseCase(patientEncryptionPersistencePort(encryption));
    }

}
