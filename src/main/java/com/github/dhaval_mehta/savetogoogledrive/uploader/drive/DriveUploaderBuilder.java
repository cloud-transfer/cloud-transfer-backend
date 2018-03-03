package com.github.dhaval_mehta.savetogoogledrive.uploader.drive;

import static com.github.dhaval_mehta.savetogoogledrive.utility.HttpUtilities.USER_AGENT;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;

import com.github.dhaval_mehta.savetogoogledrive.exception.ApiException;
import com.github.dhaval_mehta.savetogoogledrive.model.DownloadFileInfo;
import com.github.dhaval_mehta.savetogoogledrive.model.User;
import com.github.dhaval_mehta.savetogoogledrive.utility.HttpUtilities;

public class DriveUploaderBuilder {

	private static final Pattern CONTENT_DISPOSITION_PATTERN = Pattern
			.compile("(?i)filename[^;\\n=]*=(?:utf-8'')?[\"]?([^;\"]*)");

	private User user;
	private DownloadFileInfo downloadFileInfo;

	public DriveUploaderBuilder() {
		downloadFileInfo = new DownloadFileInfo();
	}

	public DriveUploaderBuilder setFileName(String fileName) {
		downloadFileInfo.setFileName(fileName);
		return this;
	}

	public DriveUploaderBuilder setUser(User user) {
		this.user = user;
		return this;
	}

	public DriveUploaderBuilder setUploadUrl(URL uploadUrl) {
		downloadFileInfo.setUploadUrl(uploadUrl);
		return this;
	}

	public DriveUploader build() throws IOException {
		assert user != null;
		assert downloadFileInfo.getUploadUrl() != null;
		fetchFileInformation();

		if (downloadFileInfo.isResumeSupported())
			return new ResumableDriveUploader(downloadFileInfo, user);
		else
			return new NonResumableDriveUploader(downloadFileInfo, user);
	}

	private void fetchFileInformation() throws IOException {
		assert downloadFileInfo.getUploadUrl() != null;
		HttpURLConnection connection = (HttpURLConnection) downloadFileInfo.getUploadUrl().openConnection();
		connection.setRequestProperty("User-Agent", USER_AGENT);
		connection.setRequestMethod("HEAD");

		connection.connect();

		int statusCode = connection.getResponseCode();

		if (!HttpUtilities.success(statusCode))
			throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "Server can not download file");

		String mimeType = connection.getContentType().toLowerCase();

		if (mimeType.contains("application/xml") || mimeType.contains("text/xml")
				|| mimeType.contains("application/json") || mimeType.contains("text/html"))
			throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "Given URL does not point to a file.");

		downloadFileInfo.setContentLength(connection.getContentLengthLong());
		downloadFileInfo.setContentType(connection.getContentType());

		String acceptRangeHeader = connection.getHeaderField("Accept-Ranges");

		if (acceptRangeHeader != null)
			downloadFileInfo.setResumeSupported(acceptRangeHeader.startsWith("bytes"));
		else
			downloadFileInfo.setResumeSupported(checkResumeSupportUsingGetMethod());

		if (downloadFileInfo.getFileName() == null || downloadFileInfo.getFileName().equals("")) {
			String contentDisposition = connection.getHeaderField("Content-Disposition");
			downloadFileInfo.setFileName(findFileName(contentDisposition));
		}
	}

	private String findFileName(String contentDisposition) {

		if (contentDisposition != null)
			try {
				Matcher matcher = CONTENT_DISPOSITION_PATTERN.matcher(contentDisposition);

				if (matcher.find()) {
					String match = matcher.group(1);

					if (match != null && !match.equals("")) {
						match = URLDecoder.decode(match, "UTF-8");
						return match;
					}
				}

			} catch (IllegalStateException ex) {
				// cannot find filename using content disposition http header.
				// Use url to find filename;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		return FilenameUtils.getName(downloadFileInfo.getUploadUrl().getPath());
	}

	private boolean checkResumeSupportUsingGetMethod() throws IOException {
		HttpURLConnection connection = (HttpURLConnection) downloadFileInfo.getUploadUrl().openConnection();
		connection.setRequestMethod("GET");
		String rangeHeaderValue = "bytes=" + 0 + "-" + 0;
		connection.setRequestProperty("Range", rangeHeaderValue);
		connection.setRequestProperty("User-Agent", USER_AGENT);
		return HttpStatus.PARTIAL_CONTENT.value() == connection.getResponseCode();
	}
}
