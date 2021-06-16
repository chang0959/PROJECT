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

public class M_infoDialog extends JDialog {//정보수정 클래스
	JButton m_btn;
	JButton e_btn;
	String current_id;
	JTextField m_id;
	JPasswordField m_pwd; 
	SiteCategoryList s_l = new SiteCategoryList();
	private JFrame frame;
	public M_infoDialog(JFrame frame, String id) {
		super(frame, "정보수정",true);
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
		p.add(new JLabel("아  이  디"));
		p.add(m_id);
		p1.add(new JLabel("비밀번호")); 
		p1.add(m_pwd);

		p3.add(p);
		p3.add(p1);

		m_btn = new JButton("완료");
		e_btn = new JButton("종료");
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
			File file = new File(LoginDialog.path+new_id+".txt");//수정할 아이디 파일

			if(new_id.trim().equals("") || new_pwd.trim().equals("")) {
				JOptionPane.showMessageDialog(M_infoDialog.this,"수정할 아이디와 비빌번호를 입력하세요.","정보 수정 불가",JOptionPane.INFORMATION_MESSAGE);
			} 
			else {
				//만약 수정된 아이디가 존재한다면 
				if(file.exists()) {
					JOptionPane.showMessageDialog(M_infoDialog.this,"아이디가 존재합니다. 다시 입력하세요.","정보 수정 불가",JOptionPane.INFORMATION_MESSAGE);
					m_id.setText("");
					m_pwd.setText("");
				}

				else {
					SiteCategoryList.setId(new_id);//현재 아이디 바꾸기 
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
						JOptionPane.showMessageDialog(M_infoDialog.this,"정보 수정이 완료되었습니다.","정보 수정 완료",JOptionPane.INFORMATION_MESSAGE);
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
						File oldExcel = new File(oldExcelName);//엑셀 파일도 이름 바꾸기
						if(oldExcel.exists()) {
							oldExcel.renameTo(new File(newExcelName));
						}
					}
					catch (Exception e1) {
						System.out.println("신규 오류");
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


