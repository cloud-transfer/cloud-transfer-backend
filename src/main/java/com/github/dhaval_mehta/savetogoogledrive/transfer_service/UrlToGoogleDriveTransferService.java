package com.github.dhaval_mehta.savetogoogledrive.transfer_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dhaval_mehta.savetogoogledrive.exception.ApiException;
import com.github.dhaval_mehta.savetogoogledrive.model.token.GoogleDriveToken;
import com.github.dhaval_mehta.savetogoogledrive.task.DownloadTask;
import com.github.dhaval_mehta.savetogoogledrive.task.GoogleDriveUploadTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UrlToGoogleDriveTransferService implements TransferService {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();
    private static final URI FILE_CREATE_URI = URI.create("https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable");
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(UrlToGoogleDriveTransferService.class);

    private GoogleDriveUploadTask uploadTask;
    private DownloadTask downloadTask;
    private GoogleDriveToken token;
    private String fileName;
    private File downloadFile;
    private String mimeType;
    private long contentLength;

    public UrlToGoogleDriveTransferService(GoogleDriveToken token, URI downloadUri, String fileName)
            throws IOException {
        this.token = token;
        this.fileName = fileName;
        downloadFile = File.createTempFile(getClass().getName(), getClass().getEnclosingMethod().getName());
        downloadTask = new DownloadTask(downloadUri, downloadFile);
    }

    @Override
    public void transfer() {
        try {
            downloadTask.download();
            mimeType = downloadTask.getMimeType();
            contentLength = downloadTask.getContentLength();
            URI createdFile = createFileInGoogleDrive();
            uploadTask = new GoogleDriveUploadTask(createdFile, downloadFile);
            uploadTask.upload();
        } catch (IOException | InterruptedException e) {
            logger.error(null, e);
        }
    }

    private URI createFileInGoogleDrive() throws IOException, InterruptedException {
        ObjectNode root = mapper.createObjectNode();
        root.put("name", fileName);
        HttpRequest request = HttpRequest.newBuilder(FILE_CREATE_URI)
                .POST(HttpRequest.BodyPublishers.ofString(root.asText()))
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", token.getAccessToken())
                .setHeader("X-Upload-Content-Type", mimeType)
                .setHeader("X-Upload-Content-Length", String.valueOf(contentLength))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

        if (response.statusCode() != HttpStatus.OK.value())
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot create new file in google dirve.");

        return URI.create(response.headers().firstValue("Location").get());
    }
}
