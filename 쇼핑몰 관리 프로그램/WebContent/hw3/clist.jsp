<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="addrbook_error.jsp" 
    import="java.util.*,jspbook.hw3.*"%>
<!DOCTYPE HTML>
<html>
<head>
<link rel="stylesheet" href="addrbook2.css" type="text/css" media="screen" /> <!-- css 포함 -->

<script type="text/javascript">
	function check(ab_id){
		document.form1.pd_id.value = ab_id;
		document.form1.submit();
	}

</script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>나의 쇼핑몰</title>

</head>
<jsp:useBean id="datas" scope="request" class="java.util.ArrayList"/> <!--forward 될 때 (request 될 때) -->
<body>
<div align="center"> 
<H2>${sessionScope.cid} 님 환영합니다!! </H2>
<h4>물품 목록 보기</h4>
<a href="addrbook_control.jsp?action=cedit&ab_id=${sessionScope.cid}">고객 정보 관리</a> <br>
<a href="order_control.jsp?action=clist&aid=${sessionScope.cid}">구매 목록 보기</a> <br>
[<a href="logout.jsp">로그아웃</a>]<br>
<HR>
<form name =form1 method= post action=add.jsp><!-- 6장의 add.jsp 이용 -->
<input type=hidden name="pd_id" value="pd_id">
<a href="checkOut.jsp">장바구니 가기</a>
	<!-- <a href="form.jsp">상품 등록</a><br>
	<a href="manager_home.jsp">관리자 홈페이지</a> 관리자의 링크 필요없음-->
	<P>
	
	<table border="1">
		<tr><th>번호</th><th>상품명</th><th>품목</th><th>제조사</th><th>가격</th><th>전화번호</th><th>메 모</th>
		<th>수량</th><th>장바구니</th></tr>
	 <%
	 	for(AddrBook ab : (ArrayList<AddrBook>)datas){//AddrBook 을 ab 로 가져올 수 있다.	 		
	 %>
	 	<tr>
		<td><%=ab.getAb_id() %></td>
		<td><%=ab.getAb_title() %></td>
		<td><%=ab.getAb_clothes()%></td>
		<td><%=ab.getAb_manu()%></td>
		<td><%=ab.getAb_price()%></td>
		<td><%=ab.getAb_tele()%></td>
		<td><a href="<%=ab.getAb_me()%>"><%=ab.getAb_me()%></a></td>
		<td><select name = "product_count<%=ab.getAb_id() %>">
		<option selected value="0">선택</option>
		<option value="1">1</option>
		<option value="2">2</option>
		<option value="3">3</option>
		<option value="4">4</option>
		<option value="5">5</option>
		<option value="6">6</option>
		</select></td>
		<td><input type="button" value="장바구니" onClick="check(<%=ab.getAb_id() %>)">
		</td>
		<!-- check 메소드에서는 나의 아이디 값을 전달하도록  -->
		</tr>
	<%
		}
	%>
	</table>
</form>

</div>
<%@include file="footer.jsp" %>
</body>
</html>
