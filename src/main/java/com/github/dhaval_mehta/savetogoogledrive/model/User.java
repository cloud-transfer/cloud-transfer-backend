/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.dhaval_mehta.savetogoogledrive.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Dhaval
 */
public class User {

    @SerializedName("given_name")
    private String name;

    @SerializedName("picture")
    private String profilePhotoUrl;

    private String email;
    private Token token;
    private long tokenUpdatedAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public long getTokenUpdatedAt() {
        return tokenUpdatedAt;
    }

    public void setTokenUpdatedAt(long tokenUpdatedAt) {
        this.tokenUpdatedAt = tokenUpdatedAt;
    }

    @Override
    public String toString() {
        return "User{" + "name=" + name + ", email=" + email + ", profilePhotoUrl=" + profilePhotoUrl + ", token=" + token + ", tokenUpdatedAt=" + tokenUpdatedAt + '}';
    }

    public synchronized void refreshTokenIfNecessary() {
        long elapsedTime = Instant.EPOCH.getEpochSecond() - token.getTokenUpdatedAt();

        if (token.getExpiresIn() - elapsedTime < 100)
            refreshToken();
    }

    private void refreshToken() {
        try {
            String refreshToken = token.getRefreshToken();

            String userUrl = "https://www.googleapis.com/oauth2/v4/token";
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(userUrl);

            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("client_id", System.getenv("client_id")));
            parameters.add(new BasicNameValuePair("client_secret", System.getenv("client_secret")));
            parameters.add(new BasicNameValuePair("grant_type", "refresh_token"));
            parameters.add(new BasicNameValuePair("refresh_token", refreshToken));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters));

            org.apache.http.HttpResponse response = httpClient.execute(httpPost);
            int httpStatusCode = response.getStatusLine().getStatusCode();
            InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());

            if (httpStatusCode / 100 == 2) {
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                token = gson.fromJson(reader, Token.class);
                token.setRefreshToken(refreshToken);
                tokenUpdatedAt = Instant.EPOCH.getEpochSecond();
            }
        } catch (IOException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
