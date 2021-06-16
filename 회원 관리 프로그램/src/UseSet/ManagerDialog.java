package UseSet;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


public class ManagerDialog extends JDialog {
	private JTable mTable;

	public ManagerDialog(JFrame f){
		super(f, "사용자 관리",true);
		buildGUI();
		setLocation(150,200);
		pack();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	private void buildGUI() {
		JPanel p = new JPanel(new BorderLayout());
		File f = new File(LoginDialog.path1);  
		File[] fileList = f.listFiles();
		String[] subFId = new String[fileList.length];
		BufferedReader br = null; 
		
		for (int i = 0; i < fileList.length; i++) {
			File subF = fileList[i];
			subFId[i]=subF.getName();
		}
		
		Vector<String> vid = new Vector<String>();//이름 저장
		for (int i = 0; i < subFId.length; i++) {
			if(subFId[i].contains("Category.txt")||subFId[i].contains("manager.txt") || subFId[i].contains(".xlsx") || subFId[i].contains("rem_info.txt"))
				continue;
			else{
				vid.add(subFId[i]);
			}
		}

		Vector<String> vpwd = new Vector<String>();//패스워드 저장
		for (int i = 0; i < vid.size(); i++) {
			String ID = vid.get(i);
			try {
				br = new BufferedReader(new FileReader(f+"\\"+ID));
				String line;  
				while((line = br.readLine())!= null) {
					vpwd.add(line);
				}
				br.close();
			}
			catch (Exception e1) {}
		}
	
		//파일 불러들이기 끝
		
		String[] columnNames= {"ID","PWD"};
		Object[][] data = new Object[vid.size()][2];

		for (int i = 0; i < vid.size(); i++) {
			String id = vid.get(i);
			String newid = id.replace(".txt", "");
			data[i][0]=(Object)newid;
			data[i][1]=(Object)vpwd.get(i);
		}
		
		mTable = new JTable(data,columnNames);//인자를 통해 테이블의 행과 열을 지정
		p.add(new JScrollPane(mTable));//new JScrollPane 안 하면 columnNames 출력 x

		mTable.setRowHeight(30);//행의 높이를 30px 정도로 지정

		add(p);
	}

}
