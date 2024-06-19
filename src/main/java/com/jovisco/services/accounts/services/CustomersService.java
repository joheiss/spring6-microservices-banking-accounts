package com.jovisco.services.accounts.services;

import com.jovisco.services.accounts.dtos.CustomerDetailsDto;

public interface CustomersService {

  CustomerDetailsDto fetchDetails(String mobileNumber, String correlationId);
}
