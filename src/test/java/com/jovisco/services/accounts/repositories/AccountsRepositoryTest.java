package com.jovisco.services.accounts.repositories;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.jovisco.services.accounts.constants.AccountsConstants;
import com.jovisco.services.accounts.entities.Account;
import com.jovisco.services.accounts.entities.Customer;

import jakarta.transaction.Transactional;

@SpringBootTest
public class AccountsRepositoryTest {

  @Autowired
  CustomersRepository customersRepository;

  @Autowired
  AccountsRepository accountsRepository;

  Customer testCustomer;

  @BeforeEach
  void setUp() {
    // create a test account
    testCustomer = createTestCustomerAndAccount();
  }

  @Transactional
  @Rollback
  @Test
  void testDeleteByCustomerId() {

    // delete the just created customer
    accountsRepository.deleteByCustomerId(testCustomer.getId());

    // check that account doesn' exist anymore
    assertFalse(accountsRepository.findByCustomerId(testCustomer.getId()).isPresent());
  }

  @Transactional
  @Rollback
  @Test
  void testFindByCustomerId() {

    // check that account is found
    assertTrue(accountsRepository.findByCustomerId(testCustomer.getId()).isPresent());
  }

  @Transactional
  @Rollback
  @Test
  void testFindByCustomerIdNotFound() {

    // check that account is found
    assertTrue(accountsRepository.findByCustomerId(1111111111L).isEmpty());
  }

  private Customer createTestCustomerAndAccount() {
    var testCustomer = Customer.builder()
        .name("Test Customer")
        .email("test.customer@test.com")
        .mobileNumber("+49777123456789")
        .build();

    testCustomer = customersRepository.save(testCustomer);

    var testAccount = Account.builder()
        .customerId(testCustomer.getId())
        .type(AccountsConstants.SAVINGS)
        .branchAddress(AccountsConstants.ADDRESS)
        .id(1999999999L)
        .build();

    testAccount = accountsRepository.save(testAccount);

    return testCustomer;
  }
}
