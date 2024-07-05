package com.medicine.register.infrastructure.config;

import com.medicine.register.domain.api.IPatientEncryptionServicePort;
import com.medicine.register.domain.api.IPatientServicePort;
import com.medicine.register.domain.spi.IPatientEncryptionPersistencePort;
import com.medicine.register.domain.spi.IPatientPersistencePort;
import com.medicine.register.domain.usecase.PatientEncryptionUseCase;
import com.medicine.register.domain.usecase.PatientUseCase;
import com.medicine.register.infrastructure.encryption.adapter.PatientEncryptionAdapter;
import com.medicine.register.infrastructure.encryption.systemEncryption.Encryption;
import com.medicine.register.infrastructure.output.dynamo.adapter.PatientDynamoAdapter;
import com.medicine.register.infrastructure.output.dynamo.mapper.IPatientEntityMapper;
import com.medicine.register.infrastructure.output.dynamo.repository.IPatientRepository;
import com.medicine.register.infrastructure.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackages = Constants.BASE_PACKAGES_REPOSITORY)
public class BeanConfiguration {
    private final IPatientRepository patientRepository;
    private final IPatientEntityMapper patientMapper;
    @Bean
    public IPatientPersistencePort patientPersistencePort(){
        return new PatientDynamoAdapter(patientRepository,patientMapper);
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
