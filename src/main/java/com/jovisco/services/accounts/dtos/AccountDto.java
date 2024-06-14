package com.jovisco.services.accounts.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Schema(name = "Account", description = "Schema to hold account data")
@Data @Builder
public class AccountDto {

  @Schema(description = "Account id", example = "1234567890")
  @NotNull(message = "Account id must not be empty")
  @Digits(integer = 10, fraction = 0, message = "Account id must be exactly 10 digits")
  private Long id;

  @Schema(description = "Account type", example = "Savings")
  @NotEmpty(message = "Account type must not be empty")
  private String type;

  @Schema(description = "Branch address", example = "123 Main Street, Los Angeles, CA")
  @NotEmpty(message = "branch address must not be empty")
  private String branchAddress;
}
