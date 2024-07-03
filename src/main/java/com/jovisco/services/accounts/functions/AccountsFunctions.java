package com.jovisco.services.accounts.functions;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jovisco.services.accounts.services.AccountsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class AccountsFunctions {

  @Bean
  public Consumer<Long> updateCommunication(AccountsService accountsService) {

    return accountNumber -> {
      log.info("Updating communication status for account number: {}", accountNumber);
      accountsService.updateCommunicationStatus(accountNumber);
    };
  }
}
