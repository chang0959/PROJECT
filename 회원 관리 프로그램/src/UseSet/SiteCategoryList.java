package UseSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

public class SiteCategoryList { 
   
	String catest;
	private static HashSet<SiteCategory> items;
	private static String sid; 
	
	SiteCategoryList(){
		items = new HashSet<SiteCategory>();
	}
	public void setCategory(String path,String id) {//���� ī�װ� ����
		sid=id;
		File categoryfile = new File(path+id+"Category.txt");
		if(categoryfile.exists()) {//ī�װ� ������ �����Ѵٸ�
			try {  
				//�Է� ��Ʈ�� ���� 
				FileReader filereader = new FileReader(categoryfile);
				//�Է� ���� ����
				BufferedReader bufReader = new BufferedReader(filereader);
				String line = "";
				catest = new String();
				while((line = bufReader.readLine()) != null){
					if(line.trim().length() > 0) {
						catest += line + ",";
					}
				}
				String[] splitSt = catest.split(",");
				
				
				for (int i = 0; i < splitSt.length; i++) {
					SiteCategory sc = new SiteCategory(splitSt[i]);
					//�ߺ����� Ȯ��
					if(isInfo(sc)==false) {//�ߺ��̸�
						continue;
					}
					else {
						items.add(sc);
					}
				}
				bufReader.close();
				filereader.close();
			}catch (FileNotFoundException e) {
				// TODO: handle exception
			}catch(IOException e){ 
				System.out.println(e);
			}
		}
	}

	public static HashSet<SiteCategory> getItems() {
		return items;
	}

	public static void setItems(HashSet<SiteCategory> its) {
		items = its;
	}

	static public String getId() {
		return sid;
	}

	static public void setId(String id) {
		sid = id;
		
	}
 
	static public boolean isInfo(SiteCategory scg) {
		Iterator<SiteCategory> it1 = items.iterator();
		String[] ord_scg_name = new String[items.size()];
		int index=0;
		boolean flag=false;
		
		while(it1.hasNext()) { 
			SiteCategory ord_scg = it1.next();
			ord_scg_name[index]=ord_scg.toString();
			index+=1;
		} 
		
		for (int size = 0; size < ord_scg_name.length ; size++) {
			if(ord_scg_name[size].equals(scg.toString())) {//���ٸ� true��
				flag = true;
				break;
			}
		} 
		if (flag==true)//�ߺ��̸� �����ϸ� ���� false
			return false;
		
		else
			return true;
	}
	
	static public boolean addInfo(SiteCategory scg) {//ī�װ� �����ϴ��� Ȯ�� �Լ�+add
		if(isInfo(scg)==false) {//�ߺ�
			return false;
		}
		else {
			items.add(scg);
			return true;
		}
	}
	

	static public boolean deleteInfo(SiteCategory scg) {//ī�װ� ����
		if(isInfo(scg)) {//ī�װ� ������
			return false;//ī�װ� ����
		}
		else {//scg�� ���� ī�װ��� �ִٸ� 
			items.remove(scg);
			return true;
		}
		
	}
	

	
	
	
	
	
}
