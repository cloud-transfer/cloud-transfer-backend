/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import model.GoogleApiTokenInfo;
import oauth2.OAuthUtility;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.EnglishReasonPhraseCatalog;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

/**
 *
 * @author Dhaval
 */
public class DriveUploader
{

    private URL downloadUrl;
    private long contentLength;
    private String contentType;
    private String fileName;
    private GoogleApiTokenInfo tokenInfo;
    private static final String POST_URL = "https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable";
    private String PUT_URL;
    private long uploadedBytes = 0;
    private final long chunkSize = 100 * 4 * 256 * 1024; // 100 MB
    private String errorMessage;

    public DriveUploader(URL downloadUrl, GoogleApiTokenInfo tokenInfo)
    {
	this.downloadUrl = downloadUrl;
	this.tokenInfo = tokenInfo;
    }

    public DriveUploader(URL downloadUrl, String fileName, GoogleApiTokenInfo tokenInfo)
    {
	this(downloadUrl, tokenInfo);
	this.fileName = fileName;
    }

    public void upload() throws IOException
    {
	fetchFileMetadata();
	obtainUploadUrl();

	while (uploadedBytes < contentLength)
	{
	    long end = uploadedBytes + chunkSize - 1;

	    if (end >= contentLength)
		end = contentLength - 1;

	    uploadPartially(uploadedBytes, end);
	}
    }

    private void fetchFileMetadata() throws IOException
    {
	if (fileName == null)
	    fileName = FilenameUtils.getName(downloadUrl.getPath());

	HttpClient httpClient = HttpClientBuilder.create().build();
	HttpHead httpHead = new HttpHead(downloadUrl.toString());

	HttpResponse response;
	String method = "head";

	try
	{
	    response = httpClient.execute(httpHead);
	}
	catch (NoHttpResponseException e)
	{
	    method = "get";
	    HttpGet httpGet = new HttpGet(downloadUrl.toString());
	    httpGet.addHeader("Range", "bytes=0-0");
	    response = httpClient.execute(httpGet);
	}

	int statusCode = response.getStatusLine().getStatusCode();

	if (OAuthUtility.isHttpSuccessStatusCode(statusCode))
	{
	    if (method.equals("head"))
		contentLength = Long.parseLong(response.getFirstHeader("Content-Length").getValue());
	    else
	    {
		String contentRange = response.getFirstHeader("Content-Range").getValue();
		contentLength = Long.parseLong(contentRange.substring(contentRange.lastIndexOf('/') + 1));
	    }
	    contentType = response.getFirstHeader("Content-Type").getValue();
	}
	else
	    throw new HttpResponseException(statusCode, EnglishReasonPhraseCatalog.INSTANCE.getReason(statusCode, Locale.US));
    }

    private int uploadPartially(long start, long end) throws IOException
    {
	HttpClient httpClient = HttpClientBuilder.create().build();
	HttpGet httpGet = new HttpGet(downloadUrl.toString());

	String rangeHeaderValue = "bytes=" + start + "-" + end;
	httpGet.setHeader("Range", rangeHeaderValue);

	HttpResponse response = httpClient.execute(httpGet);
	Header contentTypeHeader = response.getFirstHeader("Content-Range");

	if (contentTypeHeader == null)
	    contentTypeHeader = new BasicHeader("Content-Range", "bytes	" + start + "-" + end + "/" + contentLength);

	HttpPost httpPost = new HttpPost(PUT_URL);
	httpPost.addHeader(contentTypeHeader);
	httpPost.setEntity(response.getEntity());

	HttpResponse postResponse = httpClient.execute(httpPost);
	int statusCode = postResponse.getStatusLine().getStatusCode();

	if (statusCode < 400)
	{
	    uploadedBytes = end + 1;
	    return statusCode;
	}
	else
	{
	    BufferedInputStream bis = new BufferedInputStream(postResponse.getEntity().getContent());
	    byte arr[] = new byte[1024];

	    while (bis.read(arr) != -1)
	    {
		System.err.write(arr);
	    }
	    throw new HttpResponseException(statusCode, EnglishReasonPhraseCatalog.INSTANCE.getReason(statusCode, Locale.US));
	}
    }

    private void obtainUploadUrl() throws IOException
    {
	HttpClient httpClient = HttpClientBuilder.create().build();
	HttpPost httpPost = new HttpPost(POST_URL);

	ObjectMapper mapper = new ObjectMapper();
	ObjectNode node = mapper.createObjectNode();
	node.put("name", fileName);
	String postBody = node.toString();

	BasicHttpEntity entity = new BasicHttpEntity();
	entity.setContent(new ByteArrayInputStream(postBody.getBytes(StandardCharsets.UTF_8)));
	httpPost.setEntity(entity);

	httpPost.addHeader("Content-Type", "application/json");
	httpPost.addHeader("Authorization", tokenInfo.getTokenType() + " " + tokenInfo.getAccessToken());
	httpPost.addHeader("X-Upload-Content-Type", contentType);
	httpPost.addHeader("X-Upload-Content-Length", contentType);

	HttpResponse response = httpClient.execute(httpPost);
	PUT_URL = response.getFirstHeader("Location").getValue();
    }

    @Override
    public String toString()
    {
	return "DriveUploader{" + "downloadUrl=" + downloadUrl + ", contentLength=" + contentLength + ", contentType=" + contentType + ", fileName=" + fileName + ", tokenInfo=" + tokenInfo + ", PUT_URL=" + PUT_URL + ", uploadedBytes=" + uploadedBytes + ", chunkSize=" + chunkSize + '}';
    }
}
