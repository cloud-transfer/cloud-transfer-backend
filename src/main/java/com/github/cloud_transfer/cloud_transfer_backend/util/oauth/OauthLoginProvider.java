package com.github.cloud_transfer.cloud_transfer_backend.util.oauth;

import com.github.cloud_transfer.cloud_transfer_backend.model.User;

public interface OauthLoginProvider {
    User getUser(String token);
}
