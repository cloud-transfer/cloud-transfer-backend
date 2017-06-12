/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Dhaval
 */
public class GoogleApiTokenInfo
{

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("access_token")
    public String getAccessToken()
    {
	return accessToken;
    }

    @JsonProperty("access_token")
    public void setAccessToken(String accessToken)
    {
	this.accessToken = accessToken;
    }

    @JsonProperty("token_type")
    public String getTokenType()
    {
	return tokenType;
    }

    @JsonProperty("token_type")
    public void setTokenType(String tokenType)
    {
	this.tokenType = tokenType;
    }

    @JsonProperty("expires_in")
    public Integer getExpiresIn()
    {
	return expiresIn;
    }

    @JsonProperty("expires_in")
    public void setExpiresIn(Integer expiresIn)
    {
	this.expiresIn = expiresIn;
    }

    @JsonProperty("refresh_token")
    public String getRefreshToken()
    {
	return refreshToken;
    }

    @JsonProperty("refresh_token")
    public void setRefreshToken(String refreshToken)
    {
	this.refreshToken = refreshToken;
    }

    @Override
    public String toString()
    {
	return "GoogleApiTokenInfo{" + "accessToken=" + accessToken + ", tokenType=" + tokenType + ", expiresIn=" + expiresIn + ", refreshToken=" + refreshToken + '}';
    }
}
