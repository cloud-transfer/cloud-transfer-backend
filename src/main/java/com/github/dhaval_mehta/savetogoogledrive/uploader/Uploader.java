package com.github.dhaval_mehta.savetogoogledrive.uploader;

import com.github.dhaval_mehta.savetogoogledrive.model.UploadInformation;

public interface Uploader {
    void upload();

    UploadInformation getUploadInformation();
}
