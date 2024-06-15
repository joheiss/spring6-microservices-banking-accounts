package com.jovisco.services.accounts.mappers;

import com.jovisco.services.accounts.dtos.CustomerDetailsDto;
import com.jovisco.services.accounts.entities.Account;
import com.jovisco.services.accounts.entities.Customer;

public class CustomerDetailsMapper {

  public static CustomerDetailsDto mapToCustomerDetailsDto(Customer customer, Account account) {
    return CustomerDetailsDto.builder()
        .name(customer.getName())
        .email(customer.getEmail())
        .mobileNumber(customer.getMobileNumber())
        .account(AccountMapper.mapToAccountDto(account))
        .build();
  }
}
