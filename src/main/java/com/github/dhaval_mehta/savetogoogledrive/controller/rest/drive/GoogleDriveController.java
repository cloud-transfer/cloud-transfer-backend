package com.github.dhaval_mehta.savetogoogledrive.controller.rest.drive;

import com.github.dhaval_mehta.savetogoogledrive.transfer_service.TransferManager;
import com.github.dhaval_mehta.savetogoogledrive.transfer_service.UrlToGoogleDriveTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("api/drive")
public class GoogleDriveController {

    private final HttpSession session;

    @Autowired
    public GoogleDriveController(HttpSession session) {
        this.session = session;
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> handleUploadRequest(@RequestParam("url") URI url,
                                                    @RequestParam(value = "filename", required = false) String filename)
            throws IOException {

        var transferService = new UrlToGoogleDriveTransferService(token, url, filename);
        TransferManager.getInstance().addTask(transferService);
        return ResponseEntity.accepted().build();
    }
}
