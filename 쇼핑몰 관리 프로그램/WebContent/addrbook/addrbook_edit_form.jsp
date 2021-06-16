<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="addrbook_error.jsp" import="jspbook.addrbook.*"%>
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
<title>주소록:수정화면</title>
</head>

<jsp:useBean id="ab" scope="request" class="jspbook.addrbook.AddrBook" />

<body>
<div align="center">
<H2>주소록:수정화면 </H2>
<HR>
[<a href=addrbook_list.jsp>주소록 목록으로</a>] <p>
<form name=form1 method=post action=addrbook_control.jsp>
<input type=hidden name="ab_id" value="<%=ab.getAb_id()%>">
<input type=hidden name="action" value="update"><!-- 저장 버튼을 눌르면 update 란 이름을 가지고 control 페이지로 감 -->

 
<table border="1">
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
    <input type=reset value="취소"><input type="button" value="삭제" onClick="delcheck()"></td>
</tr><!-- 저장을 누르면 control 페이지로 이동 -->
<!-- 삭제 버튼을 눌르면 delcheck 메소드 호출 -->
</table>
</form>

</div>
</body>
</html>