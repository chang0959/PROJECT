package UseSet;
// �ϳ��� �� ����Ʈ �з��� ���� ����
public class SiteCategory {
	private String Website;
	public SiteCategory(String s){
		Website=s;
	}
	public SiteCategory() {
		// TODO Auto-generated constructor stub
	}
	
	public void setWebsite(String site) {
		this.Website = site;
	}
	
	public String toString() {//GetWebsite() �޼���
		return Website;
	}
}
