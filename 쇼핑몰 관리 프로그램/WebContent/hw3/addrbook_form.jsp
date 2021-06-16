<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="addrbook_error.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>고객정보작성화면</title>
<link rel="stylesheet" href="addrbook.css" type="text/css" media="screen" />
</head>
<body>
<div align="center">
<H2>${sessionScope.id} :고객정보작성화면 </H2>
<HR>
[<a href=addrbook_list.jsp>고객 목록으로</a>] <br>
[<a href="manager_home.jsp">관리자 홈페이지로</a>] <br>
[<a href="logout.jsp">로그아웃</a>]<br>
<P>

<form name=form1 method=post action=addrbook_control.jsp>
<input type="hidden" name="action" value="insert">
<!-- 사용자에게 보이지 않지만 서버로 넘어가는 필드 control.jsp 에 insert 란 이름을 가지고 간다. -->
<table border="1">
  <tr>
    <th>아이디</th>
    <td><input type="text" name="ab_id" maxlength="15"></td>
  </tr>
  <tr>
    <th>비밀번호</th>
    <td><input type="password" name="ab_pwd" maxlength="15"></td>
  </tr>
  <tr>
    <th>이 름</th>	
    <td><input type="text" name="ab_name" maxlength="15"></td>
  </tr>
  <tr>
    <th>이메일</th>
    <td><input type="email" name="ab_email" maxlength="50"></td>
  </tr>
  <tr>
    <th>전화번호</th>
    <td><input type="text" name="ab_tel" maxlength="20"></td>
  </tr>
  <tr>
    <th>생 일</th>
    <td><input type="date" name="ab_birth"></td>
  </tr>  
  <tr>
    <th>회 사</th>
    <td><input type="text" name="ab_comdept" maxlength="20"></td>
  </tr>
  <tr>
    <th>메 모</th>
    <td><input type="text" name="ab_memo"></td>
  </tr>
  <tr>
    <td colspan=2 align=center><input type=submit value="저장"><input type=reset value="취소"></td>
</tr>
</table>
</form>

</div>
<%@include file="footer.jsp" %>
</body>
</html>