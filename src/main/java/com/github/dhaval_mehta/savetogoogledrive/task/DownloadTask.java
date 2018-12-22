package com.github.dhaval_mehta.savetogoogledrive.task;

import com.github.dhaval_mehta.savetogoogledrive.util.ProgressBufferedInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class DownloadTask implements Task {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();
    private static final Logger logger = LoggerFactory.getLogger(DownloadTask.class);

    private URI uri;
    private File file;
    private Map<String, String> headers;
    private long contentLength;
    private String mimeType;
    private ProgressBufferedInputStream responseStream;

    public DownloadTask(URI uri, File downloadFile) {
        this.uri = uri;
        file = downloadFile;
    }

    public DownloadTask(URI uri, File directory, String fileName) throws IOException {
        this(uri, directory, fileName, new HashMap<>());
    }

    public DownloadTask(URI uri, File directory, String fileName, Map<String, String> headers) throws IOException {
        this.uri = uri;
        this.file = new File(directory, fileName);
        this.headers = headers;
        if (!file.exists()) {
            boolean created = file.createNewFile();
            if (!created)
                throw new RuntimeException("Failed to create file: " + file.getAbsolutePath());
        }
    }

    public File download() {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file), 102400)) {
            var response = sendHttpRequest();
            setContentType(response);
            responseStream = new ProgressBufferedInputStream(response.body());
            responseStream.transferTo(outputStream);
            responseStream.close();
        } catch (IOException | InterruptedException e) {
            logger.error(null, e);
        }
        return file;
    }

    private HttpResponse<InputStream> sendHttpRequest() throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(uri).GET();
        headers.forEach(requestBuilder::header);
        return httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofInputStream());
    }

    private void setContentType(HttpResponse<InputStream> response) {
        mimeType = response.headers()
                .firstValue("Content-Type")
                .orElse("application/octet-stream");
    }

    public String getMimeType() {
        return mimeType;
    }

    public long getContentLength() {
        return contentLength;
    }

    private void setContentLength(HttpResponse<InputStream> response) {
        contentLength = response.headers()
                .firstValue("Content-Length")
                .map(Long::parseLong)
                .orElse(Long.MAX_VALUE);
    }

    @Override
    public double getCurrentSpeed() {
        if (responseStream == null)
            return 0;

        return responseStream.currentSpeed();
    }
}
