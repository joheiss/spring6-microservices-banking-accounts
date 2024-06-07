package com.jovisco.services.accounts.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerWithAccountDto {

    private CustomerDto customer;

    private AccountDto account;
}
