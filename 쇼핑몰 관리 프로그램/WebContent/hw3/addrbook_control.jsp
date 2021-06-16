<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"  errorPage="addrbook_error.jsp" import="jspbook.addrbook.*, java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<% request.setCharacterEncoding("utf-8"); %>

<jsp:useBean id="ab" class="jspbook.addrbook.AddrBean"/> 
<jsp:useBean id="addrbook" class="jspbook.addrbook.AddrBook"/>
<jsp:setProperty name="addrbook" property="*"/> <!-- addrbook 에 값들을 set함 -->
<% 
	// 컨트롤러 요청 파라미터
	//<input type="hidden" name="action" value="insert"> 에 해당
	String action = request.getParameter("action");//어떠한 컨트롤을 전달했는지

	// 파라미터에 따른 요청 처리
	// 주소록 목록 요청인 경우
	//5개의 액션에 대해서 
	if(action.equals("list")) {
		ArrayList<AddrBook> datas = ab.getDBList(); //getDBList 의 리턴 타입은 클래스 이므로 ArrayList<AddrBook> 로 받는다.
		request.setAttribute("datas",datas);
		pageContext.forward("addrbook_list.jsp");//response.sendRedirect 사용 불가능
	}
	
	// 주소록 등록 요청인 경우
	else if(action.equals("insert")) {		
		if(ab.insertDB(addrbook)){//ab.insertDB(addrbook) 의 뜻은 addrbook 클래스를 insertDB 함.
			//true 인 경우이므로 insert 성공
			response.sendRedirect("addrbook_control.jsp?action=list");
		}
		else {//실패
			throw new Exception("DB 입력 오류");
		}
	}
	
	else if(action.equals("cinsert")) {		
		if(ab.insertDB(addrbook)){//ab.insertDB(addrbook) 의 뜻은 addrbook 클래스를 insertDB 함.
			//true 인 경우이므로 insert 성공
			session.setAttribute("cid",request.getParameter("ab_id"));
			response.sendRedirect("control.jsp?action=clist");
		}
		else {//실패
			throw new Exception("DB 입력 오류");
		}
	}
	
	// 주소록 수정 페이지 요청인 경우
	else if(action.equals("edit")) {
		AddrBook abook= ab.getDB(addrbook.getAb_id());//id 에 해당되는 모든 정보들을 가져온다.
		if(request.getParameter("pwd").equals("1234")){
			//pwd 누른 값이 고객의 비밀번호가 같다면 
			//수정
			request.setAttribute("ab", abook);//다시 getDB를 가져올 필요없이 기존 데이터를 ab 객체에 넣었다.
			pageContext.forward("addrbook_edit_form.jsp");//수정할 수 있는 페이지로 넘어감
		}
		else{//일치하지 않다면 비밀번호 오류
			out.println("<script>alert('비밀번호가틀렸습니다!');history.go(-1);</script>");//경고창을 띄우고 이전으로 돌아가는 스크립트 추가
		}//alert 에는 "" 말고 ''로
	}
	
	else if(action.equals("cedit")) {//customer 를 위한 edit 
		AddrBook abook= ab.getDB(addrbook.getAb_id());//id 에 해당되는 모든 정보들을 가져온다.
		request.setAttribute("ab",abook);
		pageContext.forward("caddrbook_edit_form.jsp");
	}
	
	// 주소록 수정 등록 요청인 경우
	else if(action.equals("update")) {
		//AddrBean 에 있는 updateDB 메소드 호출 리턴값은 불린
		if(ab.updateDB(addrbook)){//수정이 성공됐다면
			response.sendRedirect("addrbook_control.jsp?action=list");
		}
		else{
			throw new Exception("DB UPDATE 오류");
		}
	}
	
	else if(action.equals("cupdate")) {
		//AddrBean 에 있는 updateDB 메소드 호출 리턴값은 불린
		if(ab.updateDB(addrbook)){//수정이 성공됐다면 고객홈페이지로 이동
			response.sendRedirect("control.jsp?action=clist");
		}
		else{
			throw new Exception("DB UPDATE 오류");
		}
	}
	
	// 주소록 삭제 요청인 경우
	else if(action.equals("delete")) {
		if(ab.deleteDB(addrbook.getAb_id())){
			//삭제 성공
			response.sendRedirect("addrbook_control.jsp?action=list");
		}
	
	else {
		throw new Exception("DB UPDATE 오류");
		}
	}
	
	else if(action.equals("cdelete")){
		if(ab.deleteDB(addrbook.getAb_id())){
			//삭제가 성공되었다면
			response.sendRedirect("logout.jsp");//로그아웃 화면으로
		}
		else
		throw new Exception("DB 삭제 오류");
	}
%>