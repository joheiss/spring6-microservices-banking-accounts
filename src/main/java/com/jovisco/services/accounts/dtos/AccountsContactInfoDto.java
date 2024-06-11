package com.jovisco.services.accounts.dtos;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "accounts")
public record AccountsContactInfoDto(
    String message,
    Map<String, String> contact,
    List<String> support) {
}
