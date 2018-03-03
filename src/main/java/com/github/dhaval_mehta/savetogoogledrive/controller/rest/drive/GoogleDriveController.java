package com.github.dhaval_mehta.savetogoogledrive.controller.rest.drive;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import com.github.dhaval_mehta.savetogoogledrive.exception.ApiException;
import com.github.dhaval_mehta.savetogoogledrive.model.ApiError;
import com.github.dhaval_mehta.savetogoogledrive.model.User;
import com.github.dhaval_mehta.savetogoogledrive.uploader.UploadTask;
import com.github.dhaval_mehta.savetogoogledrive.uploader.Uploader;
import com.github.dhaval_mehta.savetogoogledrive.uploader.drive.DriveUploaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.dhaval_mehta.savetogoogledrive.uploader.UploadManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@RestController
@RequestMapping("api/drive")
@Api(description = "handles requests related to Google Drive", produces = "application/json", consumes = "application/json")
public class GoogleDriveController {

	private final HttpSession session;

	@Autowired
	public GoogleDriveController(HttpSession session) {
		this.session = session;
	}

	@PostMapping("/upload")
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ApiOperation(value = "takes URL and add it into upload queue")
	@ApiResponses({
			@ApiResponse(code = 202, message = "Server has successfully added URL into upload queue", responseHeaders = {
					@ResponseHeader(name = "Location", response = java.net.URL.class) }),
			@ApiResponse(code = 422, message = "Either URL is invalid or Server did't find file from submitted URL. For more details regrading error, read response json.", response = ApiError.class),
			@ApiResponse(code = 500, message = "There is something wrong at server side. Please contact developers.", response = ApiError.class) })
	public ResponseEntity<Void> handleUploadRequest(@RequestParam("url") String urlString,
			@RequestParam(value = "filename", required = false) String filename) {
		User user = (User) session.getAttribute("user");
		URL url;

		try {
			url = new URL(urlString);
		} catch (MalformedURLException ex) {
			throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "URL is Malformed", ex);
		}

		try {
			Uploader uploader = new DriveUploaderBuilder().setUploadUrl(url).setFileName(filename).setUser(user)
					.build();

			UploadTask uploadTask = new UploadTask(uploader);
			UploadManager.getUploadManager().add(uploadTask);
			@SuppressWarnings("unchecked")
			List<String> uploads = (List<String>) session.getAttribute("uploads");
			uploads.add(uploadTask.getId());

			URI location = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("api/status/")
					.replaceQueryParams(null).path(uploadTask.getId()).build().toUri();

			return ResponseEntity.accepted().header("Location", location.toString()).body(null);

		} catch (FileNotFoundException ex) {
			throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "No file found at given URL", ex);
		} catch (UnknownHostException ex) {
			throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, url.getHost() + ": host not found.", ex);
		} catch (IOException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
		}
	}
}
