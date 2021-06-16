package UseSet;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class LoginDialog extends JDialog{
	private JTextField idText;
	private JPasswordField pwdText;
	private JButton btn_login,btn_exit,btn_signin,btn_remove,btn_edit;
	SiteCategoryList scl = new SiteCategoryList();
	final static String path = "";
	final static String path1 = ".";
	
	public LoginDialog(JFrame f){ 
		super(f, "����� �α���",true);
		//�Ŵ��� ���� ����� 
		File mana_file = new File(path+"manager.txt");
		File categoryfile = new File(path+"managerCategory.txt");
		if(!mana_file.exists() || !categoryfile.exists()) {//���� ������ ���� �����
			BufferedWriter bw1 = null;
			BufferedWriter bw2 = null;
			try {
				bw1 = new BufferedWriter(new FileWriter(mana_file)); 
				bw2 = new BufferedWriter(new FileWriter(categoryfile)); 
				bw1.write("java02");//����� �ڹ� 02
				bw1.flush();
				bw1.close();

				bw2.write("�Ϲ�\r\n");
				bw2.write("�б�\r\n");
				bw2.write("��Ż\r\n");
				bw2.flush(); 
				bw2.close();
			}catch(Exception e1) {}
		}

		buildGUI();
		setLocation(150,200);
		pack();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);//�ݱ⸦ ��ȿȭ
		WindowListener wListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				sureClosing();
			}
		}; 
		addWindowListener(wListener);
	} 

	private void sureClosing() {//���� ó��
		int n = JOptionPane.showConfirmDialog(null, "���� �����Ͻðڽ��ϱ�?","����",JOptionPane.OK_CANCEL_OPTION);//Ȯ��,���
		if(n==JOptionPane.OK_OPTION)
			System.exit(0);
	}

	private void buildGUI() {
		idText = new JTextField(10);
		pwdText = new JPasswordField(10);
		btn_login= new JButton("��  ��  ��");
		btn_exit= new JButton("����");
		btn_signin= new JButton("ȸ������");
		btn_remove = new JButton("Ż��");
		btn_edit= new JButton("��������");

		JPanel idP= new JPanel();
		JPanel pwdP= new JPanel();
		JPanel btnP= new JPanel();
		JPanel id_pwdP= new JPanel(new GridLayout(2,0));

		idP.add(new JLabel("��  ��  ��"));
		idP.add(idText);
		idP.add(btn_signin);

		pwdP.add(new JLabel("��й�ȣ"));
		pwdP.add(pwdText);
		pwdP.add(btn_login);

		id_pwdP.add(idP);
		id_pwdP.add(pwdP);

		btnP.add(btn_edit);
		btnP.add(btn_remove);
		btnP.add(btn_exit);

		JPanel logP = new JPanel(new BorderLayout());
		logP.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),"�ȳ��ϼ���?"));
		logP.add(id_pwdP);
		logP.add(btnP,BorderLayout.SOUTH);

		add(logP);//��ȭ���ڿ� ����������

		btn_edit.addActionListener(new ActionListener() {//���� ���� ������
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String id = idText.getText();
				String pwd = new String(pwdText.getPassword()); 
				if(id.equals("") || pwd.equals("")) {
					JOptionPane.showMessageDialog(LoginDialog.this,"���̵�� �н����带 �Է��ϼ���","�α���",JOptionPane.INFORMATION_MESSAGE);
				} 
				else { 
					String isSuccess = LoginSuccess(path+id+".txt", pwd);  
					if(isSuccess.equals("�α��� ����")) {  
						M_infoDialog m_Dialog = new M_infoDialog(SiteInfoManager.frame,id);
						m_Dialog.setVisible(true);
						idText.setText(""); 
						pwdText.setText("");
					} 
					else if(isSuccess.equals("�α��� ����")){
						JOptionPane.showMessageDialog(LoginDialog.this,"�α����� �����Ͽ����ϴ�. �ٽ� �Է��ϼ���","���� ���� ����",JOptionPane.INFORMATION_MESSAGE);
						idText.setText(""); 
						pwdText.setText("");
					}
					else if(isSuccess.equals("���� ����")){//���� x
						JOptionPane.showMessageDialog(LoginDialog.this,"���̵� �������� �ʽ��ϴ�. �ٽ� �Է��ϼ���","���� ���� ����",JOptionPane.INFORMATION_MESSAGE);
						idText.setText(""); 
						pwdText.setText("");
					}
				}
			}
		});

		btn_exit.addActionListener(new ActionListener() {//���Ḧ ���� ���
			@Override
			public void actionPerformed(ActionEvent e) {
				sureClosing();
			}
		});

		btn_login.addActionListener(new ActionListener() {//�α����� ������ 
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = idText.getText();
				String pwd = new String(pwdText.getPassword());
				if(id.equals("") || pwd.equals("")) {
					JOptionPane.showMessageDialog(LoginDialog.this,"���̵�� �н����带 �Է��ϼ���","�α���",JOptionPane.INFORMATION_MESSAGE);
				} 
				else { 
					String isSuccess = LoginSuccess(path+id+".txt", pwd);  
					if(isSuccess.equals("�α��� ����")) {
						scl.setCategory(path, id);
						setVisible(false);//��ȭ���� ����
					}   
					else if(isSuccess.equals("�α��� ����")){
						JOptionPane.showMessageDialog(LoginDialog.this,"�α����� �����Ͽ����ϴ�. �ٽ� �Է��ϼ���","�α��� ����",JOptionPane.INFORMATION_MESSAGE);
						idText.setText(""); 
						pwdText.setText("");
					}
					else if(isSuccess.equals("���� ����")){//���� x
						JOptionPane.showMessageDialog(LoginDialog.this,"���̵� �������� �ʽ��ϴ�. �ٽ� �Է��ϼ���","�α��� ����",JOptionPane.INFORMATION_MESSAGE);
						idText.setText(""); 
						pwdText.setText("");
					}
				}
			}
		});


		btn_signin.addActionListener(new ActionListener() {//ȸ������ ��ư�� ������
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = idText.getText();
				String pwd = new String(pwdText.getPassword()); 
				if(id.equals("") || pwd.equals("")) {
					JOptionPane.showMessageDialog(LoginDialog.this,"���̵�� �н����带 �Է��ϼ���","ȸ������",JOptionPane.INFORMATION_MESSAGE);
				} 

				else{//���� ��ٸ�
					File file = new File(path+id+".txt");
					File categoryfile = new File(path+id+"Category.txt");
					if(file.exists()) {//������ �����ϸ�
						JOptionPane.showMessageDialog(LoginDialog.this,"���̵� �����մϴ�. �ٽ� �Է��ϼ���.","ȸ������",JOptionPane.INFORMATION_MESSAGE);
						idText.setText("");
						pwdText.setText("");
					}
					else {//������ ���ٸ�
						try {
							FileWriter fw = new FileWriter(file, true);//id ���� ����
							FileWriter cf = new FileWriter(categoryfile, true);//idcategory ���� ����
							fw.write(pwd);//pwd ������ ���� 
							fw.flush(); 
							fw.close();

							cf.write("�Ϲ�\r\n");
							cf.write("�б�\r\n");
							cf.write("��Ż\r\n");
							cf.flush(); 
							cf.close();
							System.out.println("ȸ�����԰� ī�װ� ���� ����");
							JOptionPane.showMessageDialog(LoginDialog.this,"�����մϴ�! ȸ������ �Ǿ����ϴ�!","ȸ������",JOptionPane.INFORMATION_MESSAGE);
							idText.setText("");
							pwdText.setText("");
						} 
						catch (Exception e1) {
							System.out.println("���� ���� ����");
						}
					}

				}
			}
		});

		btn_remove.addActionListener(new ActionListener() {//Ż���ư�� Ŭ���ϸ� 
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = idText.getText(); 
				String pwd = new String(pwdText.getPassword()); 
				if(id.equals("") || pwd.equals("")) {
					JOptionPane.showMessageDialog(LoginDialog.this,"���̵�� �н����带 �Է��ϼ���","Ż�� �Ұ�",JOptionPane.INFORMATION_MESSAGE);
				} 
				else {
					String loginsuccess = LoginSuccess(LoginDialog.path+id+".txt",pwd);
					if(loginsuccess.equals("�α��� ����")) { 
						//���� �����
						File removeFile1 = new File(LoginDialog.path+id+".txt");
						File removeFile2 = new File(LoginDialog.path+id+"Category.txt");
						File removeFile3 = new File(LoginDialog.path+id+"Excel.xlsx");
						System.gc();
						if(removeFile2.exists()) {
							removeFile2.delete();
							System.gc();
						}
						if(removeFile1.exists()) {
							removeFile1.delete();
							System.gc();
						}
						System.gc();
						if(removeFile3.exists()) {
							removeFile3.delete();
							System.gc();
						} 
						System.out.println("���̵�� ī�װ� ���� ����"); 
						JOptionPane.showMessageDialog(LoginDialog.this,"Ż���Ͽ����ϴ�. �����մϴ�.","Ż��",JOptionPane.INFORMATION_MESSAGE);
						idText.setText("");
						pwdText.setText("");
					}
					else if(loginsuccess.equals("�α��� ����")){
						JOptionPane.showMessageDialog(LoginDialog.this,"�α��� �����Դϴ�. �ٽ� �Է��ϼ���.","Ż�� �Ұ�",JOptionPane.INFORMATION_MESSAGE);
						idText.setText("");
						pwdText.setText(""); 
					}
					else if(loginsuccess.equals("���� ����")){
						JOptionPane.showMessageDialog(LoginDialog.this,"���̵� �����ϴ�. �ٽ� �Է��ϼ���.","Ż�� �Ұ�",JOptionPane.INFORMATION_MESSAGE);
						idText.setText("");
						pwdText.setText(""); 
					}
				}
			}
		});

	}

	public String LoginSuccess(String fileId, String filepwd) {//�α��� ���� ���� ��ȯ �Լ�
		File file = new File(fileId);
		if(file.exists()) {//������ �����Ѵٸ�
			try {
				Scanner s= new Scanner(file); 
				while(s.hasNextLine()) {
					String file_pwd= s.nextLine();//���� ���� pwd
					if(file_pwd.equals(filepwd)) {//�α��� ����! 
						return "�α��� ����";
					} 
					else{//�α��� ����
						return "�α��� ����";
					} 
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else {//������ �������� ���� ��
			return "���� ����";
		}
		return null;
	}

}
