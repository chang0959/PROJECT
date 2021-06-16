<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="addrbook_error.jsp" 
    import="java.util.*,jspbook.hw3.*"%>
<!DOCTYPE HTML>
<html>
<head>
<link rel="stylesheet" href="addrbook.css" type="text/css" media="screen" /> <!-- css 포함 -->

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>구매목록화면</title>

</head>
<jsp:useBean id="datas" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="pd" scope="request" class="jspbook.hw3.PrdBean"/>
<jsp:useBean id="product" scope="request" class="jspbook.hw3.Product"/>
<jsp:useBean id="odb" scope="request" class="jspbook.hw3.OrderBean"/>
<jsp:useBean id="od" scope="request" class="jspbook.hw3.Order"/>
<!--forward 될 때 (request 될 때) -->
<body>
<div align="center"> 

<H2>${sessionScope.cid} 님의 구매목록화면</H2>
<h4>관리자 모드</h4>
<HR>
<form>
	[<a href="manager_home.jsp">관리자 홈페이지</a>] <br>
	[<a href="logout.jsp">로그아웃</a>]<br>
	<P>
	<table>
	
	  <%
	 	for(Order ob : (ArrayList<Order>)datas){//AddrBook 을 ab 로 가져올 수 있다.
	 %>
		<tr><th><%=ob.getPd_id1()%>번 품목의 개수</th><th><%=ob.getPd_id2()%>번 품목의 개수</th><th><%=ob.getPd_id3()%>번 품목의 개수</th>
		<th><%=ob.getPd_id4()%>번 품목의 개수</th><th><%=ob.getPd_id5()%>번 품목의 개수</th></tr>
		<td> <%=ob.getPd_num1()%> 개</td>
		<td> <%=ob.getPd_num2()%> 개</td>
		<td> <%=ob.getPd_num3()%> 개</td>
		<td> <%=ob.getPd_num4()%> 개</td>
		<td> <%=ob.getPd_num5()%> 개</td>

		<%
	 	};
	%>
	</table>
	
</form>
</div>
<%@include file="footer.jsp" %>
</body>
</html>