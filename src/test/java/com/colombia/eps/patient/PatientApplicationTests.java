package com.colombia.eps.patient;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
    "ALIAS=dummy",
    "PASSWORD=dummy",
    "CERTIFICATE_NAME=dummy",
    "PRIVATEKEY_PASSWORD=dummy",
    "ENVIRONMENT=local",
    "REGION=us-east-1",
    "PATIENT_SESSION_NAME=dummy",
    "app.dynamodb.validation.enabled=false"
})
@ActiveProfiles("local")
class PatientApplicationTests {

	@Test
	void contextLoads() {
	}

}
