<%-- 
    Document   : layout
    Created on : 15 Jun, 2017, 6:48:42 AM
    Author     : Dhaval
--%>

<%@tag description="The template for the front-end" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="title"%>
<%@attribute name="head_area" fragment="true" %>
<%@attribute name="body_area" fragment="true" required="true" %>

<!DOCTYPE html>
<html lang="en">
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Bootstrap Dashboard by Bootstrapious.com</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="robots" content="all,follow">
    <!-- Bootstrap CSS-->
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <!-- Google fonts - Lato | Open Sans | Poppins | Roboto | Work Sans -->
    <link href="https://fonts.googleapis.com/css?family=Lato|Open+Sans|Poppins|Roboto|Work+Sans" rel="stylesheet">
    <!-- theme stylesheet-->
    <link rel="stylesheet" href="css/style.default.css" id="theme-stylesheet">
    <!-- Favicon-->
    <link rel="shortcut icon" href="img/favicon.ico">
    <!-- Font Awesome CDN-->
    <script src="https://use.fontawesome.com/99347ac47f.js"></script>
    <!-- Font Icons CSS-->
    <link rel="stylesheet" href="https://file.myfontastic.com/da58YPMQ7U5HY8Rb6UxkNf/icons.css">
    <!-- Tweaks for older IEs--><!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script><![endif]-->
  </head>
    <body>
    <div class="page home-page">
      <!-- Main Navbar-->
      <header class="header">
        <nav class="navbar">
          <div class="container-fluid">
            <div class="navbar-holder d-flex align-items-center justify-content-between">
              <!-- Navbar Header-->
              <div class="navbar-header">
                <!-- Navbar Brand --><a href="index.html" class="navbar-brand">
                <div class="brand-text brand-big hidden-lg-down"><span>Save to</span> <strong>Drive</strong></div>
                  <div class="brand-text brand-small"><strong>SD</strong></div></a>
                  <!-- Toggle Button--><a id="toggle-btn" href="#" class="menu-btn active"><span></span><span></span><span></span></a>
              </div>
              <!-- Navbar Menu -->
              <ul class="nav-menu list-unstyled d-flex flex-md-row align-items-md-center">
                <!-- Logout    -->
                <li class="nav-item"><a href="/logout" class="nav-link logout">Logout<i class="fa fa-sign-out"></i></a></li>
              </ul>
            </div>
          </div>
        </nav>
      </header>
      <div class="page-content d-flex align-items-stretch">
        <!-- Side Navbar -->
        <nav class="side-navbar" style="min-height: 100%">
          <!-- Sidebar Header-->
          <div class="sidebar-header d-flex align-items-center">
            <div class="avatar"><img src="img/guest.png" alt="..." class="img-fluid rounded-circle"></div>
            <div class="title">
			<p>Welcome</p>
			<h1 class="h4">Guest</h1>
            </div>
          </div>
          <!-- Sidebar Navidation Menus--><span class="heading">Main</span>
          <ul class="list-unstyled">
            <li class="active"> <a href="./"><i class="icon-home"></i>Home</a></li>
			<li> <a href="./"><i class="fa fa-cloud-upload"></i>New upload</a></li>
            <li> <a href="tables.html"> <i class="fa fa-user"></i>Profile </a></li>
            <li> <a href="charts.html"> <i class="fa fa-thumbs-o-up"></i>Suggest an Idea</a></li>
            <li> <a href="forms.html"> <i class="fa fa-bug"></i>Report a bug </a></li>
			<li> <a href="login.html"> <i class="fa fa-envelope-o "></i>Contact us</a></li>
          </ul>
        </nav>
        <div class="content-inner" id="content-inner">
		
            <jsp:invoke fragment="body_area" />		
              
            <div class="push"></div>
            
            <!-- Page Footer-->
          <footer class="main-footer bg-white has-shadow">
            <div class="container-fluid">
              <div class="row ">
                <div class="col-sm-12 text-center">
                  <p>Design by Dhaval Mehta and Aditya Krishnakumar</p>
                  <!-- Please do not remove the backlink to us unless you support further theme's development at https://bootstrapious.com/donate. It is part of the license conditions. Thank you for understanding :)-->
                </div>
              </div>
            </div>
          </footer>
        </div>
      </div>
    </div>
    <!-- Javascript files-->
    <script src="js/jquery.min.js"></script>
    <script src="js/tether.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/jquery.cookie.js"> </script>
    <script src="js/jquery.validate.min.js"></script>
    <script src="js/front.js"></script>
    <script src="js/sticky-footer.js"></script>
  </body>
</html>