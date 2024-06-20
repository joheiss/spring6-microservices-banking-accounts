package com.jovisco.services.accounts.services.clients;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.jovisco.services.accounts.dtos.LoanDto;

@Component
public class LoansFallback implements LoansFeignClient {

  @Override
  public ResponseEntity<LoanDto> fetchLoan(String correlationId, String mobileNumber) {
    return null;
  }
}
