package com.github.dhaval_mehta.savetogoogledrive.uploader.drive;

import static com.github.dhaval_mehta.savetogoogledrive.utility.HttpUtilities.USER_AGENT;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.validation.constraints.NotNull;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;

import com.github.dhaval_mehta.savetogoogledrive.exception.ApiException;
import com.github.dhaval_mehta.savetogoogledrive.model.DownloadFileInfo;
import com.github.dhaval_mehta.savetogoogledrive.model.UploadInformation;
import com.github.dhaval_mehta.savetogoogledrive.model.UploadStatus;
import com.github.dhaval_mehta.savetogoogledrive.model.User;
import com.github.dhaval_mehta.savetogoogledrive.uploader.Uploader;
import com.google.gson.JsonObject;

abstract class DriveUploader implements Uploader {
	private static final URL CREATE_FILE_URL;

	static {
		try {
			CREATE_FILE_URL = new URL("https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable");
		} catch (MalformedURLException ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	final int chunkSize;
	protected User user;
	DownloadFileInfo downloadFileInfo;
	UploadInformation uploadInformation;
	private URL createdFileUrl;

	DriveUploader(DownloadFileInfo downloadFileInfo, User user) {
		this();
		this.user = user;
		this.downloadFileInfo = downloadFileInfo;
		uploadInformation.setFileName(downloadFileInfo.getFileName());
		uploadInformation.setUrl(downloadFileInfo.getUploadUrl().toString());
		uploadInformation.setTotalSize(downloadFileInfo.getContentLength());

		try {
			obtainUploadUrl();
		} catch (Exception e) {
			uploadInformation.setUploadStatus(UploadStatus.failed);
			uploadInformation.setErrorMessage(e.getMessage());
		}
	}

	private DriveUploader() {
		chunkSize = 1024 * 1024; // 1 MB
		uploadInformation = new UploadInformation();
		uploadInformation.setUploadStatus(UploadStatus.waiting);

	}

	/**
	 * It will upload bytes in range [start,end] to Google drive.
	 *
	 * @param start
	 *            starting byte of range
	 * @param end
	 *            ending byte of range
	 */
	void uploadPartially(@NotNull byte[] buffer, long start, long end) {
		String contentRange = "bytes " + start + "-" + end + "/" + downloadFileInfo.getContentLength();

		int statusCode;
		try {
			HttpURLConnection uploadConnection = (HttpURLConnection) createdFileUrl.openConnection();
			uploadConnection.setDoOutput(true);
			uploadConnection.setRequestProperty("User-Agent", USER_AGENT);
			uploadConnection.setRequestProperty("Content-Range", contentRange);
			IOUtils.copy(new ByteArrayInputStream(buffer), uploadConnection.getOutputStream());
			uploadConnection.connect();
			statusCode = uploadConnection.getResponseCode();
		} catch (IOException e) {
			throw new RuntimeException("Error While uploading file.", e);
		}

		// In case of successful upload, status code will be 3** or 2**
		if (statusCode < 400)
			uploadInformation.setUploadedSize(end + 1);
		else if (statusCode == 403) {
			throw new RuntimeException(
					"Google didn't allow us to create file. Your drive might not have enough space.");
		}
	}

	private void obtainUploadUrl() throws IOException {
		user.refreshTokenIfNecessary();
		HttpURLConnection connection = (HttpURLConnection) CREATE_FILE_URL.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("User-Agent", USER_AGENT);
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", downloadFileInfo.getFileName());
		String postBody = jsonObject.toString();

		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Authorization",
				user.getToken().getTokenType() + " " + user.getToken().getAccessToken());
		connection.setRequestProperty("X-Upload-Content-Type", downloadFileInfo.getContentType());
		connection.setRequestProperty("X-Upload-Content-Length", String.valueOf(downloadFileInfo.getContentLength()));

		try (PrintStream writer = new PrintStream(connection.getOutputStream())) {
			writer.print(postBody);
		}

		connection.connect();

		int statusCode = connection.getResponseCode();

		if (statusCode == HttpStatus.OK.value())
			createdFileUrl = new URL(connection.getHeaderField("Location"));
		else if (statusCode == HttpStatus.UNAUTHORIZED.value())
			throw new ApiException(HttpStatus.UNAUTHORIZED, "Your session is expired");
		else
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot create new file in google dirve.");

	}

	@Override
	public UploadInformation getUploadInformation() {
		return uploadInformation;
	}
}
