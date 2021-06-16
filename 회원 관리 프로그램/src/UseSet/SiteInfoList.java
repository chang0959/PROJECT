package UseSet;
import java.util.HashSet;
import java.util.Iterator;

// 여러 계정에 대한 정보 유지
public class SiteInfoList {
	private HashSet<SiteInfo> infos;

	SiteInfoList(){
		infos = new HashSet<SiteInfo>();
	}
	
	public int size() {
		return infos.size();
	}
	
	// HashSet에 Site정보 추가
	public void addSiteInfo(SiteInfo si) {
		if(infos.size()!=0) {
			Iterator<SiteInfo> it = infos.iterator();
			boolean checkRemove = false;
			SiteInfo sif = null;
			
			while(it.hasNext()) {
				sif = it.next(); // Site정보 하나씩 꺼내옴
				
				if(si.hashCode()==sif.hashCode()) {
					checkRemove = true;
					break;
				}
			}
			
			if(checkRemove) { // 동일한 URL이 있을 경우,
				System.out.println("이전 정보>> "+sif.url+"의 주소: "+sif.hashCode());
				infos.remove(sif);
				infos.add(si); // 같은 URL일 경우 수정
				System.out.println("새 정보>> "+si.url+"의 주소: "+si.hashCode());
				System.out.println(si.url+"가 이미 존재하여 수정합니다.");
			}
			else { // 동일 URL 없을 경우,
				System.out.println("추가 정보>> "+si.url+"의 주소: "+si.hashCode());
				infos.add(si);
			}
		}
		else {
			infos.add(si);
			System.out.println("추가 정보>> "+si.url+"의 주소: "+si.hashCode());
		}
		showAllSiteInfo();
	}
	
	// 매개변수로 받은 url에 해당하는 사이트 정보 삭제
	public boolean removeSiteInto(String sUrl) {
		Iterator<SiteInfo> it = infos.iterator();
		
		while(it.hasNext()) {
			SiteInfo sif = it.next(); // Site정보 하나씩 꺼내옴
			
			String tmpUrl = sif.url; // URL 비교
			if(sUrl.equals(tmpUrl)) {
				infos.remove(sif); // 같은 URL일 경우 삭제
				return true;
			}
		}
		System.out.println(sUrl+"가 존재하지 않습니다.");
		return false;
	}
	
	// 모든 회원 출력
	public void showAllSiteInfo() {
		for(SiteInfo si:infos) {
			System.out.println(si);
		}
		System.out.println();
	}
	
	public SiteInfo getSiteInfo(int n) {
		Iterator<SiteInfo> it = infos.iterator();
		
		int i=0;
		while(it.hasNext()) {
			SiteInfo sif = it.next(); // Site정보 하나씩 꺼내옴
			
			if(i==n)
				return sif;
			i++;
		}
		return null;
	}
	
	public boolean checkSiteInfo(SiteInfo si) { // 동일 URL 존재 확인 -> true=존재
		Iterator<SiteInfo> it = infos.iterator();
		boolean checkRemove = false;
		SiteInfo sif = null;
		
		while(it.hasNext()) {
			sif = it.next(); // Site정보 하나씩 꺼내옴
			
			if(si.hashCode()==sif.hashCode()) {
				checkRemove = true;
				break;
			}
		}
		return checkRemove;
	}
}
