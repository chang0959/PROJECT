package UseSet;
// 하나의 웹 사이트 분류에 대한 정보
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
	
	public String toString() {//GetWebsite() 메서드
		return Website;
	}
}
