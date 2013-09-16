<%
/*
 *  GeoBatch - Open Source geospatial batch processing system
 *  http://geobatch.geo-solutions.it/
 *  Copyright (C) 2007-2012 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
%>
<%@ page contentType="text/html" import="java.sql.*, java.io.*, java.util.*, it.geosolutions.geobatch.catalog.*, it.geosolutions.geobatch.flow.event.action.*, it.geosolutions.geobatch.flow.event.listeners.status.*" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title wicket:id="pageTitle">GeoBatch Login</title>
      <link href="img/favicon.ico" rel="shortcut icon"/>
      <link rel="stylesheet" href="css/ui-ext.css" type="text/css"
              media="screen, projection" />
      <!--[if IE]>
        <link rel="stylesheet" href="css/blueprint/ie.css" type="text/css" media="screen, projection" />
	    <link rel="stylesheet" href="css/ie.css" type="text/css" media="screen, projection" />
      <![endif]-->
</head>
<body>
    
 <div class="geoSDIWrapper" >
            <div class="google-header-bar" >
                <div class="header geoSDIContent geoSDIClearFix">
                    <a href="index.html"><img class="logo" src="img/geoautomator.png"	alt="Geo Automator" /></a>
                </div>
            </div>
            <div class="geoSDIMain geoSDIContent geoSDIClearFix">
                <div class="geoSDISign-in">
                    <div class="geoSDISignin-box">
                        <form name="f" action="<c:url value='j_spring_security_check'/>" method="post">
                            <h2>Accesso a GeoAutomator
                                <strong></strong>
                            </h2>

                            <strong class="geoSDIEmail-label">
                                <g:Label>Nome Utente</g:Label>
                            </strong>
                            <input class="inputGeoSdi" type='text' name='j_username' value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'/>
                            <p></p>
                            <strong class="geoSDIPassword-label">
                                <g:Label>Password</g:Label>
                            </strong>
                                <input class="inputGeoSdi" type='password' name='j_password'>
        
                            <p></p>
                            <p></p>
                            <input name="submit" class="geoSDI-Button geoSDI-Button-submit" type="submit">
                            <c:if test="${not empty param.login_error}">
                                <div class="loginError">
                                    Your login attempt was not successful, try again.<br/><br/>
                                    Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
                                </div>
                            </c:if>
                            
                        </form>
                    </div>
                </div>

            </div>
        </div>
                             <div class="geosdi-footer-bar">
            <div class="geoSDI-Footer geoSDIContent geoSDIClearFix">
                <ul>
                    <li>© 2011-2012 
                        <a href="http://www.protezionecivile.it" target="_blank">Protezione Civile Nazionale</a> - powered by CNR IMAA 
                        <a href="http://www.geosdi.org" target="_blank">geoSDI</a>
                    </li>
                </ul>
            </div>
        </div>
    </body>
</html>    