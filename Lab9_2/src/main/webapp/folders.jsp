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
        <li class="active">
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
    <c:forEach var="folder" items="${folders}" >
        <li style="margin-bottom: 20px">
            name: ${folder.name}
            <div style="position: absolute; left: 740px; " >
                <form action="home" method="post">
                    <input name="command" type="hidden" value="getEditFolder">
                    <input name="id" type="hidden" value="${folder.id}">
                    <button class="btn btn-danger" type="submit">Edit</button>
                </form>
            </div>
            <div style="position: absolute; left: 800px; " >
                <form action="home" method="get">
                    <input name="command" type="hidden" value="getFilesInFolder">
                    <input name="id" type="hidden" value="${folder.id}">
                    <button class="btn btn-danger" type="submit">Show files</button>
                </form>
            </div>
            <div style="position: absolute; left: 900px; " >
                <form action="home" method="post">
                    <input name="command" type="hidden" value="deleteFolder">
                    <input name="id" type="hidden" value="${folder.id}">
                    <button class="btn btn-danger" type="submit">Delete</button>
                </form>
            </div>
        </li>
    </c:forEach>
</ul>
</body>
</html>
