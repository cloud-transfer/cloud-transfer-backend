package com.github.cloud_transfer.cloud_transfer_backend.model;

import java.io.File;

public class DownloadInformation {

    private String contentType;
    private File file;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
