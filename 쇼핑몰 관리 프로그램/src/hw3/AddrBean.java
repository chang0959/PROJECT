package jspbook.hw3;

import java.sql.*;



import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import jspbook.hw3.AddrBook;

/**
 * File : AddrBean.java
 * Desc : 주소록 프로그램 DAO 클래스
 * @author 황희정(dinfree@dinfree.com)
 */
public class AddrBean { 
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
	
	// 수정된 주소록 내용 갱신을 위한 메서드
		public boolean updateDB(AddrBook mymall) {
			connect();
			
			String sql ="update mymall set ab_title=?, ab_clothes=?, ab_manu=?, ab_price=?, ab_tele=?, ab_me=? where ab_id=?";		
			 //? 는 setString 으로 값을 넣어준다.
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,mymall.getAb_title());
				pstmt.setString(2,mymall.getAb_clothes());
				pstmt.setString(3,mymall.getAb_manu());
				pstmt.setString(4,mymall.getAb_price());
				pstmt.setString(5,mymall.getAb_tele());
				pstmt.setString(6,mymall.getAb_me());
				pstmt.setInt(7,mymall.getAb_id());
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
		
		
		
		// 특정 주소록 게시글 삭제 메서드
		public boolean deleteDB(int gb_id) {//아이디 값을 전달받음
			connect();
			
			String sql ="delete from mymall where ab_id=?"; //id가 어떤 값인 데이터를 가지고 작업
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,gb_id);//아이디가 int 이므로 setInt
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
		
		// 신규 주소록 메시지 추가 메서드
		public boolean insertDB(AddrBook mymall) {
			connect();
			// sql 문자열 , gb_id 는 자동 등록 되므로 입력하지 않는다.
					
			String sql ="insert into mymall(ab_title,ab_clothes,ab_manu,ab_price,ab_tele,ab_me) values(?,?,?,?,?,?)";
			//insert 할 때 아이디는 빠진 상태로 작업
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,mymall.getAb_title());
				pstmt.setString(2,mymall.getAb_clothes());
				pstmt.setString(3, mymall.getAb_manu());
				pstmt.setString(4,mymall.getAb_price());
				pstmt.setString(5,mymall.getAb_tele());
				pstmt.setString(6,mymall.getAb_me());
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

		// 특정 주소록 게시글 가져오는 메서드
		public AddrBook getDB(int gb_id) {//특정한 아이디의 레코드 값을 가져온다 return 값이 AddrBook 클래스를 리턴한다.
			
			connect();
			
			String sql = "select * from mymall where ab_id=?";//하나의 레코드를 받을 수 있는 문장을 선언
			AddrBook mymall = new AddrBook();// 그 값은 addrbook 에 넣어줌 
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,gb_id);
				ResultSet rs = pstmt.executeQuery();//select 인 경우에는 upDate 가 아니라 query 사용
				
				// 데이터가 하나만 있으므로 rs.next()를 한번만 실행 한다.
				rs.next();
				mymall.setAb_id(rs.getInt("ab_id"));
				mymall.setAb_title(rs.getString("ab_title"));
				mymall.setAb_clothes(rs.getString("ab_clothes"));
				mymall.setAb_manu(rs.getString("ab_manu"));
				mymall.setAb_price(rs.getString("ab_price"));
				mymall.setAb_tele(rs.getString("ab_tele"));
				mymall.setAb_me(rs.getString("ab_me"));
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				disconnect();
			}
			return mymall;//새로 만든 addrbook 클래스를 리턴한다.
			
		}
		
		// 전체 주소록 목록을 가져오는 메서드
		public ArrayList<AddrBook> getDBList() {//특정한 아이디가 아니라서 매개변수가 없다.
			connect();
			ArrayList<AddrBook> datas = new ArrayList<AddrBook>(); //각각의 addrbook 마다 arraylist 로 담아서 출력할 것
			
			String sql = "select * from mymall order by ab_id desc";
			//ab_id 아이디값 순서대로 descending 으로 가져오라는 뜻인 mySQL 의 명령어
			
			try {
				pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					AddrBook mymall = new AddrBook();
					//반복문 한 번마다 하나의 addrbook 클래스를 만들고 리스트에 추가
					
					mymall.setAb_id(rs.getInt("ab_id"));
					mymall.setAb_title(rs.getString("ab_title"));
					mymall.setAb_clothes(rs.getString("ab_clothes"));
					mymall.setAb_manu(rs.getString("ab_manu"));
					mymall.setAb_price(rs.getString("ab_price"));
					mymall.setAb_tele(rs.getString("ab_tele"));
					mymall.setAb_me(rs.getString("ab_me"));
					datas.add(mymall); 
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