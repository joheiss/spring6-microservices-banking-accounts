package com.jovisco.services.accounts.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Schema(name = "Customer and Account", description = "Schema to hold selected customer and account data")
@Data @Builder
public class CustomerWithAccountDto {

  @Schema(description = "Customer data")
  @NotNull
  @Valid
  private CustomerDto customer;

  @Schema(description = "Account data")
  @NotNull
  @Valid
  private AccountDto account;
}
