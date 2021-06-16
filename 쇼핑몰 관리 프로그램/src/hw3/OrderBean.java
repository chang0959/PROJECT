package jspbook.hw3;

import java.sql.*;


import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import jspbook.hw3.Order;

/**
 * File : AddrBean.java
 * Desc : 주소록 프로그램 DAO 클래스
 * @author 황희정(dinfree@dinfree.com)
 */
public class OrderBean { 
	Connection conn = null;
	PreparedStatement pstmt = null;
	
	/* Oracle 연결정보
	String jdbc_driver = "oracle.jdbc.driver.OracleDriver";
	String jdbc_url = "jdbc:oracle:thin:@220.68.14.7:1521";
	*/
	
	/* MySQL 연결정보 */
	String jdbc_driver = "com.mysql.cj.jdbc.Driver";
	String jdbc_url = "jdbc:mysql://127.0.0.1/jspdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"; 
	
	// DB연결 메서드
	void connect() {
		try {
			Context initContext =new InitialContext();
			Context envContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/mysql");
			conn = ds.getConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void disconnect() {
		if(pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 수정된 구매 내용 갱신을 위한 메서드
		public boolean updateDB(Order ord) {
			connect();
			
			String sql ="update myorder set ab_id=?,pd_id1=?,pd_num1=?,pd_id2=?,pd_num2=?,pd_id3=?,pd_num3=?,pd_id4=?,pd_num4=?,pd_id5=?,pd_num5=?,pd_total=? where o_id=?";

			 //? 는 setString 으로 값을 넣어준다.
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,ord.getAb_id());
				pstmt.setInt(2,ord.getPd_id1());
				pstmt.setInt(3, ord.getPd_num1());
				pstmt.setInt(4, ord.getPd_id2());
				pstmt.setInt(5, ord.getPd_num2());
				pstmt.setInt(6, ord.getPd_id3());
				pstmt.setInt(7, ord.getPd_num3());
				pstmt.setInt(8, ord.getPd_id4());
				pstmt.setInt(9, ord.getPd_num4());
				pstmt.setInt(10, ord.getPd_id5());
				pstmt.setInt(11, ord.getPd_num5());
				pstmt.setInt(12,ord.getPd_total());
				pstmt.setInt(13,ord.getO_id());
				pstmt.executeUpdate();
			

			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			finally {
				disconnect();//connect 가 되면 반드시 disconnect 필수
			}
			return true;
		}
		
		
		
		// 특정 구매 삭제 메서드
		public boolean deleteDB(int o_id) {//아이디 값을 전달받음
			connect();
			
			String sql ="delete from myorder where o_id=?";
			
			try {
				pstmt = conn.prepareStatement(sql);  
				pstmt.setInt(1,o_id);  
				pstmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			finally {
				disconnect();
			}
			return true;
		}
		
		// 신규 구매 메시지 추가 메서드
		public boolean insertDB(Order ord) {
			connect();
			// sql 문자열 , gb_id 는 자동 등록 되므로 입력하지 않는다.
					
			String sql ="insert into myorder(ab_id,pd_id1,pd_num1,pd_id2,pd_num2,pd_id3,pd_num3,pd_id4,pd_num4,  pd_id5,pd_num5,pd_total) values(?,?,?,?,?,?,?,?,?,?,?,?)";
			//insert 할 때 아이디는 빠진 상태로 작업
			try {
				pstmt = conn.prepareStatement(sql);  
				pstmt.setString(1,ord.getAb_id());  
				pstmt.setInt(2, ord.getPd_id1());  
				pstmt.setInt(3, ord.getPd_num1());
				pstmt.setInt(4, ord.getPd_id2());
				pstmt.setInt(5, ord.getPd_num2());
				pstmt.setInt(6, ord.getPd_id3());
				pstmt.setInt(7, ord.getPd_num3());
				pstmt.setInt(8, ord.getPd_id4());
				pstmt.setInt(9, ord.getPd_num4());
				pstmt.setInt(10, ord.getPd_id5());
				pstmt.setInt(11, ord.getPd_num5());
				pstmt.setInt(12, ord.getPd_total());
				pstmt.executeUpdate();

				
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			finally {
				disconnect();
			}
			return true;
		}

		// 특정 구매 가져오는 메서드
		public Order getDB(int o_id) {//특정한 아이디의 레코드 값을 가져온다 return 값이 AddrBook 클래스를 리턴한다.
			
			connect();
			
			String sql = "select * from myorder where o_id=?";
			Order ord = new Order();
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,o_id);
				ResultSet rs = pstmt.executeQuery();//select 인 경우에는 upDate 가 아니라 query 사용
				rs.next();  
				ord.setAb_id(rs.getString("ab_id"));  
				ord.setPd_id1(rs.getInt("pd_id1"));  
				ord.setPd_num1(rs.getInt("pd_num1"));    
				ord.setPd_id2(rs.getInt("pd_id2"));
				ord.setPd_num2(rs.getInt("pd_num2"));
				ord.setPd_id3(rs.getInt("pd_id3"));
				ord.setPd_num3(rs.getInt("pd_num3"));
				ord.setPd_id4(rs.getInt("pd_id4"));
				ord.setPd_num4(rs.getInt("pd_num4"));
				ord.setPd_id5(rs.getInt("pd_id5"));
				ord.setPd_num5(rs.getInt("pd_num5"));
				ord.setPd_total(rs.getInt("pd_total"));  
				rs.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				disconnect();
			}
			return ord;
			
		}
		
		// 전체 구매 목록을 가져오는 메서드 : 관리자 모드
		public ArrayList<Order> getDBList(String aid) {
			connect();
			ArrayList<Order> datas = new ArrayList<Order>();
			
			String sql = "select * from myorder where ab_id=?";  
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,aid);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					Order ord = new Order();
					ord.setO_id(rs.getInt("o_id"));
					ord.setAb_id(rs.getString("ab_id"));  
					ord.setPd_id1(rs.getInt("pd_id1"));  
					ord.setPd_num1(rs.getInt("pd_num1"));    
					ord.setPd_id2(rs.getInt("pd_id2"));
					ord.setPd_num2(rs.getInt("pd_num2"));
					ord.setPd_id3(rs.getInt("pd_id3"));
					ord.setPd_num3(rs.getInt("pd_num3"));
					ord.setPd_id4(rs.getInt("pd_id4"));
					ord.setPd_num4(rs.getInt("pd_num4"));
					ord.setPd_id5(rs.getInt("pd_id5"));
					ord.setPd_num5(rs.getInt("pd_num5"));
					ord.setPd_total(rs.getInt("pd_total"));  
					datas.add(ord); 

				}
				rs.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				disconnect();
			}
			return datas;
		}
		
		
		
		public ArrayList<Order> getDBList() {
			connect();
			ArrayList<Order> datas = new ArrayList<Order>();
			
			String sql = "select * from myorder";  
			
			try {
				pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					Order ord = new Order();
					ord.setO_id(rs.getInt("o_id"));
					ord.setAb_id(rs.getString("ab_id"));  
					ord.setPd_id1(rs.getInt("pd_id1"));  
					ord.setPd_num1(rs.getInt("pd_num1"));    
					ord.setPd_id2(rs.getInt("pd_id2"));
					ord.setPd_num2(rs.getInt("pd_num2"));
					ord.setPd_id3(rs.getInt("pd_id3"));
					ord.setPd_num3(rs.getInt("pd_num3"));
					ord.setPd_id4(rs.getInt("pd_id4"));
					ord.setPd_num4(rs.getInt("pd_num4"));
					ord.setPd_id5(rs.getInt("pd_id5"));
					ord.setPd_num5(rs.getInt("pd_num5"));
					ord.setPd_total(rs.getInt("pd_total"));  
					datas.add(ord); 
				}
				rs.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				disconnect();
			}
			return datas;
		}
		

	}