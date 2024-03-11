<%@ page import="idusw.javaweb.openapia.model.MemberDTO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%ArrayList<MemberDTO> memberList = (ArrayList<MemberDTO>) request.getAttribute("List");
    for(MemberDTO m : memberList)
        out.print(m.getEmail());
    %>
</body>
</html>
