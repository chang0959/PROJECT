<%@ page contentType="text/html;charset=UTF-8" errorPage="addrbook_error.jsp"%>
<jsp:useBean id="login" class="jspbook.hw3.loginBean" scope="application" />
<jsp:setProperty name="login" property="*" />
<jsp:useBean id="ab" class="jspbook.addrbook.AddrBean" scope="application" />

<HTML>
<HEAD><meta charset="UTF-8">
<TITLE>나의 쇼핑몰 </TITLE></HEAD>
<BODY>
<div align=center>
<% 
	request.setCharacterEncoding("UTF-8"); 
	if(!login.checkUser()) {//관리자는 아닌 경우
		if(!ab.checkUser(request.getParameter("userid"), request.getParameter("passwd"))){
			//false 를 전달받음(관리자도 아니고 고객도 아닌 경우)
			out.println("로그인 실패!");
			pageContext.include("login_form.html");
		}
		else{//관리자는 아니지만 고객인 경우
			session.setAttribute("cid",request.getParameter("userid"));
			response.sendRedirect("control.jsp?action=clist");//고객인경우 고객의 구매 리스트로 이동
			//관리자의 구매 리스트가 아니기때문에 action 을 clist 로 수정
		}
	}
	
	else {
		//out.println("로그인 성공!");
		session.setAttribute("id","관리자");
		response.sendRedirect("manager_home.jsp");
	}
%>

</div>
</BODY>
</HTML>