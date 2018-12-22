package com.github.cloud_transfer.cloud_transfer.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.github.cloud_transfer.cloud_transfer.model.token.Token;

import java.util.HashSet;
import java.util.Set;

public class User {

    @JsonSetter("given_name")
    private String name;

    @JsonSetter("picture")
    private String profilePhotoUrl;

    private String email;
    private Set<Token> tokens = new HashSet<>();

    public String getName() {
        return name;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public String getEmail() {
        return email;
    }
}
