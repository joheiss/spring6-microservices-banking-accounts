package com.jovisco.services.accounts.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jovisco.services.accounts.dtos.CustomerDto;
import com.jovisco.services.accounts.exceptions.CustomerAlreadyExistsException;
import com.jovisco.services.accounts.exceptions.ResourceNotFoundException;
import com.jovisco.services.accounts.repositories.AccountsRepository;
import com.jovisco.services.accounts.repositories.CustomersRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AccountsControllerIT {

  @Autowired
  AccountsController accountsController;

  @Autowired
  CustomersRepository customersRepository;

  @Autowired
  AccountsRepository accountsRepository;

  @Autowired
  WebApplicationContext wac;

  @Autowired
  ObjectMapper objectMapper;

  MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Transactional
  @Rollback
  @Test
  void testCreateAccountMvc() throws Exception {

    var customerDto = buildCustomerDto();

    mockMvc.perform(
        post("/api/v1/accounts")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerDto)))
        .andExpect(status().isCreated())
        .andReturn();
  }

  @Transactional
  @Rollback
  @Test
  void testCreateAccount() {

    var customerDto = buildCustomerDto();

    var response = accountsController.createAccount(customerDto);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    var location = response.getHeaders().getLocation();
    System.out.println("Location: " + location);
    System.out.flush();
    assertThat(location).isNotNull();

    // get id from location & verify that the customer and account is really present
    // on the database
    if (location != null) {
      var segments = location.getPath().split("/");
      var mobileNumber = segments[segments.length - 1];
      assertThat(mobileNumber).isNotNull();
      var customer = customersRepository.findByMobileNumber(mobileNumber);
      assertTrue(customer.isPresent());
      assertTrue(accountsRepository.findByCustomerId(customer.get().getId()).isPresent());
    }
  }

  @Transactional
  @Rollback
  @Test
  void testCreateAccountWithAlreadyExistsError() {

    var customerDto = buildCustomerDto();
    accountsController.createAccount(customerDto);

    assertThatExceptionOfType(CustomerAlreadyExistsException.class)
        .isThrownBy(() -> accountsController.createAccount(customerDto));
  }

  @Test
  void testCreateAccountWithNameValidationError() {

    var customerDto = buildCustomerDto();
    customerDto.setName("");

    assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(() -> accountsController.createAccount(customerDto))
        .withMessageContaining("name");
  }

  @Test
  void testCreateAccountWithEmailValidationError() {

    var customerDto = buildCustomerDto();
    customerDto.setEmail("INVALID EMAIL");

    assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(() -> accountsController.createAccount(customerDto))
        .withMessageContaining("email");
  }

  @Test
  void testCreateAccountWithMobileNumberValidationError() {

    var customerDto = buildCustomerDto();
    customerDto.setMobileNumber("+499879876543XY");

    assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(() -> accountsController.createAccount(customerDto))
        .withMessageContaining("mobile");
  }

  @Transactional
  @Rollback
  @Test
  void testDeleteAccountMvc() throws Exception {

    // first create a test account
    var customerDto = buildCustomerDto();
    accountsController.createAccount(customerDto);

    mockMvc.perform(
        delete("/api/v1/accounts/{mobileNumber}", customerDto.getMobileNumber()))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Transactional
  @Rollback
  @Test
  void testDeleteAccount() {

    // first create a test account
    var customerDto = buildCustomerDto();
    var response = accountsController.createAccount(customerDto);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    var location = response.getHeaders().getLocation();

    // get id from location and delete account
    var segments = location.getPath().split("/");
    var mobileNumber = segments[segments.length - 1];

    response = accountsController.deleteAccount(mobileNumber);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    // verify that customer and account have been deleted from database
    assertFalse(customersRepository.findByMobileNumber(mobileNumber).isPresent());
  }

  @Transactional
  @Rollback
  @Test
  void testFetchAccountMvc() throws Exception {

    // first create a test account
    var customerDto = buildCustomerDto();
    accountsController.createAccount(customerDto);

    mockMvc.perform(
        get("/api/v1/accounts/{mobileNumber}", customerDto.getMobileNumber()))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Transactional
  @Rollback
  @Test
  void testFetchAccount() {

    // first create a test account
    var customerDto = buildCustomerDto();
    accountsController.createAccount(customerDto);

    // check that account can be fetched
    var response = accountsController.fetchAccount(customerDto.getMobileNumber());
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody().getCustomer().getMobileNumber()).isEqualTo(customerDto.getMobileNumber());
  }

  @Test
  void testFetchAccountWithNotFound() {

    // check that an exception is thrown
    assertThatExceptionOfType(ResourceNotFoundException.class)
        .isThrownBy(() -> accountsController.fetchAccount("+999999999999"));
  }

  @Transactional
  @Rollback
  @Test
  void testUpdateAccountMvc() throws Exception {

    // first create a test account
    var customerDto = buildCustomerDto();
    accountsController.createAccount(customerDto);
    // ... fetch it, and update fields
    var customerWithAccountDto = accountsController.fetchAccount(customerDto.getMobileNumber()).getBody();
    customerWithAccountDto.getCustomer().setName("*** UPDATED ***");

    mockMvc.perform(
        put("/api/v1/accounts")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerWithAccountDto)))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Transactional
  @Rollback
  @Test
  void testUpdateAccount() {

    // first create a test account and fetch it
    var customerDto = buildCustomerDto();
    accountsController.createAccount(customerDto);
    var customerWithAccountDto = accountsController.fetchAccount(customerDto.getMobileNumber()).getBody();

    // check that account can be updated
    final String updatedName = "*** UPDATED ***";
    final String updatedBranchAddress = "*** UPDATED BRANCH ADDRESS ***";
    customerWithAccountDto.getCustomer().setName(updatedName);
    customerWithAccountDto.getAccount().setBranchAddress(updatedBranchAddress);
    var response = accountsController.updateAccount(customerWithAccountDto);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    // check that fields have been updated with the correct values
    var updated = accountsController.fetchAccount(customerDto.getMobileNumber()).getBody();
    assertThat(updated.getCustomer().getName()).isEqualTo(updatedName);
    assertThat(updated.getAccount().getBranchAddress()).isEqualTo(updatedBranchAddress);
  }

  @Transactional
  @Rollback
  @Test
  void testUpdateAccountWithValidationErrors() {

    // first create a test account and fetch it
    var customerDto = buildCustomerDto();
    accountsController.createAccount(customerDto);
    var customerWithAccountDto = accountsController.fetchAccount(customerDto.getMobileNumber()).getBody();

    // check that account cannot be updated with invalid values
    customerWithAccountDto.getCustomer().setName("U");
    customerWithAccountDto.getCustomer().setMobileNumber("abc");
    customerWithAccountDto.getCustomer().setEmail("INVALID EMAIL");
    customerWithAccountDto.getAccount().setType("");
    customerWithAccountDto.getAccount().setBranchAddress("");

    assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(() -> accountsController.updateAccount(customerWithAccountDto))
        .withMessageContainingAll("name", "mobile", "email", "type", "address");
  }

  @Transactional
  @Rollback
  @Test
  void testUpdateAccountWithMoreValidationErrors() {

    // first create a test account and fetch it
    var customerDto = buildCustomerDto();
    accountsController.createAccount(customerDto);
    var customerWithAccountDto = accountsController.fetchAccount(customerDto.getMobileNumber()).getBody();

    // check that account cannot be updated with invalid values
    customerWithAccountDto.getCustomer().setName(null);
    customerWithAccountDto.getCustomer().setMobileNumber(null);
    customerWithAccountDto.getCustomer().setEmail(null);
    customerWithAccountDto.getAccount().setType(null);
    customerWithAccountDto.getAccount().setBranchAddress(null);
    customerWithAccountDto.getAccount().setId(null);

    assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(() -> accountsController.updateAccount(customerWithAccountDto))
        .withMessageContainingAll("name", "mobile", "email", "type", "address", "id");
  }

  private CustomerDto buildCustomerDto() {
    return CustomerDto.builder()
        .name("Test Customer")
        .email("test.customer@test.com")
        .mobileNumber("+1122233333333")
        .build();
  }

  @Test
  void testGetBuildVersion() {
    var response = accountsController.getBuildVersion();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotEmpty();

  }

  @Test
  void testGetContactInfo() {
    var response = accountsController.getContactInfo();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody().getMessage()).isNotEmpty();
  }

}
