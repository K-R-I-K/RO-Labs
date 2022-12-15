<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
  <link rel="stylesheet" href="style/index.css">
  <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
</head>
<body>
<nav class="navbar navbar-expand-lg">
  <ul class="navbar-nav">
    <li class="nav-item">
      <a href="/home?command=getFolders" class="nav-link"> List of folders </a>
    </li>
    <li class="nav-item">
      <a href="newFolder.jsp" class="nav-link"> Add folder </a>
    </li>
    <li class="nav-item">
      <a href="/home?command=getFileForm" class="nav-link"> Add file </a>
    </li>
  </ul>
</nav>
<h3>list of folders</h3>
<ul>
  <c:forEach var="file" items="${files}" >
    <li style="margin-bottom: 20px">
      name: ${file.name}
      size: ${file.size}
      <div style="position: absolute; left: 750px; " >
        <form action="home" method="post">
          <input name="command" type="hidden" value="getEditFile">
          <input name="id" type="hidden" value="${file.id}">
          <button class="btn btn-danger" type="submit">Edit</button>
        </form>
      </div>
      <div style="position: absolute; left: 830px; " >
        <form action="home" method="get">
          <input name="command" type="hidden" value="deleteFile">
          <input name="id" type="hidden" value="${file.id}">
          <button class="btn btn-danger" type="submit">Delete</button>
        </form>
      </div>
    </li>
  </c:forEach>
</ul>
</body>
</html>
