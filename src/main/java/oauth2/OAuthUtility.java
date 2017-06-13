/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oauth2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.OAuthException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import model.GoogleApiTokenInfo;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author Dhaval
 */
public class OAuthUtility
{

    public static void refreshToken(GoogleApiTokenInfo token) throws UnsupportedEncodingException, IOException
    {
	String refreshToken = token.getRefreshToken();
	String tokenUrl = "https://www.googleapis.com/oauth2/v4/token";
	HttpClient httpClient = HttpClientBuilder.create().build();
	HttpPost httpPost = new HttpPost(tokenUrl);

	List<NameValuePair> parameters = new ArrayList<>();
	parameters.add(new BasicNameValuePair("client_id", System.getenv("client_id")));
	parameters.add(new BasicNameValuePair("client_secret", System.getenv("client_secret")));
	parameters.add(new BasicNameValuePair("grant_type", "refresh_token"));
	parameters.add(new BasicNameValuePair("refresh_token", refreshToken));
	httpPost.setEntity(new UrlEncodedFormEntity(parameters));

	org.apache.http.HttpResponse response = httpClient.execute(httpPost);
	int httpStatusCode = response.getStatusLine().getStatusCode();
	InputStream inputStream = response.getEntity().getContent();
	ObjectMapper objectMapper = new ObjectMapper();

	if (isHttpSuccessStatusCode(httpStatusCode))
	{
	    JsonNode rootNode = objectMapper.readTree(inputStream);
	    String newToken = rootNode.get("token").asText();
	    token.setAccessToken(newToken);
	}
	else
	{
	    JsonNode rootNode = objectMapper.readTree(inputStream);
	    OAuthException exception = new OAuthException();
	    exception.setError(rootNode.get("error").asText());
	    exception.setDescription(rootNode.get("description").asText());
	    throw exception;
	}
    }

    public static boolean isHttpSuccessStatusCode(int httpStatusCode)
    {
	return httpStatusCode / 100 == 2;
    }
}
