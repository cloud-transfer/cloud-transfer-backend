package com.github.dhaval_mehta.savetogoogledrive.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.github.dhaval_mehta.savetogoogledrive.model.token.Token;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private Set<Token> tokens = new HashSet<>();
}
