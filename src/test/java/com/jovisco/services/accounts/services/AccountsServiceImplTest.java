package com.jovisco.services.accounts.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.jovisco.services.accounts.dtos.CustomerDto;
import com.jovisco.services.accounts.entities.Customer;
import com.jovisco.services.accounts.exceptions.ResourceNotFoundException;
import com.jovisco.services.accounts.repositories.AccountsRepository;
import com.jovisco.services.accounts.repositories.CustomersRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class AccountsServiceImplTest {

    @Autowired
    AccountsService accountsService;

    @Autowired
    AccountsRepository accountsRepository;

    @Autowired
    CustomersRepository customersRepository;

    Customer testCustomer;

    @BeforeEach
    void setUp() {
        // create test account ...
        var customerDto = buildCustomerDto();
        accountsService.createAccount(customerDto);
        // ... and store test data
        testCustomer = customersRepository.findByMobileNumber(customerDto.getMobileNumber()).get();
    }

    @Transactional
    @Rollback
    @Test
    void testCreateAccount() {

        // account has been created in setup -> see above

        // check if customer and account exist
        var customer = customersRepository.findByMobileNumber(testCustomer.getMobileNumber());
        assertTrue(customer.isPresent());
        assertTrue(accountsRepository.findByCustomerId(customer.get().getId()).isPresent());
    }

    @Transactional
    @Rollback
    @Test
    void testDeleteAccount() {

        assertTrue(accountsService.deleteAccount(testCustomer.getMobileNumber()));
        assertFalse(customersRepository.findByMobileNumber(testCustomer.getMobileNumber()).isPresent());
    }

    @Transactional
    @Rollback
    @Test
    void testFetchAccount() {

        var found = accountsService.fetchAccount(testCustomer.getMobileNumber());
        assertThat(found).isNotNull();
        assertThat(found.getCustomer().getMobileNumber()).isEqualTo(testCustomer.getMobileNumber());
    }

    @Transactional
    @Rollback
    @Test
    void testFetchAccountNotFound() {

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> accountsService.fetchAccount("+9988877777777"));
    }

    @Transactional
    @Rollback
    @Test
    void testUpdateAccount() {

        // prepare data for update
        final String updatedName = "*** UPDATED ***";
        var found = accountsService.fetchAccount(testCustomer.getMobileNumber());
        found.getCustomer().setName(updatedName);

        // check that update works
        assertTrue(accountsService.updateAccount(found));
        var updated = customersRepository.findByMobileNumber(found.getCustomer().getMobileNumber());
        assertTrue(updated.isPresent());
        assertThat(updated.get().getName()).isEqualTo(updatedName);
    }

    private CustomerDto buildCustomerDto() {
        return CustomerDto.builder()
                .name("Test Customer")
                .email("test.customer@test.com")
                .mobileNumber("+1122233333333")
                .build();
    }
}
