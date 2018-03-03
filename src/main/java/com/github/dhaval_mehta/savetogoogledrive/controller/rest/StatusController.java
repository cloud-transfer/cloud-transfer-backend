package com.github.dhaval_mehta.savetogoogledrive.controller.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.dhaval_mehta.savetogoogledrive.model.ApiError;
import com.github.dhaval_mehta.savetogoogledrive.model.UploadInformation;
import com.github.dhaval_mehta.savetogoogledrive.uploader.UploadManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("api/status")
@Api(description = "handle requests for upload status.")
public class StatusController {

	private final HttpSession session;

	@Autowired
	public StatusController(HttpSession session) {
		this.session = session;
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "gives upload information for given id", response = UploadInformation.class)
	@ApiResponses({ @ApiResponse(code = 404, message = "server does not find any upload with given ID"),
			@ApiResponse(code = 500, message = "There is something wrong at server side. Please contact developers.", response = ApiError.class) })
	public ResponseEntity<UploadInformation> getStatus(@PathVariable("id") String id) {
		UploadInformation uploadInformation = UploadManager.getUploadManager().getUploadInformation(id);

		if (uploadInformation == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		return new ResponseEntity<>(uploadInformation, HttpStatus.OK);
	}

	@GetMapping
	@ApiOperation(value = "gives array of upload information of current user.", response = UploadInformation[].class)
	@ApiResponses({
			@ApiResponse(code = 500, message = "There is something wrong at server side. Please contact developers.", response = ApiError.class) })
	public List<UploadInformation> handleStatusRequest() {

		@SuppressWarnings("unchecked")
		List<String> uploads = (List<String>) session.getAttribute("uploads");
		List<UploadInformation> uploadInformations = new ArrayList<>();

		if (uploads != null)
			uploads.forEach((id) -> uploadInformations.add(UploadManager.getUploadManager().getUploadInformation(id)));

		Collections.reverse(uploadInformations);
		return uploadInformations;
	}
}
