package com.github.dhaval_mehta.savetogoogledrive.controller.rest.oauth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("/api")
@Api(description = "provides functionalities regarding sessions.", produces = "application/json", consumes = "application/json")
public class SessionController {

    private final HttpSession session;

    @Autowired
    public SessionController(HttpSession session) {
        this.session = session;
    }

    @GetMapping("/checkSession")
    @ApiOperation(value = "utility to check session is alive or not")
    @ApiResponses({@ApiResponse(code = 200, message = "session is alive and you are authenticated"),
            @ApiResponse(code = 401, message = "your session might be expired. Please re-authenticate.")})
    public void checkSession(HttpServletResponse response) throws IOException {
        if (session.getAttribute("user") != null)
            return;

        response.sendRedirect("/index.jsp");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
