package com.github.cloud_transfer.cloud_transfer_backend.controller.login;

import com.github.cloud_transfer.cloud_transfer_backend.model.User;
import com.github.cloud_transfer.cloud_transfer_backend.security.JwtConfig;
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
    private final JwtTokenIssuer tokenIssueer;
    private final JwtConfig jwtConfig;

    @Autowired
    public OauthLoginController(UserService userService, JwtTokenIssuer tokenIssueer, JwtConfig jwtConfig) {
        this.userService = userService;
        this.tokenIssueer = tokenIssueer;
        this.jwtConfig = jwtConfig;
    }


    @GetMapping("{provider}")
    public ResponseEntity<Void> oauth2Login(@PathVariable OauthLoginProviders provider, @RequestParam String token) {

        OauthLoginProvider loginProvider = OauthLoginProviderFactory.get(provider);
        User user = loginProvider.getUser(token);

        User finalUser = userService.findUserByEmail(user.getEmail())
                .orElseGet(() -> userService.createUser(user));

        String authenticationToken = jwtConfig.getPrefix() + tokenIssueer.issueFor(finalUser);

        return ResponseEntity.noContent()
                .header(jwtConfig.getHeader(), authenticationToken)
                .build();
    }
}
