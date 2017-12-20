<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@page contentType="text/html;charset=UTF-8"%>

<portlet:defineObjects />
<liferay-theme:defineObjects />
<%
String themeImagePath = themeDisplay.getPathThemeImages();
%>
<html>
  <head>
    <style>
    .centered{
    position:absolute;
    width:-300px;
    height:-100px;
    top:50%;
    }
    </style>
  </head>
  <body>

    <%-- <p>Welcome <%=request.getAttribute("name")%></p> --%>

    <div class="centered">Please <a href="http://hgar-srv2.bu.edu/web/guest/digital-reserve?p_p_id=58&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&saveLastPath=false&_58_struts_action=%2Flogin%2Flogin">login</a> to view your digital reserve</div>

  </body>

</html>
