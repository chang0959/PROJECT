package jspbook.hw3;

public class loginBean {
	private String userid;
	private String passwd;
	final String _userid = "myuser";
	final String _passwd = "1234";
	
	public boolean checkUser() {
		if(userid.equals(_userid) && passwd.equals(_passwd))
			return true;
		return false;
	}
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
}
