/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.dhaval_mehta.savetogoogledrive.model;

import java.time.Instant;

/**
 * @author Dhaval
 */
public class Token {

    private String accessToken;
    private String tokenType;
    private Integer expiresIn;
    private String refreshToken;
    private String idToken;
    private long tokenUpdatedAt;

    public Token() {
        tokenUpdatedAt = Instant.EPOCH.getEpochSecond();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public long getTokenUpdatedAt() {
        return tokenUpdatedAt;
    }

    public void setTokenUpdatedAt(long tokenUpdatedAt) {
        this.tokenUpdatedAt = tokenUpdatedAt;
    }
}
