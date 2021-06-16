package jspbook.addrbook;

import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * File : AddrBean.java
 * Desc : 주소록 프로그램 DAO 클래스
 * @author 황희정(dinfree@dinfree.com)
 */
public class AddrBean { 
	//데이터 베이스와 연동하는 역할을 하는 AddrBean
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	
	/* Oracle 연결정보(오라클 db를 쓸 때 우리는 MySQL 을 쓴다.)
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
	public boolean updateDB(AddrBook addrbook) {
		connect();
		
		String sql ="update addrbook set ab_name=?, ab_email=?, ab_birth=?, ab_tel=?, ab_comdept=?, ab_memo=?,ab_pwd=? where ab_id=?";		
		 //? 는 setString 으로 값을 넣어준다.
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,addrbook.getAb_name());//addrbook 에서 getter 함수를 이용해서 주소록에 있는 데이터를 가져온다.
			pstmt.setString(2,addrbook.getAb_email());
			pstmt.setString(3, addrbook.getAb_birth());
			pstmt.setString(4,addrbook.getAb_tel());
			pstmt.setString(5,addrbook.getAb_comdept());
			pstmt.setString(6,addrbook.getAb_memo());
			pstmt.setString(7,addrbook.getAb_pwd());
			pstmt.setString(8,addrbook.getAb_id());
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
	public boolean deleteDB(String gb_id) {//아이디 값을 전달받음
		connect();
		
		String sql ="delete from addrbook where ab_id=?"; //id가 어떤 값인 데이터를 가지고 작업
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,gb_id);//아이디가 int 이므로 setInt
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
	public boolean insertDB(AddrBook addrbook) {//전달받은 매개변수는 addrbook 이라는 레코드
		connect();
		// sql 문자열 , gb_id 는 자동 등록 되므로 입력하지 않는다.
				
		String sql ="insert into addrbook(ab_name,ab_email,ab_birth,ab_tel,ab_comdept,ab_memo,ab_id,ab_pwd) values(?,?,?,?,?,?,?,?)";
		//insert 할 때 아이디는 빠진 상태로 작업
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,addrbook.getAb_name());
			pstmt.setString(2,addrbook.getAb_email());
			pstmt.setString(3, addrbook.getAb_birth());
			pstmt.setString(4,addrbook.getAb_tel());
			pstmt.setString(5,addrbook.getAb_comdept());
			pstmt.setString(6,addrbook.getAb_memo());
			pstmt.setString(7,addrbook.getAb_id());
			pstmt.setString(8,addrbook.getAb_pwd());
			
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
	public AddrBook getDB(String ab_id) {//특정한 아이디의 레코드 값을 가져온다 return 값이 AddrBook 클래스를 리턴한다.
		
		connect();
		
		String sql = "select * from addrbook where ab_id=?";//하나의 레코드를 받을 수 있는 문장을 선언
		AddrBook addrbook = new AddrBook();// 그 값은 addrbook 에 넣어줌 
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,ab_id);
			ResultSet rs = pstmt.executeQuery();//select 인 경우에는 upDate 가 아니라 query 사용
			
			// 데이터가 하나만 있으므로 rs.next()를 한번만 실행 한다.
			rs.next();
			addrbook.setAb_id(rs.getString("ab_id"));
			addrbook.setAb_name(rs.getString("ab_name"));
			addrbook.setAb_email(rs.getString("ab_email"));
			addrbook.setAb_birth(rs.getString("ab_birth"));
			addrbook.setAb_tel(rs.getString("ab_tel"));
			addrbook.setAb_comdept(rs.getString("ab_comdept"));
			addrbook.setAb_memo(rs.getString("ab_memo"));
			addrbook.setAb_pwd(rs.getString("ab_pwd"));
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			disconnect();
		}
		return addrbook;//새로 만든 addrbook 클래스를 리턴한다.
		
	}
	
	// 전체 주소록 목록을 가져오는 메서드
	public ArrayList<AddrBook> getDBList() {//특정한 아이디가 아니라서 매개변수가 없다.
		connect();
		ArrayList<AddrBook> datas = new ArrayList<AddrBook>(); //각각의 addrbook 마다 arraylist 로 담아서 출력할 것
		
		String sql = "select * from addrbook order by ab_id desc";
		//ab_id 아이디값 순서대로 descending 으로 가져오라는 뜻인 mySQL 의 명령어
		
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				AddrBook addrbook = new AddrBook();
				//반복문 한 번마다 하나의 addrbook 클래스를 만들고 리스트에 추가
				
				addrbook.setAb_id(rs.getString("ab_id"));
				addrbook.setAb_pwd(rs.getString("ab_pwd"));
				addrbook.setAb_name(rs.getString("ab_name"));
				addrbook.setAb_email(rs.getString("ab_email"));
				addrbook.setAb_comdept(rs.getString("ab_comdept"));
				addrbook.setAb_birth(rs.getString("ab_birth"));
				addrbook.setAb_tel(rs.getString("ab_tel"));
				addrbook.setAb_memo(rs.getString("ab_memo"));
				datas.add(addrbook);
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
	
	public boolean checkUser(String ab_id, String ab_pwd) {  //id 와 비번이 맞는지 확인하는 메소드
		connect();
	String sql = "select * from addrbook where ab_id=?";  
	try {
	pstmt = conn.prepareStatement(sql);  
	pstmt.setString(1,ab_id);
	ResultSet rs = pstmt.executeQuery();
	
	rs.next();// 데이터가 하나만 있으므로 rs.next()를 한번만 실행한다.
	
	if(ab_id.equals(rs.getString("ab_id"))&&ab_pwd.equals(rs.getString  ("ab_pwd"))){
		//로그인이 성공한다면 
		rs.close();  
		//리턴하기 전에 반환 꼭 필요!
		return true;
		}
		rs.close();
	} 
	catch (SQLException e) {
		e.printStackTrace();
	}  
	finally {
		disconnect();
	}
	return false;
	}

}