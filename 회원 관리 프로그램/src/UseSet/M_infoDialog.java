package UseSet;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class M_infoDialog extends JDialog {//�������� Ŭ����
	JButton m_btn;
	JButton e_btn;
	String current_id;
	JTextField m_id;
	JPasswordField m_pwd; 
	SiteCategoryList s_l = new SiteCategoryList();
	private JFrame frame;
	public M_infoDialog(JFrame frame, String id) {
		super(frame, "��������",true);
		this.frame=frame;
		current_id = id;
		buildGUI(); 
		setLocation(150,200);
		pack();		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	private void buildGUI() {
		setLayout(new BorderLayout());
		JPanel p = new JPanel();
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel(new GridLayout(2,0));
		m_id = new JTextField(10);
		m_pwd = new JPasswordField(10); 
		p.add(new JLabel("��  ��  ��"));
		p.add(m_id);
		p1.add(new JLabel("��й�ȣ")); 
		p1.add(m_pwd);

		p3.add(p);
		p3.add(p1);

		m_btn = new JButton("�Ϸ�");
		e_btn = new JButton("����");
		p2.add(m_btn);
		p2.add(e_btn);

		add(p3,BorderLayout.CENTER);
		add(p2,BorderLayout.SOUTH);

		m_btn.addActionListener(handler1);
		e_btn.addActionListener(handler2);
	}

	private ActionListener handler1 = new ActionListener() {
		@Override 
		public void actionPerformed(ActionEvent e) {
			String new_id =m_id.getText();
			String new_pwd =new String(m_pwd.getPassword());
			File file = new File(LoginDialog.path+new_id+".txt");//������ ���̵� ����

			if(new_id.trim().equals("") || new_pwd.trim().equals("")) {
				JOptionPane.showMessageDialog(M_infoDialog.this,"������ ���̵�� �����ȣ�� �Է��ϼ���.","���� ���� �Ұ�",JOptionPane.INFORMATION_MESSAGE);
			} 
			else {
				//���� ������ ���̵� �����Ѵٸ� 
				if(file.exists()) {
					JOptionPane.showMessageDialog(M_infoDialog.this,"���̵� �����մϴ�. �ٽ� �Է��ϼ���.","���� ���� �Ұ�",JOptionPane.INFORMATION_MESSAGE);
					m_id.setText("");
					m_pwd.setText("");
				}

				else {
					SiteCategoryList.setId(new_id);//���� ���̵� �ٲٱ� 
					String oldFileName = new String(LoginDialog.path+current_id+".txt");
					String oldFile_CateName = new String(LoginDialog.path+current_id+"Category.txt");
					String newFile_CateName = new String(LoginDialog.path+new_id+"Category.txt"); 
					String oldExcelName = new String(LoginDialog.path+current_id+"Excel.xlsx");
					String newExcelName = new String(LoginDialog.path+new_id+"Excel.xlsx");
					String tmpFileName =LoginDialog.path+new_id+".txt";     
					BufferedWriter bw = null;   

					try {
						bw = new BufferedWriter(new FileWriter(tmpFileName));
						bw.write(new_pwd);
						System.gc();  
						bw.close();  
						m_id.setText("");
						m_pwd.setText("");
						JOptionPane.showMessageDialog(M_infoDialog.this,"���� ������ �Ϸ�Ǿ����ϴ�.","���� ���� �Ϸ�",JOptionPane.INFORMATION_MESSAGE);
						setVisible(false);
						System.gc();
						File oldFile = new File(oldFileName);
						if (oldFile.exists()){
							System.gc();
							oldFile.delete();
							System.gc();
						}
						File oldFile_Cate = new File(oldFile_CateName); 
						oldFile_Cate.renameTo(new File(newFile_CateName)); 
						File oldExcel = new File(oldExcelName);//���� ���ϵ� �̸� �ٲٱ�
						if(oldExcel.exists()) {
							oldExcel.renameTo(new File(newExcelName));
						}
					}
					catch (Exception e1) {
						System.out.println("�ű� ����");
					}
				}
			}
		}
	};


	private ActionListener handler2 = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
		}
	};
}


