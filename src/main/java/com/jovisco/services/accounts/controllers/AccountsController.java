package com.jovisco.services.accounts.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovisco.services.accounts.constants.AccountsConstants;
import com.jovisco.services.accounts.dtos.AccountDto;
import com.jovisco.services.accounts.dtos.CustomerDto;
import com.jovisco.services.accounts.dtos.CustomerWithAccountDto;
import com.jovisco.services.accounts.dtos.ResponseDto;
import com.jovisco.services.accounts.services.IAccountsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountsController {

        public static final String ACCOUNTS_PATH = "/accounts";
        public static final String ACCOUNTS_ID_PATH = ACCOUNTS_PATH + "/{id}";

        private final IAccountsService accountsService;

        @GetMapping(ACCOUNTS_PATH)
        public List<AccountDto> listAccounts() {
                return List.of(
                                AccountDto.builder().id(1L).type("Savings Account").branchAddress("New York City, NY")
                                                .build(),
                                AccountDto.builder().id(2L).type("Loans Account").branchAddress("Las Vegas, NV")
                                                .build(),
                                AccountDto.builder().id(3L).type("Current Account").branchAddress("Miami, FL").build());
        }

        @GetMapping(ACCOUNTS_PATH + "/{mobileNumber}")
        public ResponseEntity<CustomerWithAccountDto> fetchAccount(@PathVariable String mobileNumber) {

                var found = accountsService.fetchAccount(mobileNumber);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(found);

        }

        @PostMapping(ACCOUNTS_PATH)
        public ResponseEntity<ResponseDto> createAccount(@RequestBody CustomerDto customerDto) {

                accountsService.createAccount(customerDto);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(ResponseDto.builder()
                                                .statusCode(AccountsConstants.STATUS_201)
                                                .statusMessage(AccountsConstants.MESSAGE_201)
                                                .build());
        }
}
