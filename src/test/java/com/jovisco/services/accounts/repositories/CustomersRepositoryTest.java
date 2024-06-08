package com.jovisco.services.accounts.repositories;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.jovisco.services.accounts.entities.Customer;

import jakarta.transaction.Transactional;

@SpringBootTest
public class CustomersRepositoryTest {

    @Autowired
    CustomersRepository customersRepository;

    Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = createTestCustomer();
    }

    @Transactional
    @Rollback
    @Test
    void testFindByMobileNumber() {

        assertTrue(customersRepository.findByMobileNumber(testCustomer.getMobileNumber()).isPresent());
    }

    @Transactional
    @Rollback
    @Test
    void testFindByMobileNumberNotFound() {

        assertFalse(customersRepository.findByMobileNumber("+9999999999999").isPresent());
    }

    private Customer createTestCustomer() {
        var testCustomer = Customer.builder()
                .name("Test Customer")
                .email("test.customer@test.com")
                .mobileNumber("+49777123456789")
                .build();

        testCustomer = customersRepository.save(testCustomer);

        return testCustomer;
    }

}
