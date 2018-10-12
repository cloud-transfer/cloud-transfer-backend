package com.github.dhaval_mehta.savetogoogledrive.controller.rest.drive;

import com.github.dhaval_mehta.savetogoogledrive.exception.ApiException;
import com.github.dhaval_mehta.savetogoogledrive.model.ApiError;
import com.github.dhaval_mehta.savetogoogledrive.model.User;
import com.github.dhaval_mehta.savetogoogledrive.model.token.GoogleDriveToken;
import com.github.dhaval_mehta.savetogoogledrive.transfer_service.UrlToGoogleDriveTransferService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URISyntaxException;

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
                    @ResponseHeader(name = "Location", response = java.net.URL.class)}),
            @ApiResponse(code = 422, message = "Either URL is invalid or Server did't find file from submitted URL. For more details regrading error, read response json.", response = ApiError.class),
            @ApiResponse(code = 500, message = "There is something wrong at server side. Please contact developers.", response = ApiError.class)})
    public ResponseEntity<Void> handleUploadRequest(@RequestParam("url") String urlString,
                                                    @RequestParam(value = "filename", required = false) String filename) {
        User user = (User) session.getAttribute("user");
        URI url;

        try {
            url = new URI(urlString);
        } catch (URISyntaxException ex) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "URL is Malformed", ex);
        }
        GoogleDriveToken token = new GoogleDriveToken();

        var transferService = new UrlToGoogleDriveTransferService(token, url, filename);
        return ResponseEntity.accepted().build();
    }
}
