package com.github.dhaval_mehta.savetogoogledrive.transfer_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dhaval_mehta.savetogoogledrive.task.DownloadManager;
import com.github.dhaval_mehta.savetogoogledrive.task.DownloadTask;
import com.github.dhaval_mehta.savetogoogledrive.exception.ApiException;
import com.github.dhaval_mehta.savetogoogledrive.model.token.GoogleDriveToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

import static com.github.dhaval_mehta.savetogoogledrive.util.HttpUtilities.USER_AGENT;

public class UrlToGoogleDriveTransferService implements TransferService {
    private static final HttpClient httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
    private static final URI FILE_CREATE_URI = URI.create("https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable");
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final DownloadManager downloadManager = DownloadManager.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(UrlToGoogleDriveTransferService.class);

    private DownloadTask downloadTask;
    private URI createdFileUri;
    private GoogleDriveToken token;
    private String fileName;
    private URI downloadUri;
    private File downloadFile;
    private int chunkSize = 8192;
    private String mimeType;

    public UrlToGoogleDriveTransferService(GoogleDriveToken token, URI downloadUri, String fileName) {
        this.token = token;
        this.fileName = fileName;
        this.downloadUri = downloadUri;
    }

    @Override
    public void transfer() {
        try {
            downloadFile = File.createTempFile(getClass().getName(), getClass().getEnclosingMethod().getName());
            downloadTask = new DownloadTask(downloadUri, downloadFile);
            mimeType = downloadTask.getMimeType();
            downloadManager.addTask(downloadTask).get();
            uploadFile();
        } catch (IOException | InterruptedException | ExecutionException e) {
            logger.error(null, e);
        }
    }

    private void uploadFile() throws IOException, InterruptedException {
        long contentLength = downloadFile.length();
        createFileInGoogleDrive(contentLength);
        var inputStream = new BufferedInputStream(new FileInputStream(downloadFile));
        long completed = 0;
        while (true) {
            byte[] buffer = inputStream.readNBytes(chunkSize);
            if (buffer.length == 0)
                break;
            uploadPartially(buffer, completed, completed + buffer.length - 1, contentLength);
        }
    }

    private void createFileInGoogleDrive(long contentLength) throws IOException, InterruptedException {
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

        createdFileUri = URI.create(response.headers().firstValue("Location").get());
    }

    private void uploadPartially(@NotNull byte[] buffer, long start, long end, long contentLength)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder(createdFileUri)
                .PUT(HttpRequest.BodyPublishers.ofByteArray(buffer))
                .setHeader("User-Agent", USER_AGENT)
                .setHeader("Content-Range", "bytes " + start + "-" + end + "/" + contentLength)
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException(
                    "Google didn't allow us to create file. Your drive might not have enough space.");
        }
    }
}
