package com.github.dhaval_mehta.savetogoogledrive.uploader.drive;

import com.github.dhaval_mehta.savetogoogledrive.exception.ApiException;
import com.github.dhaval_mehta.savetogoogledrive.model.DownloadFileInfo;
import com.github.dhaval_mehta.savetogoogledrive.model.UploadStatus;
import com.github.dhaval_mehta.savetogoogledrive.model.User;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.math.BigInteger;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NonResumableDriveUploader extends DriveUploader {
    private static final SecureRandom random = new SecureRandom();
    private File downloadedFile;

    NonResumableDriveUploader(DownloadFileInfo downloadFileInfo, User user) {
        super(downloadFileInfo, user);
    }

    private static synchronized String generateRandomString() {
        return new BigInteger(130, random).toString(32) + new BigInteger(String.valueOf(System.currentTimeMillis())).toString(32);
    }

    @Override
    public void upload() {
        try {
            downloadFile();
        } catch (IOException e) {
            Logger.getLogger(NonResumableDriveUploader.class.getName()).log(Level.SEVERE, null, e);
            uploadInformation.setUploadStatus(UploadStatus.failed);
            uploadInformation.setErrorMessage("File not found.");
            return;
        }
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(downloadedFile))) {
            byte buffer[] = new byte[chunkSize];
            int n;

            uploadInformation.setUploadStatus(UploadStatus.uploading);

            while ((n = bufferedInputStream.read(buffer)) > 0) {
                long start = uploadInformation.getUploadedSize();
                long startTime = System.currentTimeMillis();
                uploadPartially(buffer, start, start + n - 1);
                long endTime = System.currentTimeMillis();
                double timeElapsed = (endTime - startTime) / 1000.0;
                uploadInformation.setSpeed(n / timeElapsed);
                uploadInformation.setUploadedSize(start + n);
            }

            uploadInformation.setUploadStatus(UploadStatus.completed);
        } catch (IOException e) {
            uploadInformation.setUploadStatus(UploadStatus.failed);
            uploadInformation.setErrorMessage(e.getMessage());
            e.printStackTrace();
        }
        downloadedFile.delete();
        downloadedFile.getParentFile().delete();
    }

    private void downloadFile() throws IOException {
        String random = generateRandomString();
        File downloadDirectory = new File(System.getenv("TEMP_STROAGE"), random);
        boolean created = downloadDirectory.mkdir();

        if (!created) {
            uploadInformation.setUploadStatus(UploadStatus.failed);
            uploadInformation.setErrorMessage("Can not create download directory.");
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Can not create download directory.");
        }

        downloadedFile = new File(downloadDirectory, downloadFileInfo.getFileName());
        ReadableByteChannel rbc = Channels.newChannel(downloadFileInfo.getUploadUrl().openStream());
        FileOutputStream fos = new FileOutputStream(downloadedFile);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
    }
}
