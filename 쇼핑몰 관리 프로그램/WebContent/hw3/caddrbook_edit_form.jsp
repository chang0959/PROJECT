<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="addrbook_error.jsp" import="jspbook.addrbook.*"%>
<!DOCTYPE HTML>
<html>
<head>
<link rel="stylesheet" href="addrbook2.css" type="text/css" media="screen" />


<script type="text/javascript">
function delcheck(){
	result = confirm("정말로 탈퇴하겠습니까!!!!!!?");
	if(result == true){
		//삭제
		document.form1.action.value="cdelete"; //form 의 form1 에서의 action 값을 delete 로 줌
		document.form1.submit();
		}
	else {//취소
		return;
		}
	}
</script>


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>고객정보수정화면</title>
</head>

<jsp:useBean id="ab" scope="request" class="jspbook.addrbook.AddrBook" /> 

<body>
<div align="center">
<H2>${sessionScope.cid} : 고객정보 수정화면 </H2>
<HR>
<!-- [<a href=addrbook_list.jsp>고객 목록으로</a>] <br>관리자에 해당되는 링크 생략 -->
[<a href="control.jsp?action=clist">고객 홈페이지로</a>] <br>
[<a href="logout.jsp">로그아웃</a>]<br>
<p>

<form name=form1 method=post action=addrbook_control.jsp>
<input type=hidden name="ab_id" value="<%=ab.getAb_id()%>">
<input type=hidden name="action" value="cupdate"><!-- 저장 버튼을 눌르면 update 란 이름을 가지고 control 페이지로 감 -->

 
<table border="1">
  <tr>
    <th>아이디</th><!-- 아이디는 주키로 수정 불가능하므로 readonly 넣어서 text 처럼 보이기는 하지만 수정 불가능하게 함 -->
    <td><input type="text" name="ab_id" value="<%= ab.getAb_id() %>" readonly></td>
  </tr>
  <tr>
    <th>비밀번호</th><!-- type 을 text 로 하면 다 보이므로 password 로 설정-->
    <td><input type="password" name="ab_pwd" value="<%= ab.getAb_pwd() %>"></td>
  </tr>
  <tr>
    <th>이 름</th>
    <td><input type="text" name="ab_name" value="<%= ab.getAb_name() %>"></td>
  </tr>
  <tr>
    <th>이메일</th>
    <td><input type="text" name="ab_email" value="<%= ab.getAb_email()%>"></td>
  </tr>
    <tr>
    <th>전화번호</th>
    <td><input type="text" name="ab_tel" value="<%= ab.getAb_tel()%>"></td>
  </tr>
      <tr>
    <th>생 일</th>
    <td><input type="date" name="ab_birth" value="<%= ab.getAb_birth()%>"></td>
  </tr>
  <tr>
    <th>회 사</th>
    <td><input type="text" name="ab_comdept" value="<%= ab.getAb_comdept()%>"></td>
  </tr>
  <tr>
    <th>메 모</th>
    <td><input type="text" name="ab_memo" value="<%= ab.getAb_memo()%>"></td>
  </tr>
  <tr>
    <td colspan=2 align=center><input type=submit value="저장">
    <input type=reset value="취소">
    <input type="button" value="회원탈퇴"  onClick="delcheck()">
</tr><!-- 저장을 누르면 control 페이지로 이동 -->
<!-- 삭제 버튼을 눌르면 delcheck 메소드 호출 -->
</table>
</form>

</div>
<%@include file="footer.jsp" %>
</body>
</html>