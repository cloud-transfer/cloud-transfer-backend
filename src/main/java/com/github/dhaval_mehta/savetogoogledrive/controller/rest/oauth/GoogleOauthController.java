package com.github.dhaval_mehta.savetogoogledrive.controller.rest.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dhaval_mehta.savetogoogledrive.exception.ApiException;
import com.github.dhaval_mehta.savetogoogledrive.model.User;
import com.github.dhaval_mehta.savetogoogledrive.model.token.GoogleDriveToken;
import com.github.dhaval_mehta.savetogoogledrive.model.token.Token;
import com.github.dhaval_mehta.savetogoogledrive.utility.HttpUtilities;
import io.swagger.annotations.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dhaval
 */
@RestController
@RequestMapping("/api/oauth/google")
@Api(description = "handles Google OAuth.", produces = "application/json", consumes = "application/json")
public class GoogleOauthController {

    private final static String PROFILE_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
    private final static String TOKEN_URL = "https://www.googleapis.com/oauth2/v4/token";
    private final static String OAUTH_URL = "https://accounts.google.com/o/oauth2/auth";
    private final static String ACCESS_TYPE = "offline";
    private final static String RESPONSE_TYPE = "code";
    private final static String SCOPE = "https://www.googleapis.com/auth/drive.file https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email";
    private final static String GRANT_TYPE = "authorization_code";
    private final static String CLIENT_ID = System.getenv("client_id");
    private final static String CLIENT_SECRET = System.getenv("client_secret");
    private final static String REDIRECT_URI = System.getenv("redirect_uri");
    private final static ObjectMapper mapper = new ObjectMapper();
    private final HttpSession session;

    @Autowired
    public GoogleOauthController(HttpSession session) {
        this.session = session;
    }

    @GetMapping(value = "/redirect")
    @ResponseStatus(HttpStatus.FOUND)
    @ApiOperation(value = "redirect client for oauth")
    @ApiResponses(@ApiResponse(code = 302, message = "server successfully redirected client for oauth", responseHeaders = @ResponseHeader(name = "Location", response = java.net.URL.class)))
    public void redirectForOauth2(HttpServletResponse response) throws URISyntaxException, IOException {
        String redirectUrl = new URIBuilder(OAUTH_URL)
                .addParameter("approval_prompt", "force")
                .addParameter("scope", SCOPE)
                .addParameter("access_type", ACCESS_TYPE)
                .addParameter("redirect_uri", REDIRECT_URI)
                .addParameter("response_type", RESPONSE_TYPE)
                .addParameter("client_id", CLIENT_ID)
                .build()
                .toString();
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/callback")
    @ResponseStatus(HttpStatus.FOUND)
    @ApiOperation(value = "handles oauth callback from google", hidden = true)
    @ApiResponses(@ApiResponse(code = 302, message = "redirect client for session check", responseHeaders = @ResponseHeader(name = "Location", response = java.net.URL.class)))
    public void handleCallback(@RequestParam("code") String code, HttpServletResponse response)
            throws IOException, URISyntaxException {
        Token token = getAccessToken(code);
        User user = getUser(token);
        response.sendRedirect("/");
        System.out.println(token);

		if (user != null) {
			session.setAttribute("user", user);
			response.sendRedirect("/");
		} else {
			response.setHeader("Location", "");
			response.sendRedirect("/");
		}
    }

    private Token getAccessToken(@NotNull String code) throws IOException {
        // Initialize client
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(TOKEN_URL);

        // add request parameters
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("code", code));
        parameters.add(new BasicNameValuePair("client_id", CLIENT_ID));
        parameters.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
        parameters.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI));
        parameters.add(new BasicNameValuePair("grant_type", GRANT_TYPE));
        httpPost.setEntity(new UrlEncodedFormEntity(parameters));

        // send request
        org.apache.http.HttpResponse response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        InputStream inputStream = response.getEntity().getContent();
        inputStream.transferTo(System.out);
		if (HttpUtilities.success(statusCode))
			return mapper.readValue(inputStream, GoogleDriveToken.class);

        throw new ApiException(HttpStatus.valueOf(statusCode));
    }

    private User getUser(@NotNull Token token) throws IOException, URISyntaxException {

        URIBuilder builder = new URIBuilder(PROFILE_URL);
        builder.addParameter("access_token", token.getAccessToken());

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(builder.build());
        org.apache.http.HttpResponse response = httpClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        InputStream inputStream = response.getEntity().getContent();

        if (HttpUtilities.success(statusCode)) {
            User user = mapper.readValue(inputStream, User.class);
//            user.setToken(token);
            return user;
        }

        throw new ApiException(HttpStatus.valueOf(statusCode));
    }
}
