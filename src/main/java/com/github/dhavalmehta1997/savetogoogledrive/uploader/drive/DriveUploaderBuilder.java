package com.github.dhavalmehta1997.savetogoogledrive.uploader.drive;

import com.github.dhavalmehta1997.savetogoogledrive.model.DownloadFileInfo;
import com.github.dhavalmehta1997.savetogoogledrive.model.User;
import com.github.dhavalmehta1997.savetogoogledrive.utility.HttpUtilities;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.dhavalmehta1997.savetogoogledrive.utility.HttpUtilities.USER_AGENT;

public class DriveUploaderBuilder {

    private static final Pattern CONTENT_DISPOSITION_PATTERN =
            Pattern.compile("attachment;\\s*filename\\s*=\\s*\"([^\"]*)\"");

    private User user;
    private DownloadFileInfo downloadFileInfo;

    public DriveUploaderBuilder() {
        downloadFileInfo = new DownloadFileInfo();
    }

    public DriveUploaderBuilder setFileName(String fileName) {
        downloadFileInfo.setFileName(fileName);
        return this;
    }

    public DriveUploaderBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public DriveUploaderBuilder setUploadUrl(URL uploadUrl) {
        downloadFileInfo.setUploadUrl(uploadUrl);
        return this;
    }

    public DriveUploader build() throws IOException {
        assert user != null;
        assert downloadFileInfo.getUploadUrl() != null;
        fetchFileInformation();

        if (downloadFileInfo.isResumeSupported())
            return new ResumableDriveUploader(downloadFileInfo, user);
        else
            return new NonResumableDriveUploader(downloadFileInfo, user);
    }

    private void fetchFileInformation() throws IOException {
        assert downloadFileInfo.getUploadUrl() != null;
        HttpURLConnection connection = (HttpURLConnection) downloadFileInfo.getUploadUrl().openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestMethod("HEAD");
        int statusCode = connection.getResponseCode();

        if (!HttpUtilities.success(statusCode))
            return;

        downloadFileInfo.setContentLength(connection.getContentLengthLong());
        downloadFileInfo.setContentType(connection.getContentType());

        String acceptRangeHeader = connection.getHeaderField("Accept-Ranges");

        if (acceptRangeHeader != null)
            downloadFileInfo.setResumeSupported(acceptRangeHeader.startsWith("bytes"));
        else
            downloadFileInfo.setResumeSupported(checkResumeSupportUsingGetMethod());

        if (downloadFileInfo.getFileName() == null) {
            String contentDisposition = connection.getHeaderField("Content-Disposition");

            if (contentDisposition == null)
                downloadFileInfo.setFileName(FilenameUtils.getName(downloadFileInfo.getUploadUrl().getPath()));
            else {
                try {
                    Matcher matcher = CONTENT_DISPOSITION_PATTERN.matcher(contentDisposition);
                    if (matcher.find()) {
                        downloadFileInfo.setFileName(matcher.group(1));
                    }
                } catch (IllegalStateException ex) {
                    downloadFileInfo.setFileName(FilenameUtils.getName(downloadFileInfo.getUploadUrl().getPath()));
                }
            }
        }
    }

    private boolean checkResumeSupportUsingGetMethod() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) downloadFileInfo.getUploadUrl().openConnection();
        connection.setRequestMethod("GET");
        String rangeHeaderValue = "bytes=" + 0 + "-" + 0;
        connection.setRequestProperty("Range", rangeHeaderValue);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        return HttpStatus.PARTIAL_CONTENT.value() == connection.getResponseCode();
    }
}
