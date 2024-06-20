package com.jovisco.services.accounts.services.clients;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.jovisco.services.accounts.dtos.CardDto;

@Component
public class CardsFallback implements CardsFeignClient {

  @Override
  public ResponseEntity<CardDto> fetchCard(String correlationId, String mobileNumber) {
    return null;
  }

}
