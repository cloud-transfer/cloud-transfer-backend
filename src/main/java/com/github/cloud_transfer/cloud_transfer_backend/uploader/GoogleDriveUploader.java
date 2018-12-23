package com.github.cloud_transfer.cloud_transfer_backend.uploader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.cloud_transfer.cloud_transfer_backend.model.exception.ApiException;
import com.github.cloud_transfer.cloud_transfer_backend.model.token.GoogleDriveToken;
import com.github.cloud_transfer.cloud_transfer_backend.util.ProgressBufferedInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GoogleDriveUploader implements Uploader {

    private static final int chunkSize = 8192;
    private static final URI FILE_CREATE_URI = URI.create("https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable");
    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private static HttpClient httpClient;
    private final File file;
    private final GoogleDriveToken token;
    private final long contentLength;
    private final String mimeType;
    private URI googleDriveFileURI;
    private ProgressBufferedInputStream fileStream;

    public GoogleDriveUploader(File file, GoogleDriveToken token, String mimeType) {
        this.file = file;
        this.contentLength = file.length();
        this.token = token;
        this.mimeType = mimeType;
    }

    private URI createFileInGoogleDrive() throws IOException, InterruptedException {
        ObjectNode root = mapper.createObjectNode();
        root.put("name", file.getName());
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

    public void upload() throws IOException, InterruptedException {
        googleDriveFileURI = createFileInGoogleDrive();
        fileStream = new ProgressBufferedInputStream(new FileInputStream(file));
        long completed = 0;
        while (true) {
            byte[] buffer = fileStream.readNBytes(chunkSize);
            if (buffer.length == 0)
                break;
            uploadPartially(buffer, completed, completed + buffer.length - 1, contentLength);
        }
    }

    private void uploadPartially(byte[] buffer, long start, long end, long contentLength)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder(googleDriveFileURI)
                .PUT(HttpRequest.BodyPublishers.ofByteArray(buffer))
                .setHeader("Content-Range", "bytes " + start + "-" + end + "/" + contentLength)
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Failed to create file.");
        }
    }

    @Override
    public double getCurrentSpeed() {
        if (fileStream == null)
            return 0;

        return fileStream.currentSpeed();
    }
}
