package jspbook.hw3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class PrdBean {
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
		public Product getDB(int ab_id) {//특정한 아이디의 레코드 값을 가져온다 return 값이 AddrBook 클래스를 리턴한다.
			connect();
			
			String sql = "select * from mymall where ab_id=?";//하나의 레코드를 받을 수 있는 문장을 선언
			Product mymall = new Product();// 그 값은 addrbook 에 넣어줌 
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,ab_id);
				ResultSet rs = pstmt.executeQuery();//select 인 경우에는 upDate 가 아니라 query 사용
				
				// 데이터가 하나만 있으므로 rs.next()를 한번만 실행 한다.
				rs.next();
				mymall.setPd_id(rs.getInt("ab_id"));
				mymall.setPd_name(rs.getString("ab_title"));
				mymall.setPd_price(rs.getString("ab_price"));
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				disconnect();
			}
			return mymall;//새로 만든 addrbook 클래스를 리턴한다.
			
		}
	}