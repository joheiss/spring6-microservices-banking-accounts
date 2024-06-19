package com.jovisco.services.accounts.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovisco.services.accounts.dtos.CustomerDetailsDto;
import com.jovisco.services.accounts.dtos.ErrorResponseDto;
import com.jovisco.services.accounts.services.CustomersService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "REST APIs for Customer Details in Banking Microservices", description = "REST APIs to READ Customer Details in Banking Microservices")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {

  public static final String CUSTOMERS_PATH = "/customers";
  public static final String CUSTOMERS_MOBILENUMBER_PATH = CUSTOMERS_PATH + "/{mobileNumber}";

  private final CustomersService customersService;

  @Operation(summary = "Fetch a singlec ustomer by the mobile number", description = "Fetch data from customer for a given mobile number")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
      @ApiResponse(responseCode = "404", description = "HTTP Status NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), examples = {
          @ExampleObject(value = "{\"apiPath\": \"uri=/api/v1/customers/+122234567890\", \"errorCode\": \"404\", \"errorMessage\": \"Customer not found ...\", \"errorTime\": \"2024-07-04T11:12:13\"}")
      }, mediaType = MediaType.APPLICATION_JSON_VALUE)),
      @ApiResponse(responseCode = "500", description = "HTTP Status INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), examples = {
          @ExampleObject(value = "{\"apiPath\": \"uri=/api/v1/customers\", \"errorCode\": \"500\", \"errorMessage\": \"An error occurred ...\", \"errorTime\": \"2024-07-04T11:12:13\"}") }, mediaType = MediaType.APPLICATION_JSON_VALUE))
  })
  @GetMapping("/customers/{mobileNumber}")
  public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(
    @RequestHeader("jovisco-banking-correlation-id") String correlationId,
    @PathVariable String mobileNumber
  ) {

    log.info("Log level INFO message");
    log.error("Log level ERROR message");
    log.warn("Log level WARN message");
    log.debug("Log level DEBUG message");
    
    log.debug("jovisco-banking-correlation-id received: {}", correlationId);

    var found = customersService.fetchDetails(mobileNumber, correlationId);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(found);
  }
}
