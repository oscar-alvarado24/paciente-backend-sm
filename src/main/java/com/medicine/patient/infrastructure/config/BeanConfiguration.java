package com.medicine.patient.infrastructure.config;

import com.medicine.patient.domain.api.IPatientEncryptionServicePort;
import com.medicine.patient.domain.api.IPatientServicePort;
import com.medicine.patient.domain.spi.IPatientEncryptionPersistencePort;
import com.medicine.patient.domain.spi.IPatientPersistencePort;
import com.medicine.patient.domain.usecase.PatientEncryptionUseCase;
import com.medicine.patient.domain.usecase.PatientUseCase;
import com.medicine.patient.infrastructure.encryption.adapter.PatientEncryptionAdapter;
import com.medicine.patient.infrastructure.encryption.systemEncryption.Encryption;
import com.medicine.patient.infrastructure.output.dynamo.adapter.PatientDynamoAdapter;
import com.medicine.patient.infrastructure.output.dynamo.mapper.IPatientEntityMapper;
import com.medicine.patient.infrastructure.output.dynamo.repository.IPatientRepository;
import com.medicine.patient.infrastructure.output.dynamo.repository.PatientRepository;
import com.medicine.patient.infrastructure.util.Constants;
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
    public IPatientPersistencePort patientPersistencePort(){
        return new PatientDynamoAdapter(patientRepository(),patientMapper);
    }

    @Bean
    public IPatientServicePort patientServicePort(){
        return new PatientUseCase(patientPersistencePort());
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
