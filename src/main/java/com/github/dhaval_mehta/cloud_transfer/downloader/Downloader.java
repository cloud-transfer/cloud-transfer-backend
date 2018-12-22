package com.github.dhaval_mehta.cloud_transfer.downloader;

import com.github.dhaval_mehta.cloud_transfer.model.DownloadInformation;

import java.io.IOException;

public interface Downloader {

    DownloadInformation download() throws IOException, InterruptedException;
    double getCurrentSpeed();

}
