package com.jovisco.services.accounts.services;

import org.springframework.stereotype.Service;

import com.jovisco.services.accounts.dtos.CustomerDetailsDto;
import com.jovisco.services.accounts.exceptions.ResourceNotFoundException;
import com.jovisco.services.accounts.mappers.CustomerDetailsMapper;
import com.jovisco.services.accounts.repositories.AccountsRepository;
import com.jovisco.services.accounts.repositories.CustomersRepository;
import com.jovisco.services.accounts.services.clients.CardsFeignClient;
import com.jovisco.services.accounts.services.clients.LoansFeignClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomersServiceImpl implements CustomersService {

  private final CustomersRepository customersRepository;
  private final AccountsRepository accountsRepository;
  private final LoansFeignClient loansFeignClient;
  private final CardsFeignClient cardsFeignClient;

  @Override
  public CustomerDetailsDto fetchDetails(String mobileNumber, String correlationId) {

    // fetch customer data
    var foundCustomer = customersRepository
        .findByMobileNumber(mobileNumber)
        .orElseThrow(
            () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

    // fetch account data
    var foundAccount = accountsRepository
        .findByCustomerId(foundCustomer.getId())
        .orElseThrow(
            () -> new ResourceNotFoundException("Account", "customerId", foundCustomer.getId().toString()));

    var detailsDto = CustomerDetailsMapper.mapToCustomerDetailsDto(foundCustomer, foundAccount);

    // fetch loans data - via feign client
    var loanResponse = loansFeignClient.fetchLoan(correlationId, mobileNumber);
    if (null != loanResponse) {
      detailsDto.setLoan(loanResponse.getBody());
    }

    // fetch cards data - via feign client
    var cardResponse = cardsFeignClient.fetchCard(correlationId, mobileNumber);
    if (null != cardResponse) {
      detailsDto.setCard(cardResponse.getBody());
    }

    return detailsDto;
  }
}
