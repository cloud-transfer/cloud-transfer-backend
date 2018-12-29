package com.github.cloud_transfer.cloud_transfer_backend.util.oauth;

import javax.validation.constraints.NotNull;

public class OauthLoginProviderFactory {

    private static final GoogleOauthLoginProvider googleOauthLoginProvider = new GoogleOauthLoginProvider();

    public static OauthLoginProvider get(@NotNull OauthLoginProviders provider) {

        switch (provider) {
            case google:
                return googleOauthLoginProvider;
            default:
                throw new RuntimeException();
        }
    }

}
