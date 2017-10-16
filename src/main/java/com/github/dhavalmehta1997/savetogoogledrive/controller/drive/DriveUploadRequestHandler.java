package com.github.dhavalmehta1997.savetogoogledrive.controller.drive;

import com.github.dhavalmehta1997.savetogoogledrive.exception.ApiException;
import com.github.dhavalmehta1997.savetogoogledrive.model.User;
import com.github.dhavalmehta1997.savetogoogledrive.uploader.UploadManager;
import com.github.dhavalmehta1997.savetogoogledrive.uploader.UploadTask;
import com.github.dhavalmehta1997.savetogoogledrive.uploader.Uploader;
import com.github.dhavalmehta1997.savetogoogledrive.uploader.drive.DriveUploaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/drive")
public class DriveUploadRequestHandler {

    private final HttpSession session;

    @Autowired
    public DriveUploadRequestHandler(HttpSession session) {
        this.session = session;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleUploadRequest(@RequestParam("url") String urlString, @RequestParam(value = "filename", required = false) String filename, HttpServletResponse response) {
        User user = (User) session.getAttribute("user");
        URL url;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException ex) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "URL is Malformed", ex);
        }

        try {
            Uploader uploader = new DriveUploaderBuilder()
                    .setUploadUrl(url)
                    .setFileName(filename)
                    .setUser(user)
                    .build();

            UploadTask uploadTask = new UploadTask(uploader);
            UploadManager.getUploadManager().add(uploadTask);
            List<String> uploads = (List<String>) session.getAttribute("uploads");
            uploads.add(uploadTask.getId());

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .replacePath("api/status/")
                    .path(uploadTask.getId())
                    .build()
                    .toUri();

            response.setHeader("Location", location.toString());
            return ResponseEntity.accepted().build();

        } catch (FileNotFoundException ex) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "No file found at given URL", ex);
        } catch (UnknownHostException ex) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, url.getHost() + ": host not found. ", ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }
}
