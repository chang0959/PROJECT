<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"  errorPage="addrbook_error.jsp" import="jspbook.hw3.*, java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<% request.setCharacterEncoding("utf-8"); %>


<jsp:useBean id="pd" class="jspbook.hw3.OrderBean"/>
<jsp:useBean id="order" class="jspbook.hw3.Order"/>
<jsp:setProperty name="order" property="*"/>

<% 
	// 컨트롤러 요청 파라미터
	//<input type="hidden" name="action" value="insert"> 에 해당
	String action = request.getParameter("action");//어떠한 컨트롤을 전달했는지
	// 파라미터에 따른 요청 처리
	// 주소록 목록 요청인 경우
	//5개의 액션에 대해서 
	
	if(action.equals("clist")) {
		String aid = request.getParameter("aid");//특정한 아이디
		session.setAttribute("cid", request.getParameter("aid"));
		ArrayList<Order> datas = pd.getDBList(aid);
		request.setAttribute("datas", datas);
		pageContext.forward("buylist.jsp");//고객 구매 목록 나열 jsp 파일
	}
	
	if(action.equals("mlist")) {
		String aid = request.getParameter("aid");//특정한 아이디
		session.setAttribute("cid", request.getParameter("aid"));
		ArrayList<Order> datas = pd.getDBList(aid);
		request.setAttribute("datas", datas);
		pageContext.forward("managerbuylist.jsp");//고객 구매 목록 나열 jsp 파일
	}
	
	else if(action.equals("list")) {
		ArrayList<Order> datas1 = pd.getDBList();
		request.setAttribute("datas1", datas1);
		pageContext.forward("totallist.jsp");
	}
	
	 
	// 주소록 등록 요청인 경우
	else if(action.equals("insert")) {		
		if(pd.insertDB(order)){
			response.sendRedirect("control.jsp?action=list");
		}
		else {//실패
			throw new Exception("DB 입력 오류");
		}
	}
	
	else if(action.equals("cinsert")) {  
			if(pd.insertDB(order)) {
				String aid = order.getAb_id();  
				response.sendRedirect("order_control.jsp?action=clist&aid="+aid);
			}
		else
		throw new Exception("DB 입력오류");
		}
	
	else if(action.equals("cupdate")) {
		if(pd.updateDB(order)){
			String aid = order.getAb_id();
			response.sendRedirect("order_control.jsp?action=clist&aid="+aid);
		}
		else{
			throw new Exception("DB UPDATE 오류");
		}
	}
	
	else {
		throw new Exception("DB UPDATE 오류");
		}
%>
