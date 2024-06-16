package com.jovisco.services.accounts;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.jovisco.services.accounts.dtos.CustomerDto;
import com.jovisco.services.accounts.services.AccountsService;
import io.restassured.RestAssured;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create",
        "build.version=1.0.0",
})
@ActiveProfiles("test")
class AccountsApplicationContainerTests {

	@Container
	static final MySQLContainer<?> MYSQL_CONTAINER;

	@LocalServerPort
	private Integer port;
	
	@Autowired
	private AccountsService accountsService;
	
	private CustomerDto customerDto;

	static {
		MYSQL_CONTAINER = new MySQLContainer<>("mysql:latest");
	}

	@DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url",() -> MYSQL_CONTAINER.getJdbcUrl());
        registry.add("spring.datasource.username",() -> MYSQL_CONTAINER.getUsername());
        registry.add("spring.datasource.password",() -> MYSQL_CONTAINER.getPassword());
        // registry.add("spring.jpa.hibernate.ddl-auto",() -> "create");
        // registry.add("build.version",() -> "1.0.0");
    }

	// -- NOT NEEDED SINCE THIS IS DONE BY @TestContainers
	// @BeforeAll
	// static void beforeAll() {
	// 	MYSQL_CONTAINER.start();
	// }

	// -- NOT NEEDED SINCE THIS IS DONE BY @TestContainers
	// @AfterAll
	// static void afterAll() {
	// 	MYSQL_CONTAINER.stop();
	// }

	@BeforeEach
	void beforeEach() {
		RestAssured.baseURI = "http://localhost:" + port;
		// prep customer data input
		customerDto = CustomerDto.builder()
			.mobileNumber("+12223333444")
			.name("Test Customer")
			.email("test@example.com")
			.build();
	}

	@Order(1)
	@Test
	void testCreateAccount() {
		
		RestAssured
			.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(customerDto)
			.when()
				.post("/api/v1/accounts")
			.then()
				.statusCode(201)
				.header("Location", Matchers.containsString(customerDto.getMobileNumber()));		
	}

	@Order(2)
	@Test
	void testFetchAccount() {
		
		RestAssured
			.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
				.get("/api/v1/accounts/{mobileNumber}", customerDto.getMobileNumber())
			.then()
				.statusCode(200)
				.body("customer.mobileNumber", Matchers.containsString(customerDto.getMobileNumber()));		
	}

	@Order(3)
	@Test
	void testUpdateAccount() {
		var customerWithAccountDto = accountsService.fetchAccount(customerDto.getMobileNumber());
		customerWithAccountDto.getCustomer().setName("***UPDATED***");
		customerWithAccountDto.getCustomer().setEmail("updated@updated.com");
		customerWithAccountDto.getAccount().setBranchAddress("***UPDATED ADDRESS***");

		RestAssured
			.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(customerWithAccountDto)
			.when()
				.put("/api/v1/accounts")
			.then()
				.statusCode(200);
	}

	@Order(4)
	@Test
	void testDeleteAccount() {

		RestAssured
			.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
				.delete("/api/v1/accounts/{mobileNumber}", customerDto.getMobileNumber())
			.then()
				.statusCode(200);
	}
}
