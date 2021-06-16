<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, jspbook.hw3.*" %>
<jsp:useBean id="pd" class="jspbook.hw3.PrdBean"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<HTML>
<HEAD>
<link rel="stylesheet" href="addrbook2.css" type="text/css" media="screen" />
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>나의 쇼핑몰</title>
</HEAD>
<body>
<div align="center">
<H2><%= session.getAttribute("cid")%> 님이 선택한 장바구니 상품 목록<br></H2>
<a href ="control.jsp?action=clist">고객홈페이지</a><br>
<a href="addrbook_control.jsp?action=cedit&ab_id=${sessionScope.cid}">고객 정보 관리</a> <br>
[<a href="logout.jsp">로그아웃</a>]<br>
<HR>
<%
	ArrayList list = (ArrayList)session.getAttribute("productlist");
	if(list == null) {
		out.println("선택한 상품이 없습니다.!!!");
	}
	else {
	%>
	<form name=form1 method=post action="order_control.jsp">
	<input type="hidden" name="action" value="cinsert">
	<input type="hidden" name="ab_id" value="${sessionScope.cid}">
	
	<table border="1">
	<tr><th>번호</th><th>상품명</th><th>가격</th><th>수량</th></tr>
	<%
	int total = 0, n=1;  
	for(Object productname:list) {
	int target_n = productname.toString().indexOf("-");
	String p_id = productname.toString().substring(0,target_n);  
	String p_count =
	productname.toString().substring(target_n+1,productname.toString().length());  

	Product prd = pd.getDB(Integer.parseInt(p_id));
	out.println("<tr>");
	out.println("<input type=hidden name=pd_id"+n+" value="+prd.getPd_id()+">");  
	out.println("<input type=hidden name=pd_num"+n+" value="+p_count+">");
	n = n+1;
	out.println("<td>"+prd.getPd_id()+"</td>");
	out.println("<td>"+prd.getPd_name()+"</td>");
	out.println("<td>"+prd.getPd_price()+"</td>");
	out.println("<td>"+p_count+"</td>");
	out.println("</tr>");
	total += Integer.parseInt(prd.getPd_price())*Integer.parseInt(p_count);
	}
	
	if(n <= 5){
		for( ; n <= 5 ; n++) {
		out.println("<input type=hidden name=pd_id"+n+" value="+0+">");
		out.println("<input type=hidden name=pd_num"+n+" value=\"\">");
		}
		}
	%>
	<input type=hidden name="pd_total" value="<%=total%>">
	<tr><td colspan=4> 총합은 <%=total%>원</td> </tr>
	</table>
	<br>
	<input type=submit value="구매하기">
	
	</form>
<%
}
%>

</div>
<%@include file="footer.jsp" %>
</body>
</html>