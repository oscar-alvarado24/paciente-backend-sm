package com.medicine.register;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.medicine.register.infrastructure.util.Constants;

@SpringBootApplication
@EnableDynamoDBRepositories(basePackages = Constants.BASE_PACKAGES_REPOSITORY)
public class RegisterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegisterApplication.class, args);
	}

}
