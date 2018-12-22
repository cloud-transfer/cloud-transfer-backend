package com.github.cloud_transfer.cloud_transfer.transfer_service;

import com.github.cloud_transfer.cloud_transfer.downloader.URLDownloader;
import com.github.cloud_transfer.cloud_transfer.model.token.GoogleDriveToken;
import com.github.cloud_transfer.cloud_transfer.uploader.GoogleDriveUploader;
import com.github.cloud_transfer.cloud_transfer.uploader.Uploader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.net.URI;

public class UrlToGoogleDriveTransferService extends TransferService {

    @Value("${downloadDirectory}")
    private static String downloadDirectory;
    private final MultiValueMap<String, String> headers;
    private GoogleDriveToken token;
    private String fileName;
    private URI downloadUri;

    public UrlToGoogleDriveTransferService(URI downloadUri, GoogleDriveToken token, MultiValueMap<String, String> headers, String fileName) {
        this(downloadUri, token, headers);
        this.fileName = fileName;
    }

    public UrlToGoogleDriveTransferService(URI downloadUri, GoogleDriveToken token, MultiValueMap<String, String> headers) {
        this.token = token;
        this.downloadUri = downloadUri;
        this.headers = headers;
    }


    @Override
    protected @NotNull Uploader getUploader() {
        return new GoogleDriveUploader(downloadInformation.getFile(), token, downloadInformation.getContentType());
    }

    @Override
    protected URLDownloader getDownloader() {
        if (fileName == null)
            return new URLDownloader(downloadUri, headers, new File(downloadDirectory));
        else
            return new URLDownloader(downloadUri, headers, new File(downloadDirectory), fileName);
    }
}
