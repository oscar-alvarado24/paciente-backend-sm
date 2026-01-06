package com.colombia.eps.patient;

import com.colombia.eps.patient.infrastructure.input.graphql.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest()
@ActiveProfiles("test-integration")
@Import(TestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class PatientApplicationTests {

	@Test
	void contextLoads() {
	}

}
