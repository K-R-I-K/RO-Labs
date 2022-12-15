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
    <li class="active">
      <a href="newFolder.jsp" class="nav-link"> Add folder </a>
    </li>
    <li class="nav-item">
      <a href="/home?command=getFileForm" class="nav-link"> Add file </a>
    </li>
  </ul>
</nav>

<section class="h-100 h-custom" style="background-color: #8fc4b7;">
  <div class="container py-5 h-100">
    <div class="row d-flex justify-content-center align-items-center h-100">
      <div class="col-lg-8 col-xl-6">
        <div class="card rounded-3">
          <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-registration/img3.webp"
               class="w-100" style="border-top-left-radius: .3rem; border-top-right-radius: .3rem;"
               alt="Sample photo">
          <div class="card-body p-4 p-md-5">
            <h3 class="mb-4 pb-2 pb-md-0 mb-md-5 px-md-2"> Add Folder </h3>
            <form action="/home" method="post" enctype="multipart/form-data">
              <input name="command" type="hidden" value="addFolder">

              <div class="form-outline mb-4">
                <input type="text" name="name" placeholder="name">
              </div>

              <button class = "btn btn-success btn-lg mb-1" type="submit">Add new folder</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>

<%--<h1>Add Folder</h1>
<form action="/home" method="post" enctype="multipart/form-data">
  <input name="command" type="hidden" value="addFolder">

  <input type="text" name="name" placeholder="name">

  <button class = "btn btn-success btn-lg mb-1" type="submit">Add new folder</button>
</form>--%>
</body>
</html>
