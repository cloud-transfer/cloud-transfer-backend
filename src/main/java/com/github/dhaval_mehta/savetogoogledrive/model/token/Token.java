package com.github.dhaval_mehta.savetogoogledrive.model.token;

public interface Token {
    String getAccessToken();

    TokenType getType();
}
