package com.jovisco.services.accounts.controllers;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovisco.services.accounts.constants.AccountsConstants;
import com.jovisco.services.accounts.dtos.AccountDto;
import com.jovisco.services.accounts.dtos.CustomerDto;
import com.jovisco.services.accounts.dtos.CustomerWithAccountDto;
import com.jovisco.services.accounts.dtos.ErrorResponseDto;
import com.jovisco.services.accounts.dtos.ResponseDto;
import com.jovisco.services.accounts.services.AccountsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "CRUD REST APIs for Accounts in Banking Microservices", description = "CRUD REST APIs to CREATE, READ, UPDATE and DELETE Accounts in Banking Microservices")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class AccountsController {

  public static final String ACCOUNTS_PATH = "/accounts";
  public static final String ACCOUNTS_MOBILENUMBER_PATH = ACCOUNTS_PATH + "/{mobileNumber}";

  private final AccountsService accountsService;

  @GetMapping(ACCOUNTS_PATH)
  public List<AccountDto> listAccounts() {
    return List.of(
        AccountDto.builder().id(1L).type("Savings Account").branchAddress("New York City, NY")
            .build(),
        AccountDto.builder().id(2L).type("Loans Account").branchAddress("Las Vegas, NV")
            .build(),
        AccountDto.builder().id(3L).type("Current Account").branchAddress("Miami, FL").build());
  }

  @Operation(summary = "Fetch a single account by the customer's mobile number", description = "Fetch data from customer and account for a given mobile number")

  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
      @ApiResponse(responseCode = "404", description = "HTTP Status NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), examples = {
          @ExampleObject(value = "{\"apiPath\": \"uri=/api/v1/accounts/+122234567890\", \"errorCode\": \"404\", \"errorMessage\": \"Account not found ...\", \"errorTime\": \"2024-07-04T11:12:13\"}")
      }, mediaType = MediaType.APPLICATION_JSON_VALUE)),
      @ApiResponse(responseCode = "500", description = "HTTP Status INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), examples = {
          @ExampleObject(value = "{\"apiPath\": \"uri=/api/v1/accounts\", \"errorCode\": \"500\", \"errorMessage\": \"An error occurred ...\", \"errorTime\": \"2024-07-04T11:12:13\"}") }, mediaType = MediaType.APPLICATION_JSON_VALUE))
  })

  @GetMapping(ACCOUNTS_MOBILENUMBER_PATH)
  public ResponseEntity<CustomerWithAccountDto> fetchAccount(@PathVariable String mobileNumber) {

    var found = accountsService.fetchAccount(mobileNumber);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(found);

  }

  @Operation(summary = "Create an account", description = "Create a customer and an account")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "HTTP Status CREATED", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = {
          @ExampleObject(value = "{\"statusCode\": \"201\", \"statusMessage\": \"Account created successfully\"}") }, mediaType = MediaType.APPLICATION_JSON_VALUE)),
      @ApiResponse(responseCode = "400", description = "HTTP Status BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "500", description = "HTTP Status INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), examples = {
          @ExampleObject(value = "{\"apiPath\": \"uri=/api/v1/accounts\", \"errorCode\": \"500\", \"errorMessage\": \"An error occurred ...\", \"errorTime\": \"2024-07-04T11:12:13\"}") }, mediaType = MediaType.APPLICATION_JSON_VALUE))
  })

  @PostMapping(ACCOUNTS_PATH)
  public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {

    accountsService.createAccount(customerDto);

    // set Location header
    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", ACCOUNTS_PATH + "/" + customerDto.getMobileNumber());

    // HTTP status = 201 CREATED
    var body = ResponseDto.builder()
        .statusCode(AccountsConstants.STATUS_201)
        .statusMessage(AccountsConstants.MESSAGE_201)
        .build();

    return new ResponseEntity<ResponseDto>(body, headers, HttpStatus.CREATED);
  }

  @Operation(summary = "Update an account and/or customer", description = "Update a customer and/or an account")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
      @ApiResponse(responseCode = "400", description = "HTTP Status BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "HTTP Status NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), examples = {
          @ExampleObject(value = "{\"apiPath\": \"uri=/api/v1/accounts/+122234567890\", \"errorCode\": \"404\", \"errorMessage\": \"Account not found ...\", \"errorTime\": \"2024-07-04T11:12:13\"}") }, mediaType = MediaType.APPLICATION_JSON_VALUE)),
      @ApiResponse(responseCode = "500", description = "HTTP Status INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), examples = {
          @ExampleObject(value = "{\"apiPath\": \"uri=/api/v1/accounts\", \"errorCode\": \"500\", \"errorMessage\": \"An error occurred ...\", \"errorTime\": \"2024-07-04T11:12:13\"}") }, mediaType = MediaType.APPLICATION_JSON_VALUE))
  })
  @PutMapping(ACCOUNTS_PATH)
  public ResponseEntity<ResponseDto> updateAccount(
      @Valid @RequestBody CustomerWithAccountDto customerWithAccountDto) {

    var isUpdated = accountsService.updateAccount(customerWithAccountDto);

    if (isUpdated) {
      return ResponseEntity
          .status(HttpStatus.OK)
          .body(ResponseDto.builder()
              .statusCode(AccountsConstants.STATUS_200)
              .statusMessage(AccountsConstants.MESSAGE_200)
              .build());
    } else {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ResponseDto.builder()
              .statusCode(AccountsConstants.STATUS_500)
              .statusMessage(AccountsConstants.MESSAGE_500)
              .build());

    }
  }

  @Operation(summary = "Delete an account", description = "Delete an account and the assigned customer by mobileNumber")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
      @ApiResponse(responseCode = "404", description = "HTTP Status NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), examples = {
          @ExampleObject(value = "{\"apiPath\": \"uri=/api/v1/accounts/+122234567890\", \"errorCode\": \"404\", \"errorMessage\": \"Account not found ...\", \"errorTime\": \"2024-07-04T11:12:13\"}") }, mediaType = MediaType.APPLICATION_JSON_VALUE)),
      @ApiResponse(responseCode = "500", description = "HTTP Status INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), examples = {
          @ExampleObject(value = "{\"apiPath\": \"uri=/api/v1/accounts\", \"errorCode\": \"500\", \"errorMessage\": \"An error occurred ...\", \"errorTime\": \"2024-07-04T11:12:13\"}") }, mediaType = MediaType.APPLICATION_JSON_VALUE))
  })
  @DeleteMapping(ACCOUNTS_PATH + "/{mobileNumber}")
  public ResponseEntity<ResponseDto> deleteAccount(@PathVariable String mobileNumber) {

    var isDeleted = accountsService.deleteAccount(mobileNumber);

    if (isDeleted) {
      return ResponseEntity
          .status(HttpStatus.OK)
          .body(ResponseDto.builder()
              .statusCode(AccountsConstants.STATUS_200)
              .statusMessage(AccountsConstants.MESSAGE_200)
              .build());
    } else {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ResponseDto.builder()
              .statusCode(AccountsConstants.STATUS_500)
              .statusMessage(AccountsConstants.MESSAGE_500)
              .build());
    }
  }

}
