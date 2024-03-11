<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title> c:sql </title>
</head>
<body>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<sql:setDataSource driver="com.mysql.cj.jdbc.Driver"
                   url="jdbc:mysql://localhost:3306/mysql"
                   user="root"
                   password="cometrue" />

<sql:query var="users" startRow="0" >
    select * from user where user like ?;
    <sql:param value="mysql%" />
</sql:query>

<c:forEach var="row" items="${users.rows}">
    ${row.Host} - ${row.User} <br>
</c:forEach>
</body>
</html>

