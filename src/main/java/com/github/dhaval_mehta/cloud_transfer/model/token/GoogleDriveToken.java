package com.github.dhaval_mehta.cloud_transfer.model.token;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

public class GoogleDriveToken implements Token {

    private static final Logger logger = LoggerFactory.getLogger(GoogleDriveToken.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private LocalDateTime expiryTime;

    @Override
    public String getAccessToken() {
        if (LocalDateTime.now().isAfter(expiryTime))
            refreshToken();

        return accessToken;
    }

    @Override
    public TokenType getType() {
        return TokenType.GoogleDriveToken;
    }

    private void refreshToken() {
        try {
            HttpResponse<String> response = sendRefreshTokenRequest();
            if (response.statusCode() != 200) {
                logger.error("Error while refreshing token: " + response.statusCode(), response.body());
                return;
            }
            JsonNode responseJson = mapper.readTree(response.body());
            expiryTime = LocalDateTime.now().plusSeconds(responseJson.get("expires_in").asInt());
            accessToken = responseJson.get("access_token").asText();
        } catch (InterruptedException | IOException e) {
            logger.error(null, e);
        }
    }

    private HttpResponse<String> sendRefreshTokenRequest() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest refreshTokenRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://www.googleapis.com/oauth2/v4/token"))
                .POST(HttpRequest.BodyPublishers.ofString(refreshTokenRequestBody()))
                .build();

        return httpClient.send(refreshTokenRequest, HttpResponse.BodyHandlers.ofString());
    }

    private String refreshTokenRequestBody() {
        ObjectNode body = mapper.createObjectNode();
        body.put("client_id", System.getenv("client_id"));
        body.put("client_secret", System.getenv("client_secret"));
        body.put("grant_type", "refresh_token");
        body.put("refresh_token", refreshToken);
        return body.asText();
    }
}
