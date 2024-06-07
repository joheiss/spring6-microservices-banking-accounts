package com.jovisco.services.accounts.services;

import com.jovisco.services.accounts.dtos.CustomerDto;
import com.jovisco.services.accounts.dtos.CustomerWithAccountDto;

public interface IAccountsService {

    /**
     * 
     * @param customerDto
     */
    void createAccount(CustomerDto customerDto);

    CustomerWithAccountDto fetchAccount(String mobileNumber);
}
