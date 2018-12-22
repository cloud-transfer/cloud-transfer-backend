package com.github.dhaval_mehta.savetogoogledrive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SaveToGoogleDriveApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaveToGoogleDriveApplication.class, args);
    }

}