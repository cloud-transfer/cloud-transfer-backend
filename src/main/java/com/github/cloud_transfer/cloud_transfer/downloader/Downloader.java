package com.github.cloud_transfer.cloud_transfer.downloader;

import com.github.cloud_transfer.cloud_transfer.model.DownloadInformation;
import com.github.cloud_transfer.cloud_transfer.model.SpeedMeter;

import java.io.IOException;

public interface Downloader extends SpeedMeter {

    DownloadInformation download() throws IOException, InterruptedException;

}
