package com.github.cloud_transfer.cloud_transfer_backend.authentication.oauth;

import com.github.cloud_transfer.cloud_transfer_backend.model.User;

public interface OauthLoginProvider {
    User getUser(String token);
}
