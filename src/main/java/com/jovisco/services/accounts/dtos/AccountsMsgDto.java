package com.jovisco.services.accounts.dtos;

/**
 * @param accoutNumber
 * @param name
 * @param email
 * @param mobileNumber
 */
public record AccountsMsgDto(
    Long accountNumber, String name, String email, String mobileNumber) {

}
