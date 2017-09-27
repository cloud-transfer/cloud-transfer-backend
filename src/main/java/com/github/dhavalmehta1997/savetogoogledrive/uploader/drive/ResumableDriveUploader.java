package com.github.dhavalmehta1997.savetogoogledrive.uploader.drive;

import com.github.dhavalmehta1997.savetogoogledrive.model.DownloadFileInfo;
import com.github.dhavalmehta1997.savetogoogledrive.model.UploadStatus;
import com.github.dhavalmehta1997.savetogoogledrive.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.github.dhavalmehta1997.savetogoogledrive.utility.HttpUtilities.USER_AGENT;

public class ResumableDriveUploader extends DriveUploader {

    public ResumableDriveUploader(DownloadFileInfo downloadFileInfo, User user) {
        super(downloadFileInfo, user);
    }

    private byte[] downloadPartially(long start, long end) throws IOException {
        HttpURLConnection downloadConnection = (HttpURLConnection) downloadFileInfo.getUploadUrl().openConnection();
        downloadConnection.setRequestMethod("GET");
        String rangeHeaderValue = "bytes=" + start + "-" + end;
        downloadConnection.setRequestProperty("Range", rangeHeaderValue);
        downloadConnection.setRequestProperty("User-Agent", USER_AGENT);
        InputStream in = downloadConnection.getInputStream();
        byte[] buffer = new byte[(int) (end - start + 1)];
        in.read(buffer);
        in.close();
        return buffer;
    }


    /**
     * This function will upload file to Google drive using resumable upload
     * method.
     */
    @Override
    public void upload() {
        try {
            uploadInformation.setUploadStatus(UploadStatus.uploading);

            while (uploadInformation.getUploadedSize() < downloadFileInfo.getContentLength()) {
                long start = uploadInformation.getUploadedSize();
                long end = uploadInformation.getUploadedSize() + chunkSize - 1;

                if (end >= downloadFileInfo.getContentLength())
                    end = downloadFileInfo.getContentLength() - 1;

                long startTime = System.currentTimeMillis();
                byte[] buffer = downloadPartially(start, end);
                uploadPartially(buffer, start, end);
                long endTime = System.currentTimeMillis();

                uploadInformation.setUploadedSize(end + 1);
                double dataFetched = end - start + 1;
                double timeElapsed = (endTime - startTime) / 1000.0;
                uploadInformation.setSpeed(dataFetched / timeElapsed);
            }

            uploadInformation.setUploadStatus(UploadStatus.completed);

        } catch (Throwable ex) {
            uploadInformation.setError(ex);
            Logger.getLogger(ResumableDriveUploader.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
