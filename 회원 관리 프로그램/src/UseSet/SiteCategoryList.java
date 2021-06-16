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
	public void setCategory(String path,String id) {//현재 카테고리 설정
		sid=id;
		File categoryfile = new File(path+id+"Category.txt");
		if(categoryfile.exists()) {//카테고리 파일이 존재한다면
			try {  
				//입력 스트림 생성 
				FileReader filereader = new FileReader(categoryfile);
				//입력 버퍼 생성
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
					//중복인지 확인
					if(isInfo(sc)==false) {//중복이면
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
			if(ord_scg_name[size].equals(scg.toString())) {//같다면 true로
				flag = true;
				break;
			}
		} 
		if (flag==true)//중복이면 존재하면 리턴 false
			return false;
		
		else
			return true;
	}
	
	static public boolean addInfo(SiteCategory scg) {//카테고리 포함하는지 확인 함수+add
		if(isInfo(scg)==false) {//중복
			return false;
		}
		else {
			items.add(scg);
			return true;
		}
	}
	

	static public boolean deleteInfo(SiteCategory scg) {//카테고리 삭제
		if(isInfo(scg)) {//카테고리 없으면
			return false;//카테고리 없음
		}
		else {//scg와 같은 카테고리가 있다면 
			items.remove(scg);
			return true;
		}
		
	}
	

	
	
	
	
	
}
