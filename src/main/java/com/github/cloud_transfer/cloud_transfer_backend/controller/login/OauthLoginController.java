package com.github.cloud_transfer.cloud_transfer_backend.controller.login;

import com.github.cloud_transfer.cloud_transfer_backend.model.User;
import com.github.cloud_transfer.cloud_transfer_backend.security.JwtTokenIssuer;
import com.github.cloud_transfer.cloud_transfer_backend.service.UserService;
import com.github.cloud_transfer.cloud_transfer_backend.util.oauth.OauthLoginProvider;
import com.github.cloud_transfer.cloud_transfer_backend.util.oauth.OauthLoginProviderFactory;
import com.github.cloud_transfer.cloud_transfer_backend.util.oauth.OauthLoginProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login/oauth")
public class OauthLoginController {

    private final UserService userService;
    private final JwtTokenIssuer tokenIssuer;

    @Autowired
    public OauthLoginController(UserService userService, JwtTokenIssuer tokenIssuer) {
        this.userService = userService;
        this.tokenIssuer = tokenIssuer;
    }


    @PostMapping("{provider}")
    public ResponseEntity oauth2Login(@PathVariable OauthLoginProviders provider, @RequestParam String token) {
        OauthLoginProvider loginProvider = OauthLoginProviderFactory.get(provider);
        User user = userService.findByEmailOrCreate(loginProvider.getUser(token));
        var authenticationToken = tokenIssuer.issueFor(user);
        return ResponseEntity.ok(authenticationToken);
    }
}
