package com.github.dhavalmehta1997.savetogoogledrive.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class GenericOauthController extends BaseController {

    @GetMapping("/oauth/authorized")
    public RedirectView redirectAfterSuccessfulAuthentication() {
        return new RedirectView("/index.jsp");
    }

    @GetMapping("/oauth//unauthorized")
    public RedirectView redirectAfterUnsuccessfulAuthentication() {
        return new RedirectView("/index.jsp");
    }

}
