package com.jovisco.services.accounts.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jovisco.services.accounts.constants.AccountsConstants;
import com.jovisco.services.accounts.dtos.AccountDto;
import com.jovisco.services.accounts.dtos.CustomerDto;
import com.jovisco.services.accounts.dtos.CustomerWithAccountDto;
import com.jovisco.services.accounts.services.AccountsService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisabledInAotMode
@WebMvcTest(AccountsController.class)
public class AccountsControllerMockTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  AccountsService accountsService;

  final String mobileNumber = "+122234567890";

  @Test
  void testCreateAccount() throws Exception {

    var createDto = buildCustomerDto(mobileNumber);

    mockMvc.perform(
        post("/api/v1/" + AccountsController.ACCOUNTS_PATH)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDto))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"));

    // verify that the accounts service's createAccount method was invoked
    verify(accountsService, times(1)).createAccount(createDto);

  }

  @Test
  void testDeleteAccount() throws Exception {

    given(accountsService.deleteAccount(any())).willReturn(true);

    mockMvc.perform(
        delete("/api/v1/" + AccountsController.ACCOUNTS_MOBILENUMBER_PATH, mobileNumber))
        .andExpect(status().isOk());

    // verify that the accounts service's deleteAccount method was invoked
    verify(accountsService, times(1)).deleteAccount(mobileNumber);
  }

  @Test
  void testFetchAccount() throws Exception {

    given(accountsService.fetchAccount(any())).willReturn(buildCustomerWithAccountDto(mobileNumber));

    mockMvc.perform(
        get("/api/v1/" + AccountsController.ACCOUNTS_MOBILENUMBER_PATH, mobileNumber)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // verify that the accounts service's deleteAccount method was invoked
    verify(accountsService, times(1)).fetchAccount(mobileNumber);
  }

  @Test
  void testUpdateAccount() throws Exception {

    given(accountsService.updateAccount(any())).willReturn(true);

    var updateDto = buildCustomerWithAccountDto(mobileNumber);
    mockMvc.perform(
        put("/api/v1/" + AccountsController.ACCOUNTS_PATH)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDto))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // verify that the accounts service's updateAccount method was invoked
    verify(accountsService, times(1)).updateAccount(updateDto);
  }

  private CustomerDto buildCustomerDto(String mobileNumber) {
    return CustomerDto.builder()
        .name("Test Customer")
        .email("test.customer@test.com")
        .mobileNumber(mobileNumber)
        .build();
  }

  private AccountDto buildAccountDto() {

    return AccountDto.builder()
        .id(1234567890L)
        .type(AccountsConstants.SAVINGS)
        .branchAddress(AccountsConstants.ADDRESS)
        .build();
  }

  private CustomerWithAccountDto buildCustomerWithAccountDto(String mobileNumber) {

    var customerDto = buildCustomerDto(mobileNumber);
    var accountDto = buildAccountDto();

    return CustomerWithAccountDto.builder()
        .account(accountDto)
        .customer(customerDto)
        .build();
  }
}
