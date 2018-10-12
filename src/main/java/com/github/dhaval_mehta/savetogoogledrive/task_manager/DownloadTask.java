package com.github.dhaval_mehta.savetogoogledrive.task_manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DownloadTask implements Runnable {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();
    private static final Logger logger = LoggerFactory.getLogger(DownloadTask.class);

    private URI uri;
    private File file;
    private Map<String, String> headers;
    private Timer timer = new Timer();
    private long contentLength;
    private long downloadSize;
    private long startTime;
    private DownloadSpeedMeter speedMeter = new DownloadSpeedMeter();
    private String mimeType;

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

    public double averageSpeed() {
        return ((double) downloadSize * 1000) / (System.currentTimeMillis() - startTime);
    }

    public double currentSpeed() {
        return speedMeter.currentSpeed();
    }

    public long getContentLength() {
        return contentLength;
    }

    private void setContentLength(HttpResponse<InputStream> response) {
        contentLength = response.headers().firstValue("Content-Length").map(Long::parseLong).orElse(Long.MAX_VALUE);
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        timer.schedule(speedMeter, 0, 1000);
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file), 10240000)) {
            var response = sendHttpRequest();
            var responseBody = new BufferedInputStream(response.body());
            setContentLength(response);
            setContentType(response);
            while (true) {
                boolean completed = writeIntoFile(responseBody, outputStream);
                if (completed)
                    break;
            }
        } catch (IOException | InterruptedException e) {
            logger.error(null, e);
        }
        timer.cancel();
    }

    private boolean writeIntoFile(BufferedInputStream responseBody, BufferedOutputStream outputStream)
            throws IOException {
        byte[] buffer = responseBody.readNBytes(102400);
        if (buffer.length == 0)
            return true;
        outputStream.write(buffer);
        downloadSize += buffer.length;
        return false;
    }

    private HttpResponse<InputStream> sendHttpRequest() throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(uri).GET();
        headers.forEach(requestBuilder::header);
        return httpClient.send(
                requestBuilder.build(), HttpResponse.BodyHandlers.ofInputStream());

    }

    private void setContentType(HttpResponse<InputStream> response) {
        mimeType = response.headers().firstValue("Content-Type").orElse("application/octet-stream");
    }

    public String getMimeType() {
        return mimeType;
    }

    private class DownloadSpeedMeter extends TimerTask {

        private double prevDownloadedSize;
        private long prevExecutionTime;
        private double currentSpeed;

        @Override
        public void run() {
            if (downloadSize - prevDownloadedSize == 0)
                return;
            long currentTime = System.currentTimeMillis();
            currentSpeed = (downloadSize - prevDownloadedSize) / ((double) (currentTime - prevExecutionTime) / 1000);
            prevExecutionTime = currentTime;
            prevDownloadedSize = downloadSize;
        }

        double currentSpeed() {
            return currentSpeed;
        }
    }
}
