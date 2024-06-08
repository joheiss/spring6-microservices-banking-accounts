package com.jovisco.services.accounts.services;

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
public class AccountsServiceImpl implements AccountsService {

  private final AccountsRepository accountsRepository;
  private final CustomersRepository customersRepository;

  @Override
  public void createAccount(CustomerDto customerDto) {

    var customer = CustomerMapper.mapToCustomer(customerDto);

    // check if customer alrteady exists
    customersRepository
        .findByMobileNumber(customer.getMobileNumber())
        .ifPresent(c -> {
          throw new CustomerAlreadyExistsException(
              "Customer already registered with given mobile number" + customer.getMobileNumber());
        });

    var savedCustomer = customersRepository.save(customer);

    var account = buildNewAccount(savedCustomer);
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

  @Override
  public boolean updateAccount(CustomerWithAccountDto customerWithAccountDto) {

    boolean isUpdated = false;

    // update account
    var accountDto = customerWithAccountDto.getAccount();
    var accountId = accountDto.getId();

    if (accountDto == null || accountId == null) return isUpdated;

    var account = accountsRepository
        .findById(accountDto.getId())
        .orElseThrow(
            () -> new ResourceNotFoundException("Account", "Id", accountDto.getId().toString()));

    account.setType(accountDto.getType());
    account.setBranchAddress(accountDto.getBranchAddress());

    account = accountsRepository.save(account);

    // update customer
    Long customerId = account.getCustomerId();
    var customerDto = customerWithAccountDto.getCustomer();

    if (customerDto == null || customerId == null) return isUpdated;

    var customer = customersRepository
        .findById(customerId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Customer", "Id", customerId.toString()));

    customer.setName(customerDto.getName());
    customer.setEmail(customerDto.getEmail());
    customer.setMobileNumber(customerDto.getMobileNumber());

    customersRepository.save(customer);

    isUpdated = true;

    return isUpdated;
  }

  @Override
  public boolean deleteAccount(String mobileNumber) {

    var foundCustomer = customersRepository
        .findByMobileNumber(mobileNumber)
        .orElseThrow(
            () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

    // delete customer
    customersRepository.deleteById(foundCustomer.getId());

    // delete account
    accountsRepository.deleteByCustomerId(foundCustomer.getId());

    return true;
  }

}
