<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>KTH-3 | Simple Tables</title>

  <!-- Google Font: Source Sans Pro -->
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="../plugins/fontawesome-free/css/all.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="../dist/css/adminlte.min.css">
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
  <!-- Navbar -->
  <%@ include file="../main/navbar.jsp"%>
  <!-- /.navbar -->

  <!-- Main Sidebar Container -->
  <%@ include file="../main/main-sidebar.jsp" %>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Simple Tables</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="#">Home</a></li>
              <li class="breadcrumb-item active">Simple Tables</li>
            </ol>
          </div>
        </div>
      </div><!-- /.container-fluid -->
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-md-12">
            <div class="card">
              <div class="card-header">
                <h3 class="card-title">Member List</h3>

                <div class="card-tools">
                  <div class="input-group input-group-sm">
                    <form action="../members/search" method="get">
                      <div class="input-group-append">
                        <select name="by" id="inputStatus" class="form-control custom-select">
                          <c:choose>
                            <c:when test="${by == 'email'}">
                              <option>fullname</option>
                              <option selected>email</option>
                            </c:when>
                            <c:otherwise>
                              <option selected>fullname</option>
                              <option>eamil</option>
                            </c:otherwise>
                          </c:choose>
                        </select>
                        <input type="text" name="keyword" value="${keyword}" class="form-control float-right"
                               placeholder="Search">
                        <button type="submit" class="btn btn-default">
                          <i class="fas fa-search"></i>
                        </button>
                      </div>
                    </form>
                  </div>
              </div>
              </div>
              <!-- /.card-header -->
              <div class="card-body">
                <table class="table table-bordered">
                  <thead>
                  <tr>
                    <th>FullName</th>
                    <th>Email</th>
                    <th>Pw</th>
                    <th>delete</th>
                  </tr>
                  </thead>
                  <tbody>
                  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                  <c:forEach var="dto" items="${requestScope.dtoList}">
                    <tr>
                      <!-- dto 참조변수가 참조하는 객체 memberDTO 객체의 getFullName() 메소드 호출
                      , 없는 메소드 호출로 인한 오류 발생

                      MemeberDTO 객체의 멤버 메소드는 getFullName() 메소드이고 이를 호출해야함  참조
                      -->
                      <td>${dto.fullName}</td>
                      <td><span class="tag tag-success">${dto.email}</span></td>
                      <td>${dto.pw}</td>
                      <td>
                        <a class="btn btn-danger btn-sm" href="../members/listdelete?mid=${dto.mid}">
                          <i class="fas fa-trash">
                          </i>
                          Delete
                        </a>
                      </td>
                    </tr>
                  </c:forEach>
                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->

            </div>

            <!-- /.card -->


            <!-- /.card -->
          </div>
          <!-- /.col -->

        </div>
        <!-- /.row -->

        <!-- /.row -->
      </div><!-- /.container-fluid -->
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->
  <footer class="main-footer">
    <div class="float-right d-none d-sm-block">
      <b>Version</b> 3.2.0
    </div>
    <strong>Copyright &copy; 2014-2021 <a href="https://adminlte.io">AdminLTE.io</a>.</strong> All rights reserved.
  </footer>

  <!-- Control Sidebar -->
  <aside class="control-sidebar control-sidebar-dark">
    <!-- Control sidebar content goes here -->
  </aside>
  <!-- /.control-sidebar -->
</div>
<!-- ./wrapper -->

<!-- jQuery -->
<script src="../plugins/jquery/jquery.min.js"></script>
<!-- Bootstrap 4 -->
<script src="../plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<!-- AdminLTE App -->
<script src="../dist/js/adminlte.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="../dist/js/demo.js"></script>
</body>
</html>
