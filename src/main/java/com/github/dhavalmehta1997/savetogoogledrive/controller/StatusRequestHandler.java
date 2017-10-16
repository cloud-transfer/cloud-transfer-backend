package com.github.dhavalmehta1997.savetogoogledrive.controller;

import com.github.dhavalmehta1997.savetogoogledrive.model.UploadInformation;
import com.github.dhavalmehta1997.savetogoogledrive.uploader.UploadManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/status")
public class StatusRequestHandler {

    private final HttpSession session;

    @Autowired
    public StatusRequestHandler(HttpSession session) {
        this.session = session;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UploadInformation> getStatus(@PathVariable("id") String id) {
        UploadInformation uploadInformation = UploadManager.getUploadManager().getUploadInformation(id);

        if (uploadInformation == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(uploadInformation, HttpStatus.OK);
    }

    @GetMapping("/")
    public List<UploadInformation> handleStatusRequest() {

        List<String> uploads = (List<String>) session.getAttribute("uploads");
        List<UploadInformation> uploadInformations = new ArrayList<>();

        if (uploads != null)
            uploads.forEach((id) -> uploadInformations.add(UploadManager.getUploadManager().getUploadInformation(id)));

        Collections.reverse(uploadInformations);
        return uploadInformations;
    }
}
