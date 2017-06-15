<%-- 
    Document   : ui_layout
    Created on : 15 Jun, 2017, 6:48:42 AM
    Author     : Aditya
--%>

<%@tag description="The template for the front-end" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="title"%>
<%@attribute name="head_area" fragment="true" %>
<%@attribute name="body_area" fragment="true" required="true" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="//cdn.muicss.com/mui-latest/css/mui.min.css" rel="stylesheet" type="text/css" />
        <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet" type="text/css" />
        <title>${title}</title>
        <jsp:invoke fragment="head_area"/>
    </head>
    <body>
        <!--Navbar Area-->
        <div id="sidedrawer" class="mui--no-user-select">
            <div id="sidedrawer-brand" class="mui--appbar-line-height">
              <span class="mui--text-title">Save2Drive</span>
            </div>
            <div class="mui-divider"></div>
            <ul>
              <li>
                  <a href="/">Home</a>
              </li>
              <li>
                  <a href="/about">About</a>
              </li>
              <li>
                  <a href="/developers">Developers</a>
              </li>
            </ul>
          </div>
        <!--/Navbar area-->
        
        <!--Appbar header-->
        <header id="header">
            <div class="mui-appbar mui--appbar-line-height">
              <div class="mui-container-fluid">
                <a class="sidedrawer-toggle mui--visible-xs-inline-block mui--visible-sm-inline-block js-show-sidedrawer"><i class="fa fa-bars" aria-hidden="true"></i></a>
                <a class="sidedrawer-toggle mui--hidden-xs mui--hidden-sm js-hide-sidedrawer"><i class="fa fa-bars" aria-hidden="true"></i></a>
                <span class="mui--text-title mui--visible-xs-inline-block mui--visible-sm-inline-block"> Save2Drive </span>
              </div>
            </div>
         </header>
        <!--/Appbar header-->
        
        <!--Content-->
        <div id="content-wrapper">
            <div class="mui--appbar-height"></div>
            <div class="mui-container-fluid">
              <br>
              <jsp:invoke fragment="body_area"/>
            </div>
        </div>
        <!--/Content-->
        
        <!--Footer-->
        <footer id="footer">
            <div class="mui-container-fluid">
              <br>
              &copy; <%= new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()) %>, Dhaval & Aditya
            </div>
        </footer>
        <!--/Footer-->
        
        <script src="//cdn.muicss.com/mui-latest/js/mui.min.js"></script>
        <script src="//code.jquery.com/jquery-2.1.4.min.js"></script>
        <script src="js/script.js"></script>
        
    </body>
</html>