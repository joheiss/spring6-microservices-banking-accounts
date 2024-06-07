package com.jovisco.services.accounts.mappers;

import com.jovisco.services.accounts.dtos.AccountDto;
import com.jovisco.services.accounts.entities.Account;

public class AccountMapper {

    /**
     * 
     * @param account
     * @return accountDto
     */
    public static AccountDto mapToAccountDto(Account account) {

        return AccountDto.builder()
                .id(account.getId())
                .type(account.getType())
                .branchAddress(account.getBranchAddress())
                .build();
    }

    /**
     * 
     * @param accountDto
     * @return account
     */
    public static Account mapToAccount(AccountDto accountDto) {
        return Account.builder()
                .id(accountDto.getId())
                .type(accountDto.getType())
                .branchAddress(accountDto.getBranchAddress())
                .build();
    }
}
