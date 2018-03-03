package com.github.dhaval_mehta.savetogoogledrive.model;

import java.net.URL;

public class DownloadFileInfo {
    
    private URL uploadUrl;
    private String fileName;
    private String contentType;
    private long contentLength;
    private boolean resumeSupported;

    public URL getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(URL uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public boolean isResumeSupported() {
        return resumeSupported;
    }

    public void setResumeSupported(boolean resumeSupported) {
        this.resumeSupported = resumeSupported;
    }
}
