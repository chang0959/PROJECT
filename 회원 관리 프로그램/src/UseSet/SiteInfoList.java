package UseSet;
import java.util.HashSet;
import java.util.Iterator;

// ���� ������ ���� ���� ����
public class SiteInfoList {
	private HashSet<SiteInfo> infos;

	SiteInfoList(){
		infos = new HashSet<SiteInfo>();
	}
	
	public int size() {
		return infos.size();
	}
	
	// HashSet�� Site���� �߰�
	public void addSiteInfo(SiteInfo si) {
		if(infos.size()!=0) {
			Iterator<SiteInfo> it = infos.iterator();
			boolean checkRemove = false;
			SiteInfo sif = null;
			
			while(it.hasNext()) {
				sif = it.next(); // Site���� �ϳ��� ������
				
				if(si.hashCode()==sif.hashCode()) {
					checkRemove = true;
					break;
				}
			}
			
			if(checkRemove) { // ������ URL�� ���� ���,
				System.out.println("���� ����>> "+sif.url+"�� �ּ�: "+sif.hashCode());
				infos.remove(sif);
				infos.add(si); // ���� URL�� ��� ����
				System.out.println("�� ����>> "+si.url+"�� �ּ�: "+si.hashCode());
				System.out.println(si.url+"�� �̹� �����Ͽ� �����մϴ�.");
			}
			else { // ���� URL ���� ���,
				System.out.println("�߰� ����>> "+si.url+"�� �ּ�: "+si.hashCode());
				infos.add(si);
			}
		}
		else {
			infos.add(si);
			System.out.println("�߰� ����>> "+si.url+"�� �ּ�: "+si.hashCode());
		}
		showAllSiteInfo();
	}
	
	// �Ű������� ���� url�� �ش��ϴ� ����Ʈ ���� ����
	public boolean removeSiteInto(String sUrl) {
		Iterator<SiteInfo> it = infos.iterator();
		
		while(it.hasNext()) {
			SiteInfo sif = it.next(); // Site���� �ϳ��� ������
			
			String tmpUrl = sif.url; // URL ��
			if(sUrl.equals(tmpUrl)) {
				infos.remove(sif); // ���� URL�� ��� ����
				return true;
			}
		}
		System.out.println(sUrl+"�� �������� �ʽ��ϴ�.");
		return false;
	}
	
	// ��� ȸ�� ���
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
			SiteInfo sif = it.next(); // Site���� �ϳ��� ������
			
			if(i==n)
				return sif;
			i++;
		}
		return null;
	}
	
	public boolean checkSiteInfo(SiteInfo si) { // ���� URL ���� Ȯ�� -> true=����
		Iterator<SiteInfo> it = infos.iterator();
		boolean checkRemove = false;
		SiteInfo sif = null;
		
		while(it.hasNext()) {
			sif = it.next(); // Site���� �ϳ��� ������
			
			if(si.hashCode()==sif.hashCode()) {
				checkRemove = true;
				break;
			}
		}
		return checkRemove;
	}
}
