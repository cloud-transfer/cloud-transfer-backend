package com.github.dhavalmehta1997.savetogoogledrive.uploader;

import com.github.dhavalmehta1997.savetogoogledrive.model.UploadInformation;

public interface Uploader {
    void upload();

    UploadInformation getUploadInformation();
}
