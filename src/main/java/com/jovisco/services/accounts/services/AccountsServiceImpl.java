package com.jovisco.services.accounts.services;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.jovisco.services.accounts.constants.AccountsConstants;
import com.jovisco.services.accounts.dtos.CustomerDto;
import com.jovisco.services.accounts.dtos.CustomerWithAccountDto;
import com.jovisco.services.accounts.entities.Account;
import com.jovisco.services.accounts.entities.Customer;
import com.jovisco.services.accounts.exceptions.CustomerAlreadyExistsException;
import com.jovisco.services.accounts.exceptions.ResourceNotFoundException;
import com.jovisco.services.accounts.mappers.CustomerMapper;
import com.jovisco.services.accounts.mappers.CustomerWithAccountMapper;
import com.jovisco.services.accounts.repositories.AccountsRepository;
import com.jovisco.services.accounts.repositories.CustomersRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountsServiceImpl implements IAccountsService {

    private final AccountsRepository accountsRepository;
    private final CustomersRepository customersRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {

        var customer = CustomerMapper.mapToCustomer(customerDto);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");

        // check if customer alrteady exists
        customersRepository
                .findByMobileNumber(customer.getMobileNumber())
                .ifPresent(c -> {
                    throw new CustomerAlreadyExistsException(
                            "Customer already registered with given mobile number" + customer.getMobileNumber());
                });

        var savedCustomer = customersRepository.save(customer);

        var account = buildNewAccount(savedCustomer);
        account.setCreatedAt(LocalDateTime.now());
        account.setCreatedBy("Anonymous");
        accountsRepository.save(account);
    }

    /**
     * Build an account entity with a account id generated using a special algorithm
     * 
     * @param customer
     * @return account
     */
    private Account buildNewAccount(Customer customer) {
        return Account.builder()
                .customerId((customer.getId()))
                .id(1000000000L + new Random().nextInt(900000000))
                .type(AccountsConstants.SAVINGS)
                .branchAddress(AccountsConstants.ADDRESS)
                .build();
    }

    @Override
    public CustomerWithAccountDto fetchAccount(String mobileNumber) {

        var foundCustomer = customersRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        var foundAccount = accountsRepository
                .findByCustomerId(foundCustomer.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Account", "customerId", foundCustomer.getId().toString()));

        return CustomerWithAccountMapper.mapToCustomerWithAccountDto(foundCustomer, foundAccount);
    }

}
