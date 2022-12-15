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
    <h3>
        <h3 style="text-align: center">
            FileSystem
        </h3>
    </h3>
</body>
</html>
