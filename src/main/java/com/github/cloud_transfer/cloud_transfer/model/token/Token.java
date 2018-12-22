package com.github.cloud_transfer.cloud_transfer.model.token;

public interface Token {
    String getAccessToken();

    TokenType getType();
}
