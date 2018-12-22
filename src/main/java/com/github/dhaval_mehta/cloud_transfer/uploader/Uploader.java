package com.github.dhaval_mehta.cloud_transfer.uploader;

import java.io.IOException;

public interface Uploader {

    void upload() throws IOException, InterruptedException;
    double getCurrentSpeed();

}
