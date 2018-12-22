package com.github.cloud_transfer.cloud_transfer.controller.drive;

import com.github.cloud_transfer.cloud_transfer.model.token.GoogleDriveToken;
import com.github.cloud_transfer.cloud_transfer.transfer_service.TransferManager;
import com.github.cloud_transfer.cloud_transfer.transfer_service.UrlToGoogleDriveTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
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
                                                    @RequestParam(value = "filename", required = false) String filename) {
        GoogleDriveToken token = new GoogleDriveToken();
        var transferService = new UrlToGoogleDriveTransferService(url, token, null);
        TransferManager.getInstance().addTask(transferService);
        return ResponseEntity.accepted().build();
    }
}
