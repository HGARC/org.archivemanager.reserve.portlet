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
  <body>

    <head>
      <script>

      var checkedItems = document.querySelectorAll('input:checkbox[name=item]:checked')
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

      //add item
      var addItem = function(sub, id){
        client.get("http://hgar-srv2.bu.edu/delegate/reserve/subject/add.json?sub="+sub+"&id="+id, function(response) {
            console.log("great");
        });
      }

      //delete item
      var deleteItem = function(sub, id){
        var client = new HttpClient();
        client.get("http://hgar-srv2.bu.edu/delegate/reserve/subject/delete.json?sub="+sub+"&id="+id, function(response) {
            console.log("great");
        });
      }

      //add subject
      var addSubject = function(sub){
        var client = new HttpClient();
        client.get("http://hgar-srv2.bu.edu/delegate/reserve/subject/add?sub="+sub, function(response) {
            console.log("great");
        });
      }

      //delete subject
      var deleteSubject = function(sub){
        client.get("http://hgar-srv2.bu.edu/delegate/reserve/subject/delete?sub="+sub, function(response) {
            console.log("great");
        });
      }

      //
       var getSaved = function(){
         client.get("http://hgar-srv2.bu.edu/delegate/reserve/subject/view", function(response) {
         //   var news = document.getElementsByClassName("elements")[0];
         //   var data = response.response.data;
         //   for(var i = 0; i < items.length; i++) {
         //       var h5 = document.createElement("h5");
         //       h5.innerHTML = data[i].id;
         //       news.appendChild(h5);
         //       var p = document.createElement("p");
         //       p.innerHTML = data[i].list[0].id;
         //       news.appendChild(p);
         //   }
         });
       }

      </script>

    </head>

    <h1>User</h1>

    <hr>
      <div class="elements"></div>
    </hr>

  </body>
</html>
