<%-- 
    Document   : index
    Created on : 13 Sep, 2017, 8:19:23 PM
    Author     : Dhaval
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<ui:templete title="Welcome to Save to Drive">
    <jsp:attribute name="head_area"/>
    <jsp:attribute name="body_area">
        <section class="dashboard-counts no-padding-bottom center">
            <div class="container-fluid">
                <div class="row bg-white has-shadow">
                    <c:choose>
                        <c:when test='${empty(user)}'>
                            <div style="text-align: center">
                                <p style="font-size: 20px; color: red">You are not authenticated. Please authenticate
                                    with Google. </p>
                                <div>
                                    <form action="${pageContext.request.contextPath}/api/oauth/google/redirect">
                                        <button id="auth-btn" class="btn btn-primary"><i class="fa fa-google"
                                                                                         aria-hidden="true"></i>
                                            &nbsp;&nbsp; Authenticate With Google
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="col-xs-6">
                                <div class="center">
                                    <image src='<c:out value="${user.profilePhotoUrl}" />'
                                           style="height: 250px; width: 250px;">
                                </div>
                            </div>
                            <div class="col-xs-3">
                                <p style="font-size: 20px; color: green">You are successfully authenticated with Google
                                    as <c:out value="${user.name} (${user.email})"/>.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </section>
    </jsp:attribute>
</ui:templete>