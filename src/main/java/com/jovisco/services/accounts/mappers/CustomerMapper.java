package com.jovisco.services.accounts.mappers;

import com.jovisco.services.accounts.dtos.CustomerDto;
import com.jovisco.services.accounts.entities.Customer;

public class CustomerMapper {

    /**
     * 
     * @param customer
     * @return customerDto
     */
    public static CustomerDto mapToCustomerDto(Customer customer) {
        return CustomerDto.builder()
                .name(customer.getName())
                .email(customer.getEmail())
                .mobileNumber(customer.getMobileNumber())
                .build();
    }

    /**
     * 
     * @param customerDto
     * @return customer
     */
    public static Customer mapToCustomer(CustomerDto customerDto) {
        return Customer.builder()
                .name(customerDto.getName())
                .email(customerDto.getEmail())
                .mobileNumber(customerDto.getMobileNumber())
                .build();
    }

}
