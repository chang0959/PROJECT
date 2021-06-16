<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="addrbook_error.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>상품작성화면</title>
<link rel="stylesheet" href="addrbook.css" type="text/css" media="screen" />
</head>
<body>
<div align="center">
<H2>${sessionScope.id} :상품작성화면 </H2>
<HR>
[<a href=list.jsp>상품 목록으로</a>] <br>
[<a href="manager_home.jsp">관리자 홈페이지로</a>] <br>
[<a href="logout.jsp">로그아웃</a>]<br>

<P>

<form name=form1 method=post action=control.jsp>
<input type="hidden" name="action" value="insert">
<!-- 사용자에게 보이지 않지만 서버로 넘어가는 필드 control.jsp 에 insert 란 이름을 가지고 간다. -->
<table border="1">
 <tr>
    <th>상품명</th>	
    <td><input type="text" name="ab_title" maxlength="15"></td>
  </tr>
  <tr>
    <th>품목</th>
    <td>
    <select name="ab_clothes">
	<option selected>여성복</option>
	<option>남성복</option>
	<option>아동복</option>
	<option>운동복</option>
	</select>
    </td>
  </tr>
  <tr>
    <th>제조사</th>
    <td><input type="text" name="ab_manu" maxlength="20"></td>
  </tr>
  <tr>
    <th>가격</th>
    <td><input type="text" name="ab_price"></td>
  </tr>  
  <tr>
    <th>전화번호</th>
    <td><input type="text" name="ab_tele" maxlength="20"></td>
  </tr>
  <tr>
    <th>메 모</th>
    <td><input type="text" name="ab_me"></td>
  </tr>
  <tr>
    <td colspan=2 align=center><input type=submit value="저장"><input type=reset value="취소"></td>
</tr>
</table>
</form>
</div>
<%@include file="footer.jsp" %>lude>
</body>
</html>