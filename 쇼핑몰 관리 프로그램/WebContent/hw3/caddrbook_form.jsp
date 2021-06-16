<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="addrbook_error.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원가입화면</title>
<link rel="stylesheet" href="addrbook2.css" type="text/css" media="screen" />
</head>
<body>
<div align="center">
<H2>회원가입작성화면 </H2>
<HR>
[<a href="index.html">홈페이지로</a>]<p>


<P>

<form name=form1 method=post action=addrbook_control.jsp><!-- 고객관리로 이동 -->
<input type="hidden" name="action"  value="cinsert"> <!-- 고객에 대해서 insert -->

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