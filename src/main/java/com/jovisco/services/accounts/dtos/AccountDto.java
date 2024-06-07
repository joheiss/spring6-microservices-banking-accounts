package com.jovisco.services.accounts.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {

    private Long id;

    private String type;

    private String branchAddress;
}
