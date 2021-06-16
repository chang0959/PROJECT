package jspbook.addrbook;

/**
 * File : AddrBook.java
 * Desc : 주소록 프로그램 DO 클래스
 * @author 황희정(dinfree@dinfree.com)
 */
public class AddrBook {//하나의 레코드,하나의 가로 행에 대한 데이터를 처리해주는 자바 파일
	// 멤버변수 선언
	private String ab_id;
	private String ab_pwd;
	private String ab_name;
	private String ab_email; 
	private String ab_tel;
	private String ab_birth;
	private String ab_comdept;
	private String ab_memo;

	public String getAb_id() {
		return ab_id;
	}
	public void setAb_id(String ab_id) {
		this.ab_id = ab_id;
	}
	public String getAb_pwd() {
		return ab_pwd;
	}
	public void setAb_pwd(String ab_pwd) {
		this.ab_pwd = ab_pwd;
	}
	public String getAb_name() {
		return ab_name;
	}
	public void setAb_name(String ab_name) {
		this.ab_name = ab_name;
	}
	public String getAb_email() {
		return ab_email;
	}
	public void setAb_email(String ab_email) {
		this.ab_email = ab_email;
	}
	public String getAb_tel() {
		return ab_tel;
	}
	public void setAb_tel(String ab_tel) {
		this.ab_tel = ab_tel;
	}
	public String getAb_birth() {
		return ab_birth;
	}
	public void setAb_birth(String ab_birth) {
		this.ab_birth = ab_birth;
	}
	public String getAb_comdept() {
		return ab_comdept;
	}
	public void setAb_comdept(String ab_comdept) {
		this.ab_comdept = ab_comdept;
	}
	public String getAb_memo() {
		return ab_memo;
	}
	public void setAb_memo(String ab_memo) {
		this.ab_memo = ab_memo;
	}
	
	
}
