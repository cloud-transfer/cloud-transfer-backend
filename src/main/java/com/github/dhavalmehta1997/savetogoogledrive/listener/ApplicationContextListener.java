package com.github.dhavalmehta1997.savetogoogledrive.listener;

import com.github.dhavalmehta1997.savetogoogledrive.uploader.UploadManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ApplicationContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        UploadManager.getUploadManager().distroy();
    }
}
