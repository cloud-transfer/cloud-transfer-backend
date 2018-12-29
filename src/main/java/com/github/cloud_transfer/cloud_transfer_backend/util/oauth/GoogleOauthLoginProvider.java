package com.github.cloud_transfer.cloud_transfer_backend.util.oauth;

import com.github.cloud_transfer.cloud_transfer_backend.model.User;

public class GoogleOauthLoginProvider implements OauthLoginProvider {

    @Override
    public User getUser(String token) {
        var user = new User();
        user.setEmail("dhaval.mehta197@gmail.com");
        return user;
    }

}
