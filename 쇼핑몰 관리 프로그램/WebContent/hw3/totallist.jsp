<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="addrbook_error.jsp" 
    import="java.util.*,jspbook.hw3.*"%>
<!DOCTYPE HTML>
<html>
<head>
<link rel="stylesheet" href="addrbook.css" type="text/css" media="screen" /> <!-- css 포함 -->

<script type="text/javascript">
	function check(aid){
		document.location.href="order_control.jsp?action=mlist&aid="+aid; 
	}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:useBean id="order" class="jspbook.hw3.Order"/>
<title>전체 구매 목록 화면</title>

</head>
<jsp:useBean id="datas1" scope="request" class="java.util.ArrayList"/> <!--forward 될 때 (request 될 때) -->
<body>
<div align="center"> 
<form>
	<a href="manager_home.jsp">관리자 홈페이지</a> <br>
	[<a href="logout.jsp">로그아웃</a>]<br>
	
	<P>
	
	<table border="1">
		<tr><th>아이디</th></tr>
	 <%
	 	for(Order ab : (ArrayList<Order>)datas1){//AddrBook 을 ab 로 가져올 수 있다.
	 %>
	 	<tr>
		<td><a href="javascript:check(<%=ab.getAb_id() %>)"><%=ab.getAb_id() %></a></td>
		</tr>
	<%
		};
	%>
	</table>	
</form>

</div>
<%@include file="footer.jsp" %>
</body>
</html>