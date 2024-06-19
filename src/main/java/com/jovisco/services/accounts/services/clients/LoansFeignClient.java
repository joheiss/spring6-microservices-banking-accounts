package com.jovisco.services.accounts.services.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.jovisco.services.accounts.dtos.LoanDto;

@FeignClient("loans")
public interface LoansFeignClient {

  @GetMapping("/api/v1/loans/{mobileNumber}")
  ResponseEntity<LoanDto> fetchLoan(
    @RequestHeader("jovisco-banking-correlation-id") String correlationId,
    @PathVariable String mobileNumber);
}
