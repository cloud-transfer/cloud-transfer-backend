package com.github.dhaval_mehta.cloud_transfer.model.token;

public interface Token {
    String getAccessToken();

    TokenType getType();
}
