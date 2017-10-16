package com.github.dhavalmehta1997.savetogoogledrive.controller.oauth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("api/oauth")
public class GenericOauthController {

    @GetMapping("/authorized")
    public RedirectView redirectAfterSuccessfulAuthentication() {
        return new RedirectView("/index.jsp");
    }

    @GetMapping("/unauthorized")
    public RedirectView redirectAfterUnsuccessfulAuthentication() {
        return new RedirectView("/index.jsp");
    }

}
