package com.jovisco.services.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.jovisco.services.accounts.dtos.ContactInfoDto;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@EnableFeignClients
@EnableConfigurationProperties(value = ContactInfoDto.class)
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Accounts Microservice - REST API Documentation", description = "Learning how to develop, build, document and deploy microservices with Spring Boot", version = "1.0.0", contact = @Contact(name = "Josef Heiss", email = "contact@jovisco.de", url = "https://www.jovisco.de"), license = @License(name = "Apache 2.0", url = "https://www.jovisco.de")), externalDocs = @ExternalDocumentation(description = "Spring Microservices Course: Accounts Microservice - REST API Documentation", url = "https://www.example.com"))
public class AccountsApplication {

  public static void main(String[] args) {
    SpringApplication.run(AccountsApplication.class, args);
  }

}
