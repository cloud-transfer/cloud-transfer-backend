package com.github.cloud_transfer.cloud_transfer.model.token;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue(value = TokenType.GoogleDriveToken)
public class GoogleDriveToken extends Token {

    private static final Logger logger = LoggerFactory.getLogger(GoogleDriveToken.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime;

    @Override
    public String getAccessToken() {
        if (LocalDateTime.now().isAfter(expiryTime))
            refreshToken();

        return accessToken;
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
