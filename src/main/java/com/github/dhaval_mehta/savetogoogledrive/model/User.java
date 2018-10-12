package com.github.dhaval_mehta.savetogoogledrive.model;

import com.fasterxml.jackson.annotation.JsonSetter;

public class User {

    @JsonSetter("given_name")
    private String name;

    @JsonSetter("picture")
    private String profilePhotoUrl;

    private String email;

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
