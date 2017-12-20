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
      <meta charset="utf-8">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js"></script>
      <script>
      var HttpClient = function() {
          this.get = function(aUrl, aCallback) {
              var anHttpRequest = new XMLHttpRequest();
              anHttpRequest.onreadystatechange = function() {
                  if (anHttpRequest.readyState == 4 && anHttpRequest.status == 200)
                      aCallback(anHttpRequest.responseText);
              }

              anHttpRequest.open( "GET", aUrl, true );
              anHttpRequest.send( null );
          }
      }

      var client = new HttpClient();

       var getSaved = function(){
         client.get("http://hgar-srv2.bu.edu/delegate/reserve/subject/view", function(response) {
           var obj = jQuery.parseJSON(response);
           var subjects = obj.response.data;
           var result = "Saved Subjects and Items" + "<br>";
           console.log("out here");
           if(subjects){
             var len = subjects.length;
             for(var i = 0; i< len; i++){
               result += "Subject " + i.toString() + ": " + subjects[i].id.toString() + "<br>";
               if(subjects[i].list){
                 var len2 = subjects[i].list.length;
                 result += "<blockquote>";
                 for(var j = 0; j< len2; j++){
                   result += subjects[i].list[j].content.toString() + "<br>";
                 }
                 result += "</blockquote>";
               }
             }
           }
           console.log(result);
           document.getElementById("demo").innerHTML = result;
         });
       }

       //getSaved();
      </script>
  </head>


  <body>

    <p>Welcome <%=request.getAttribute("name")%></p>

    <p>Student's Saved items</p>
    <p id="demo">Displaying...</p>

    <button type="button" onclick="getSaved()">Display</button>

  </body>

</html>
