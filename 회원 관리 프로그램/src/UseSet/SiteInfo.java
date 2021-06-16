package UseSet;

public class SiteInfo {
	String site, url, id, pwd, sitecate, memo, prefer;
	SiteInfo(){}
	SiteInfo(String s, String u, String i, String p,
			String ss, String pp, String m){
		site = s; url = u; id=i; pwd=p;
		sitecate=ss; prefer=pp; memo=m;
	}

	SiteInfo(String[] s){
		site = s[0]; url = s[1]; id=s[2]; pwd=s[3];
		sitecate=s[4]; prefer=s[5]; memo=s[6];
	}
	public String getSite() {
		return site;
	}
	public int getPrefer() {
		if(prefer.equals("�١١١١�"))
			return 50;
		else if(prefer.equals("�١١١�"))
			return 40;
		else if(prefer.equals("�١١�"))
			return 30;
		else if(prefer.equals("�١�"))
			return 20;
		else if(prefer.equals("��"))
			return 10;
		else 
			return 0;
	}
	public void setSite(String site) {
		this.site = site;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setSitecate(String sitecate) {
		this.sitecate = sitecate;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setPrefer(String prefer) {
		this.prefer = prefer;
	}
	public void setSiteInfo(String s, String u, String i, String p, String ss, String m, String pp){
		site = s; url = u; id=i; pwd=p;
		sitecate=ss; memo=m; prefer=pp;
	}

	public String toString() {
		return "����Ʈ: "+site+"\tID:"+id;
	}

	@Override
	public int hashCode() {
		return url.hashCode(); // url�� ������ ������ hashcode ����
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SiteInfo) {
			SiteInfo si = (SiteInfo) obj;
			if(this.url == si.url) return true; // �Ű������� ���� ������ url�� �ڽ��� ����Ʈ ���� url�� ���ٸ� true 
			else return false;
		}
		return false;
	}
}
