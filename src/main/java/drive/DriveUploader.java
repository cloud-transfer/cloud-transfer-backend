/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ErrorStatus;
import model.GoogleApiTokenInfo;
import model.Status;
import model.SuccessStatus;
import oauth2.OAuthUtility;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.EnglishReasonPhraseCatalog;

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
    private static final URL POST_URL;
    private URL PUT_URL;
    private long uploadedBytes;
    private final int chunkSize = 10 * 4 * 256 * 1024; // 10 MB
    private String errorMessage;
    private String status = "waiting";
    private static final Logger LOGGER;
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";

    static
    {
	try
	{
	    LOGGER = Logger.getLogger(DriveUploader.class.getName());
	    POST_URL = new URL("https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable");
	}
	catch (MalformedURLException ex)
	{
	    throw new ExceptionInInitializerError(ex);
	}
    }

    public DriveUploader(URL downloadUrl, GoogleApiTokenInfo tokenInfo)
    {
	this.downloadUrl = downloadUrl;
	this.tokenInfo = tokenInfo;
    }

    public DriveUploader(URL downloadUrl, GoogleApiTokenInfo tokenInfo, String fileName)
    {
	this(downloadUrl, tokenInfo);
	this.fileName = fileName;
    }

    public void init() throws UnknownHostException, HttpResponseException, IOException
    {
	fetchFileMetadata();
	obtainUploadUrl();
    }

    public void upload()
    {

	try
	{
	    status = "Uploading";

	    while (uploadedBytes < contentLength)
	    {
		long end = uploadedBytes + chunkSize - 1;

		if (end >= contentLength)
		    end = contentLength - 1;

		uploadPartially(uploadedBytes, end);
	    }

	    status = "Upload Complete";
	}
	catch (IOException ex)
	{
	    errorMessage = ex.toString();
	    LOGGER.log(Level.SEVERE, null, ex);
	}
    }

    private void fetchFileMetadata() throws IOException
    {
	if (fileName == null)
	    fileName = FilenameUtils.getName(downloadUrl.getPath());

	String method = "head";
	HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
	connection.setRequestProperty("User-Agent", USER_AGENT);

	try
	{
	    connection.setRequestMethod("HEAD");
	    connection.getResponseCode();
	}
	catch (IOException e)
	{
	    method = "get";
	    connection = (HttpURLConnection) downloadUrl.openConnection();
	    connection.setRequestProperty("User-Agent", USER_AGENT);
	    connection.setRequestMethod("GET");
	    connection.setRequestProperty("Range", "bytes=0-0");
	}

	int statusCode = connection.getResponseCode();

	if (OAuthUtility.isHttpSuccessStatusCode(statusCode))
	{
	    if (method.equals("head"))
		contentLength = connection.getContentLengthLong();
	    else
	    {
		String contentRange = connection.getHeaderField("Content-Range");
		contentLength = Long.parseLong(contentRange.substring(contentRange.lastIndexOf('/') + 1));
	    }
	    contentType = connection.getContentType();
	}
	else
	{
	    logHttpError(connection.getInputStream());
	    throw new HttpResponseException(statusCode, EnglishReasonPhraseCatalog.INSTANCE.getReason(statusCode, Locale.US));
	}
    }

    private int uploadPartially(long start, long end) throws IOException
    {
	HttpURLConnection downloadConnection = (HttpURLConnection) downloadUrl.openConnection();
	downloadConnection.setRequestMethod("GET");

	String rangeHeaderValue = "bytes=" + start + "-" + end;
	downloadConnection.setRequestProperty("Range", rangeHeaderValue);
	downloadConnection.setRequestProperty("User-Agent", USER_AGENT);
	String contentTypeString = downloadConnection.getHeaderField("Content-Range");

	if (contentTypeString == null)
	    contentTypeString = "bytes " + start + "-" + end + "/" + contentLength;

	HttpURLConnection uploadConnection = (HttpURLConnection) PUT_URL.openConnection();
	uploadConnection.setRequestProperty("Content-Range", contentTypeString);
	uploadConnection.setDoOutput(true);
	uploadConnection.setRequestProperty("User-Agent", USER_AGENT);
	BufferedInputStream bis;
	try (BufferedOutputStream bos = new BufferedOutputStream(uploadConnection.getOutputStream()))
	{
	    bis = new BufferedInputStream(downloadConnection.getInputStream());
	    byte[] buffer = new byte[chunkSize];
	    int readBytesCount;
	    while ((readBytesCount = bis.read(buffer)) != -1)
		bos.write(buffer, 0, readBytesCount);
	    bos.flush();
	}
	bis.close();

	int statusCode = uploadConnection.getResponseCode();

	if (statusCode < 400)
	{
	    uploadedBytes = end + 1;
	    return statusCode;
	}
	else
	{
	    logHttpError(uploadConnection.getInputStream());
	    throw new HttpResponseException(statusCode, EnglishReasonPhraseCatalog.INSTANCE.getReason(statusCode, Locale.US));
	}

    }

    private void obtainUploadUrl() throws IOException
    {
	HttpURLConnection connection = (HttpURLConnection) POST_URL.openConnection();
	connection.setRequestMethod("POST");
	connection.setRequestProperty("User-Agent", USER_AGENT);

	ObjectMapper mapper = new ObjectMapper();
	ObjectNode node = mapper.createObjectNode();
	node.put("name", fileName);
	String postBody = node.toString();

	connection.setDoOutput(true);
	connection.setRequestProperty("Content-Type", "application/json");
	connection.setRequestProperty("Authorization", tokenInfo.getTokenType() + " " + tokenInfo.getAccessToken());
	connection.setRequestProperty("X-Upload-Content-Type", contentType);
	connection.setRequestProperty("X-Upload-Content-Length", String.valueOf(contentLength));

	try (PrintWriter writer = new PrintWriter(connection.getOutputStream()))
	{
	    writer.print(postBody);
	    writer.flush();
	}

	PUT_URL = new URL(connection.getHeaderField("Location"));
    }

    @Override
    public String toString()
    {
	return "DriveUploader{" + "downloadUrl=" + downloadUrl + ", contentLength=" + contentLength + ", contentType=" + contentType + ", fileName=" + fileName + ", tokenInfo=" + tokenInfo + ", PUT_URL=" + PUT_URL + ", uploadedBytes=" + uploadedBytes + ", chunkSize=" + chunkSize + '}';
    }

    public Status getStatus()
    {
	if (errorMessage != null)
	    return new ErrorStatus(errorMessage);

	double percentageComplete = (double) uploadedBytes / (double) contentLength * 100D;

	SuccessStatus successStatus = new SuccessStatus();

	successStatus.setCompletePercentage(percentageComplete);
	successStatus.setProcessingStatus(status);
	return successStatus;
    }

    private void logHttpError(InputStream inputStream) throws IOException
    {
	BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

	String line;
	StringBuilder sb = new StringBuilder();

	while ((line = br.readLine()) != null)
	    sb.append(line);

	LOGGER.log(Level.SEVERE, sb.toString());

    }
}
