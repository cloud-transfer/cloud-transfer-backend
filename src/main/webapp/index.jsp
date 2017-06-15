<%-- 
    Document   : index
    Created on : 15 Jun, 2017, 6:17:32 AM
    Author     : Aditya
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:ui_layout title="Save2Drive - Home">
    
    <jsp:attribute name="head_area">
        
    </jsp:attribute>
    
    <jsp:attribute name="body_area">
        <div class="mui-row">
            <div class="mui-col-xs-6 mui-col-md-offset-4 mui-col-md-4">
                <div class="mui--text-display3 mui--text-center">Save2Drive</div>
            </div>
        </div>
        <br/>
        <div class="mui-row">
            <div class="mui--text-center">
                <p> With this app, you shall be able to download a file from URL <b>directly</b> to Google Drive </p>
            </div>
        </div>
        <div class="mui-row">
            <div class="mui-col-xs-4 mui-col-md-offset-4 mui-col-md-4">
                <form><button class="mui-btn mui-btn--raised mui-btn--primary" id="auth-btn" formaction="/OAuth2/OAuthRedirectServlet"><i class="fa fa-google" aria-hidden="true"></i>
  &nbsp;&nbsp; Authenticate With Google </button></form>
            </div>
        </div>
        
    </jsp:attribute>
    
</t:ui_layout>