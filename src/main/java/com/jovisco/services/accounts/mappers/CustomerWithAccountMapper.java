package com.jovisco.services.accounts.mappers;

import com.jovisco.services.accounts.dtos.CustomerWithAccountDto;
import com.jovisco.services.accounts.entities.Account;
import com.jovisco.services.accounts.entities.Customer;

public class CustomerWithAccountMapper {

    public static CustomerWithAccountDto mapToCustomerWithAccountDto(Customer customer, Account account) {
        return CustomerWithAccountDto.builder()
                .customer(CustomerMapper.mapToCustomerDto(customer))
                .account(AccountMapper.mapToAccountDto(account))
                .build();
    }
}
