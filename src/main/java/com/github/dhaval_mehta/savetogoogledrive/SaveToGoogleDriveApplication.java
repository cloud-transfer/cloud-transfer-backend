package com.github.dhaval_mehta.savetogoogledrive;

import org.apache.coyote.http2.Http2Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import java.net.URL;
import java.net.URLClassLoader;

@SpringBootApplication
@ServletComponentScan
public class SaveToGoogleDriveApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaveToGoogleDriveApplication.class, args);
    }

}