<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="addrbook_error.jsp"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>나의 쇼핑몰</title>
</head>
<body>
<center>
<h2>${sessionScope.id} : 관리메뉴</h2>
<br>
<a href="addrbook_control.jsp?action=list">고객관리</a>
<a href="control.jsp?action=list">물품관리</a>
<a href="order_control.jsp?action=list">구매관리</a> <br>
<hr>
[<a href="logout.jsp">로그아웃</a>]<br>

</center>
<%@include file="footer.jsp" %>
</body>
</html>  