package com.github.cloud_transfer.cloud_transfer.downloader;

import com.github.cloud_transfer.cloud_transfer.model.DownloadInformation;
import com.github.cloud_transfer.cloud_transfer.util.ProgressBufferedInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class URLDownloader implements Downloader {

    private static final Logger logger = LoggerFactory.getLogger(URLDownloader.class);
    private final DownloadInformation downloadInformation;
    @Autowired
    HttpClient httpClient;
    private String filename;
    private URI uri;
    private File directory;
    private MultiValueMap<String, String> headers;
    private ProgressBufferedInputStream responseStream;

    public URLDownloader(URI uri, MultiValueMap<String, String> headers, File directory) {
        this.uri = uri;
        this.headers = headers;
        this.directory = directory;
        downloadInformation = new DownloadInformation();
    }

    public URLDownloader(URI uri, MultiValueMap<String, String> headers, File directory, String filename) {
        this(uri, headers, directory);
        this.filename = filename;
    }

    public DownloadInformation download() throws IOException, InterruptedException {
        var response = sendHttpRequest();
        setFileNameIfRequired(response);
        File file = new File(directory, filename);
        downloadInformation.setContentType(findContentType(response));
        downloadInformation.setFile(file);
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file), 102400)) {
            responseStream = new ProgressBufferedInputStream(response.body());
            responseStream.transferTo(outputStream);
            responseStream.close();
        }
        return downloadInformation;
    }

    private void setFileNameIfRequired(HttpResponse response) {
        if (filename != null)
            return;

    }

    private HttpResponse<InputStream> sendHttpRequest() throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(uri).GET();
        if (headers != null)
            headers.forEach((key, values) -> values.forEach(value -> requestBuilder.header(key, value)));
        return httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofInputStream());
    }

    private String findContentType(@NotNull HttpResponse<InputStream> response) {
        return response.headers()
                .firstValue("Content-Type")
                .orElse("application/octet-stream");
    }

    @Override
    public double getCurrentSpeed() {
        if (responseStream == null)
            return 0;

        return responseStream.currentSpeed();
    }
}
