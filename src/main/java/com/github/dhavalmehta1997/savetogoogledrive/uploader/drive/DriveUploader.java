package com.github.dhavalmehta1997.savetogoogledrive.uploader.drive;

import com.github.dhavalmehta1997.savetogoogledrive.exception.ApiException;
import com.github.dhavalmehta1997.savetogoogledrive.model.DownloadFileInfo;
import com.github.dhavalmehta1997.savetogoogledrive.model.UploadInformation;
import com.github.dhavalmehta1997.savetogoogledrive.model.UploadStatus;
import com.github.dhavalmehta1997.savetogoogledrive.model.User;
import com.github.dhavalmehta1997.savetogoogledrive.uploader.Uploader;
import com.github.dhavalmehta1997.savetogoogledrive.utility.HttpUtilities;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.github.dhavalmehta1997.savetogoogledrive.utility.HttpUtilities.USER_AGENT;
import static com.github.dhavalmehta1997.savetogoogledrive.utility.IOUtilities.inputStreamToString;

abstract class DriveUploader implements Uploader {
    private static final URL POST_URL;
    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(DriveUploader.class.getName());
        try {
            POST_URL = new URL("https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable");
        } catch (MalformedURLException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    final int chunkSize;
    protected User user;
    DownloadFileInfo downloadFileInfo;
    UploadInformation uploadInformation;
    private URL PUT_URL;

    DriveUploader(DownloadFileInfo downloadFileInfo, User user) {
        this();
        this.downloadFileInfo = downloadFileInfo;
        this.user = user;
        uploadInformation.setFileName(downloadFileInfo.getFileName());
        uploadInformation.setUrl(downloadFileInfo.getUploadUrl().toString());
        uploadInformation.setTotalSize(downloadFileInfo.getContentLength());
        try {
            obtainUploadUrl();
        } catch (Exception e) {
            uploadInformation.setUploadStatus(UploadStatus.failed);
            uploadInformation.setErrorMessage(e.getMessage());
            uploadInformation.setError(e);
            LOGGER.log(Level.SEVERE, "Exception while initializing" + DriveUploader.class.getName(), e);
        }
    }

    private DriveUploader() {
        chunkSize = 1024 * 1024; // 1 MB
        uploadInformation = new UploadInformation();
        uploadInformation.setUploadStatus(UploadStatus.waiting);
    }

    /**
     * It will upload bytes in range [start,end] to Google drive.
     *
     * @param start starting byte of range
     * @param end   ending byte of range
     * @throws IOException
     */
    void uploadPartially(@NotNull byte[] buffer, long start, long end) throws IOException {
        HttpURLConnection uploadConnection = (HttpURLConnection) PUT_URL.openConnection();
        uploadConnection.setDoOutput(true);
        uploadConnection.setRequestProperty("User-Agent", USER_AGENT);
        String contentRange = "bytes " + start + "-" + end + "/" + downloadFileInfo.getContentLength();
        uploadConnection.setRequestProperty("Content-Range", contentRange);
        OutputStream out = uploadConnection.getOutputStream();
        out.write(buffer, 0, (int) (end - start + 1));
        out.close();
        int statusCode = uploadConnection.getResponseCode();
        // In case of successful upload, status code will be 3** or 2**
        if (statusCode < 400)
            uploadInformation.setUploadedSize(end + 1);
        else {
            String description = inputStreamToString(uploadConnection.getInputStream());
            uploadInformation.setError(new ApiException(HttpStatus.valueOf(statusCode), description, null));
        }
    }

    private void obtainUploadUrl() throws IOException {
        user.refreshTokenIfNecessary();
        HttpURLConnection connection = (HttpURLConnection) POST_URL.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", downloadFileInfo.getFileName());
        String postBody = jsonObject.toString();

        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", user.getToken().getTokenType() + " " + user.getToken().getAccessToken());
        connection.setRequestProperty("X-Upload-Content-Type", downloadFileInfo.getContentType());
        connection.setRequestProperty("X-Upload-Content-Length", String.valueOf(downloadFileInfo.getContentLength()));

        try (PrintStream writer = new PrintStream(connection.getOutputStream())) {
            writer.print(postBody);
        }

        connection.connect();

        int statusCode = connection.getResponseCode();

        if (HttpUtilities.success(statusCode))
            PUT_URL = new URL(connection.getHeaderField("Location"));
        else {
            String description = inputStreamToString(connection.getInputStream());
            uploadInformation.setError(new ApiException(HttpStatus.valueOf(statusCode), description, null));
        }
    }

    @Override
    public UploadInformation getUploadInformation() {
        return uploadInformation;
    }
}
