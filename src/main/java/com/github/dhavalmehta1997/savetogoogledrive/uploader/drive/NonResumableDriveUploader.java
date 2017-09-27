package com.github.dhavalmehta1997.savetogoogledrive.uploader.drive;

import com.github.dhavalmehta1997.savetogoogledrive.model.DownloadFileInfo;
import com.github.dhavalmehta1997.savetogoogledrive.model.User;

public class NonResumableDriveUploader extends DriveUploader {
    public NonResumableDriveUploader(DownloadFileInfo downloadFileInfo, User user) {
        super(downloadFileInfo, user);
    }

    @Override
    public void upload() {
        throw new UnsupportedOperationException("Not implemented yet.........");
    }
}
