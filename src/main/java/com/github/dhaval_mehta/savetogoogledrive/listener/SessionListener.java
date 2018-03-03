package com.github.dhaval_mehta.savetogoogledrive.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;

@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent.getSession().setAttribute("uploads", new ArrayList<String>());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
    }
}
