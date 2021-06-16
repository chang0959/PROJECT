<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="addrbook_error.jsp" import="jspbook.hw3.*"%>
<!DOCTYPE HTML>
<html>
<head>
<link rel="stylesheet" href="addrbook.css" type="text/css" media="screen" />

<script type="text/javascript">
	function delcheck(){
	result = confirm("정말로 삭제하겠습니까?");
	if(result == true){
		//삭제
		document.form1.action.value="delete"; //form 의 form1 에서의 action 값을 delete 로 줌
		document.form1.submit();
		}
	else {//취소
		return;
		}
	}
</script>


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>상품정보수정화면</title>
</head>

<jsp:useBean id="ab" scope="request" class="jspbook.hw3.AddrBook" /> 

<body>
<div align="center">
<H2>${sessionScope.id} : 상품정보 수정화면 </H2>

<HR>
[<a href=list.jsp>상품 목록으로</a>] <br>
[<a href="manager_home.jsp">관리자 홈페이지로</a>] <br>
[<a href="logout.jsp">로그아웃</a>]<br>

<p>

<form name=form1 method=post action=control.jsp>
<input type=hidden name="ab_id" value="<%=ab.getAb_id()%>">
<input type=hidden name="action" value="update"><!-- 저장 버튼을 눌르면 update 란 이름을 가지고 control 페이지로 감 -->

 
<table border="1">
  <tr>
    <th>상품명</th>
    <td><input type="text" name="ab_title" value="<%= ab.getAb_title() %>"></td>
  </tr>
  <tr>
    <th>품목</th>
    <td><input type="text" name="ab_clothes" value="<%= ab.getAb_clothes()%>"></td>
  </tr>
    <tr>
    <th>제조사</th>
    <td><input type="text" name="ab_manu" value="<%= ab.getAb_manu()%>"></td>
  </tr>
      <tr>
    <th>가격</th>
    <td><input type="text" name="ab_price" value="<%= ab.getAb_price()%>"></td>
  </tr>
  <tr>
    <th>전화번호</th>
    <td><input type="text" name="ab_tele" value="<%= ab.getAb_tele()%>"></td>
  </tr>
  <tr>
    <th>메 모</th>
    <td><input type="text" name="ab_me" value="<%= ab.getAb_me()%>"></td>
  </tr>
  <tr>
    <td colspan=2 align=center><input type=submit value="저장">
    <input type=reset value="취소"><input type="button" value="삭제" onClick="delcheck()"></td>
</tr><!-- 저장을 누르면 control 페이지로 이동 -->
<!-- 삭제 버튼을 눌르면 delcheck 메소드 호출 -->
</table>
</form>

</div>
<%@include file="footer.jsp" %>
</body>
</html>