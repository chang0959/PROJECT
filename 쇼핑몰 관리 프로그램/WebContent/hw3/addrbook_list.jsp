<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="addrbook_error.jsp" 
    import="java.util.*,jspbook.addrbook.*"%>
<!DOCTYPE HTML>
<html>
<head>
<link rel="stylesheet" href="addrbook.css" type="text/css" media="screen" /> <!-- css 포함 -->

<script type="text/javascript">
	function check(ab_id){
		pwd=prompt('수정/삭제를 하려면 비밀번호를 넣으세요');
		document.location.href="addrbook_control.jsp?action=edit&ab_id="+ab_id+"&pwd="+pwd; //control 을 통해 경유 형태는 action=edit 처럼 이름 = 값의 형태로 해야함
	}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>고객목록화면</title>

</head>
<jsp:useBean id="datas" scope="request" class="java.util.ArrayList"/> <!--forward 될 때 (request 될 때) -->
<body>
<div align="center"> 
<H2>${sessionScope.id} : 고객목록화면</H2>
<HR>
<form>
	<a href="addrbook_form.jsp">고객 등록</a><br>
	<a href="manager_home.jsp">관리자 홈페이지</a> <br>
	[<a href="logout.jsp">로그아웃</a>]<br>
	
	<P>
	
	<table border="1">
		<tr><th>아이디</th><th>비밀번호</th><th>이 름</th><th>이메일</th><th>전화번호</th><th>생 일</th><th>회 사</th><th>메 모</th></tr>
	 <%
	 	for(AddrBook ab : (ArrayList<AddrBook>)datas){//AddrBook 을 ab 로 가져올 수 있다.
	 %>
	 	<tr>
		<td><a href="javascript:check('<%=ab.getAb_id() %>')"><%=ab.getAb_id() %></a></td>
		<!-- 스크립트에 들려서 정말로 수정할 것인지 묻는다.
		자바스크립트에는 int 는 괜찮지만 string 은 ''로 넣어줘야함 -->
		<td><%=ab.getAb_pwd()%></td>
		<td><%=ab.getAb_name()%></td>
		<td><%=ab.getAb_email()%></td>
		<td><%=ab.getAb_tel()%></td>
		<td><%=ab.getAb_birth()%></td>
		<td><%=ab.getAb_comdept()%></td>
		<td><a href="<%=ab.getAb_memo()%>"><%=ab.getAb_memo()%></a></td>
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