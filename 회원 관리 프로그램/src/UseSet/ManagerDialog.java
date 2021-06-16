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
		super(f, "����� ����",true);
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
		
		Vector<String> vid = new Vector<String>();//�̸� ����
		for (int i = 0; i < subFId.length; i++) {
			if(subFId[i].contains("Category.txt")||subFId[i].contains("manager.txt") || subFId[i].contains(".xlsx") || subFId[i].contains("rem_info.txt"))
				continue;
			else{
				vid.add(subFId[i]);
			}
		}

		Vector<String> vpwd = new Vector<String>();//�н����� ����
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
	
		//���� �ҷ����̱� ��
		
		String[] columnNames= {"ID","PWD"};
		Object[][] data = new Object[vid.size()][2];

		for (int i = 0; i < vid.size(); i++) {
			String id = vid.get(i);
			String newid = id.replace(".txt", "");
			data[i][0]=(Object)newid;
			data[i][1]=(Object)vpwd.get(i);
		}
		
		mTable = new JTable(data,columnNames);//���ڸ� ���� ���̺��� ��� ���� ����
		p.add(new JScrollPane(mTable));//new JScrollPane �� �ϸ� columnNames ��� x

		mTable.setRowHeight(30);//���� ���̸� 30px ������ ����

		add(p);
	}

}
