package com.github.cloud_transfer.cloud_transfer_backend.downloader;

import com.github.cloud_transfer.cloud_transfer_backend.model.DownloadInformation;
import com.github.cloud_transfer.cloud_transfer_backend.model.SpeedMeter;

import java.io.IOException;

public interface Downloader extends SpeedMeter {

    DownloadInformation download() throws IOException, InterruptedException;

}
