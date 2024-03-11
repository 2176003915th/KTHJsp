<%--
  Created by IntelliJ IDEA.
  User: IN302
  Date: 2023-11-23
  Time: 오전 10:54
  To change this template use File | Settings | File Templates.
--%>

<html>
<head>
  <title>Title</title>
  <script src="https://kit.fontawesome.com/c0890a4f25.js" crossorigin="anonymous"></script>
</head>
<body>
<nav class="main-header navbar navbar-expand navbar-pink navbar-dark">
  <!-- Left navbar links -->
  <ul class="navbar-nav">
    <li class="nav-item">
      <a class="nav-link" data-widget="pushmenu" href="#" role="button"><i class="fas fa-bars"></i></a>
    </li>
    <li class="nav-item d-none d-sm-inline-block">
      <a href="index3.html" class="nav-link">Home</a>
    </li>
    <li class="nav-item d-none d-sm-inline-block">
      <a href="#" class="nav-link">Contact</a>
    </li>
  </ul>

  <!-- Right navbar links -->
  <ul class="navbar-nav ml-auto">
    <!-- Navbar Search -->
    <li class="nav-item">
      <a class="nav-link" data-widget="navbar-search" href="#" role="button">
        <i class="fas fa-search"></i>
      </a>
      <div class="navbar-search-block">
        <form class="form-inline">
          <div class="input-group input-group-sm">
            <input class="form-control form-control-navbar" type="search" placeholder="Search" aria-label="Search">
            <div class="input-group-append">
              <button class="btn btn-navbar" type="submit">
                <i class="fas fa-search"></i>
              </button>
              <button class="btn btn-navbar" type="button" data-widget="navbar-search">
                <i class="fas fa-times"></i>
              </button>
            </div>
          </div>
        </form>
      </div>
    </li>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <!-- EL Scope : pageScope -> requestScope -> sessionScope -> applicationScope -->
    <c:choose>
      <c:when test="${dto == null}">
    <!-- register Menu -->
    <li class="nav-item dropdown">
      <a class="nav-link" href="../members/post-form">
        <i class="fa-regular fa-registered"></i>
      </a>
    </li>
    <!-- login Menu -->
    <li class="nav-item dropdown">
      <a class="nav-link" href="../members/login-form">
        <i class="fa-solid fa-right-to-bracket"></i>
      </a>
    </li>
      </c:when>
    <c:otherwise>
    <!-- logout Menu -->
    <li class="nav-item dropdown">
      <a class="nav-link" href="../members/logout">
        <i class="fa-solid fa-reply-all"></i>
      </a>
    </li>
    <!-- detail Menu -->
    <li class="nav-item dropdown">
      <a class="nav-link" href="../members/get-one?mid=${dto.mid}">
        <i class="fa-regular fa-circle-user"></i>
      </a>
    </li>

      <c:if test="${dto.email == 'admin@naver.com'}">
<%--      <c:if test="${ooAdmin == true}">--%>
    <!--  list Menu -->
        <li class="nav-item dropdown">
          <a class="nav-link" href="../members/get-list">
            <i class="fa-regular fa-newspaper"></i>
          </a>
        </li>
     </c:if>
    </c:otherwise>
    </c:choose>
    <li class="nav-item">
      <a class="nav-link" data-widget="fullscreen" href="#" role="button">
        <i class="fas fa-expand-arrows-alt"></i>
      </a>
    </li>
    <li class="nav-item">
      <a class="nav-link" data-widget="control-sidebar" data-controlsidebar-slide="true" href="#" role="button">
        <i class="fas fa-th-large"></i>
      </a>
    </li>
  </ul>
</nav>
</body>
</html>
