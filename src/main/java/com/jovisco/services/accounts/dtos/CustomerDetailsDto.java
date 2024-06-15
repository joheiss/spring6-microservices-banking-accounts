package com.jovisco.services.accounts.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Schema(name = "CustomerDetails", description = "Schema to hold selected accounts, loans and cards data for a customer")
@Data
@Builder
public class CustomerDetailsDto {

  @Schema(description = "Customer name", example = "John Doe")
  @NotEmpty(message = "Name must not be empty")
  @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters long")
  private String name;

  @Schema(description = "Customer email address", example = "john.doe@example.com")
  @NotEmpty(message = "Email must not be empty")
  @Email(message = "Email must contain a valid email address")
  private String email;

  @Schema(description = "Mobile phone number", example = "+122234567890")
  @NotEmpty(message = "Mobile number must not be empty")
  @Pattern(regexp = "^\\+[1-9]{1}[0-9]{3,14}$", message = "Mobile number must be valid")
  private String mobileNumber;

  @Schema(description = "Account details for Customer")
  private AccountDto account;

  @Schema(description = "Loan details for Customer")
  private LoanDto loan;

  @Schema(description = "Card details for Customer")
  private CardDto card;

}
