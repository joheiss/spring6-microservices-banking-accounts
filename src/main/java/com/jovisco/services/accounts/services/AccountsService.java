package com.jovisco.services.accounts.services;

import com.jovisco.services.accounts.dtos.CustomerDto;
import com.jovisco.services.accounts.dtos.CustomerWithAccountDto;

public interface AccountsService {

    /**
     * 
     * @param customerDto
     */
    void createAccount(CustomerDto customerDto);

    /**
     * 
     * @param mobileNumber
     * @return
     */
    CustomerWithAccountDto fetchAccount(String mobileNumber);

    /**
     * 
     * @param customerDto
     * @return boolean indicating if update was successful
     */
    boolean updateAccount(CustomerWithAccountDto customerWithAccountDto);

    /**
     * 
     * @param accountNumber
     * @return
     */
    boolean updateCommunicationStatus(Long accountNumber);

    /**
     * 
     * @param mobileNumber
     * @return boolean indicating if delete was successful
     */
    boolean deleteAccount(String mobileNumber);
}
