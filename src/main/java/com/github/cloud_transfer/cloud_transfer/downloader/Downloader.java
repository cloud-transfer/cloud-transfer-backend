package com.github.cloud_transfer.cloud_transfer.downloader;

import com.github.cloud_transfer.cloud_transfer.model.DownloadInformation;

import java.io.IOException;

public interface Downloader {

    DownloadInformation download() throws IOException, InterruptedException;
    double getCurrentSpeed();

}
