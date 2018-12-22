package com.github.dhaval_mehta.savetogoogledrive.task;

import com.github.dhaval_mehta.savetogoogledrive.util.ProgressBufferedInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GoogleDriveUploadTask implements Task {

    private static final int chunkSize = 8192;
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();

    private final File uploadFile;
    private final URI googleDriveFileURI;
    private final long contentLength;
    private ProgressBufferedInputStream fileStream;

    public GoogleDriveUploadTask(URI googleDriveFileURI, File uploadFile) {
        this.googleDriveFileURI = googleDriveFileURI;
        this.uploadFile = uploadFile;
        this.contentLength = uploadFile.length();
    }

    public void upload() throws IOException, InterruptedException {
        fileStream = new ProgressBufferedInputStream(new FileInputStream(uploadFile));
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
