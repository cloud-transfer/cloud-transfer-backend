package com.github.dhavalmehta1997.savetogoogledrive.controller.drive;

import com.github.dhavalmehta1997.savetogoogledrive.controller.BaseController;
import com.github.dhavalmehta1997.savetogoogledrive.exception.HttpResponseException;
import com.github.dhavalmehta1997.savetogoogledrive.model.User;
import com.github.dhavalmehta1997.savetogoogledrive.uploader.UploadManager;
import com.github.dhavalmehta1997.savetogoogledrive.uploader.UploadTask;
import com.github.dhavalmehta1997.savetogoogledrive.uploader.Uploader;
import com.github.dhavalmehta1997.savetogoogledrive.uploader.drive.DriveUploaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@RestController
public class DriveUploadRequestHandler extends BaseController {

    private final HttpSession session;

    @Autowired
    public DriveUploadRequestHandler(HttpSession session) {
        this.session = session;
    }

    @RequestMapping("/drive/upload")
    public ResponseEntity<String> handleUploadRequest(@RequestParam("url") URL url, @RequestParam(value = "filename", required = false) String filename) {
        User user = (User) session.getAttribute("user");
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
            return new ResponseEntity<>(uploadTask.getId(), HttpStatus.OK);

        } catch (java.net.UnknownHostException ex) {
            throw new HttpResponseException(HttpStatus.BAD_REQUEST, url.getHost() + ": website not found. ", ex);
        } catch (IOException ex) {
            throw new HttpResponseException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }
}
