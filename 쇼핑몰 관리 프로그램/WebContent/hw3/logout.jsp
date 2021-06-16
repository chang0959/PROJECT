<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="addrbook_error.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>로그아웃 화면</title>
</head>
<body>

<% session.invalidate();
response.sendRedirect("index.html");%>

</body>
</html>


