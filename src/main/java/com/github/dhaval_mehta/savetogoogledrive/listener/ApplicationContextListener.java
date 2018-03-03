package com.github.dhaval_mehta.savetogoogledrive.listener;

import com.github.dhaval_mehta.savetogoogledrive.uploader.UploadManager;

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
