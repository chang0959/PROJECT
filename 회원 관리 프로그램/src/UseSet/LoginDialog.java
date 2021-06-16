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
		super(f, "사용자 로그인",true);
		//매니저 파일 만들기 
		File mana_file = new File(path+"manager.txt");
		File categoryfile = new File(path+"managerCategory.txt");
		if(!mana_file.exists() || !categoryfile.exists()) {//파일 없으면 파일 만들기
			BufferedWriter bw1 = null;
			BufferedWriter bw2 = null;
			try {
				bw1 = new BufferedWriter(new FileWriter(mana_file)); 
				bw2 = new BufferedWriter(new FileWriter(categoryfile)); 
				bw1.write("java02");//비번은 자바 02
				bw1.flush();
				bw1.close();

				bw2.write("일반\r\n");
				bw2.write("학교\r\n");
				bw2.write("포탈\r\n");
				bw2.flush(); 
				bw2.close();
			}catch(Exception e1) {}
		}

		buildGUI();
		setLocation(150,200);
		pack();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);//닫기를 무효화
		WindowListener wListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				sureClosing();
			}
		}; 
		addWindowListener(wListener);
	} 

	private void sureClosing() {//종료 처리
		int n = JOptionPane.showConfirmDialog(null, "정말 종료하시겠습니까?","종료",JOptionPane.OK_CANCEL_OPTION);//확인,취소
		if(n==JOptionPane.OK_OPTION)
			System.exit(0);
	}

	private void buildGUI() {
		idText = new JTextField(10);
		pwdText = new JPasswordField(10);
		btn_login= new JButton("로  그  인");
		btn_exit= new JButton("종료");
		btn_signin= new JButton("회원가입");
		btn_remove = new JButton("탈퇴");
		btn_edit= new JButton("정보수정");

		JPanel idP= new JPanel();
		JPanel pwdP= new JPanel();
		JPanel btnP= new JPanel();
		JPanel id_pwdP= new JPanel(new GridLayout(2,0));

		idP.add(new JLabel("아  이  디"));
		idP.add(idText);
		idP.add(btn_signin);

		pwdP.add(new JLabel("비밀번호"));
		pwdP.add(pwdText);
		pwdP.add(btn_login);

		id_pwdP.add(idP);
		id_pwdP.add(pwdP);

		btnP.add(btn_edit);
		btnP.add(btn_remove);
		btnP.add(btn_exit);

		JPanel logP = new JPanel(new BorderLayout());
		logP.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),"안녕하세요?"));
		logP.add(id_pwdP);
		logP.add(btnP,BorderLayout.SOUTH);

		add(logP);//대화상자에 보여지도록

		btn_edit.addActionListener(new ActionListener() {//정보 수정 누르면
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String id = idText.getText();
				String pwd = new String(pwdText.getPassword()); 
				if(id.equals("") || pwd.equals("")) {
					JOptionPane.showMessageDialog(LoginDialog.this,"아이디와 패스워드를 입력하세요","로그인",JOptionPane.INFORMATION_MESSAGE);
				} 
				else { 
					String isSuccess = LoginSuccess(path+id+".txt", pwd);  
					if(isSuccess.equals("로그인 성공")) {  
						M_infoDialog m_Dialog = new M_infoDialog(SiteInfoManager.frame,id);
						m_Dialog.setVisible(true);
						idText.setText(""); 
						pwdText.setText("");
					} 
					else if(isSuccess.equals("로그인 실패")){
						JOptionPane.showMessageDialog(LoginDialog.this,"로그인이 실패하였습니다. 다시 입력하세요","정보 수정 실패",JOptionPane.INFORMATION_MESSAGE);
						idText.setText(""); 
						pwdText.setText("");
					}
					else if(isSuccess.equals("파일 없음")){//파일 x
						JOptionPane.showMessageDialog(LoginDialog.this,"아이디가 존재하지 않습니다. 다시 입력하세요","정보 수정 실패",JOptionPane.INFORMATION_MESSAGE);
						idText.setText(""); 
						pwdText.setText("");
					}
				}
			}
		});

		btn_exit.addActionListener(new ActionListener() {//종료를 누를 경우
			@Override
			public void actionPerformed(ActionEvent e) {
				sureClosing();
			}
		});

		btn_login.addActionListener(new ActionListener() {//로그인을 누르면 
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = idText.getText();
				String pwd = new String(pwdText.getPassword());
				if(id.equals("") || pwd.equals("")) {
					JOptionPane.showMessageDialog(LoginDialog.this,"아이디와 패스워드를 입력하세요","로그인",JOptionPane.INFORMATION_MESSAGE);
				} 
				else { 
					String isSuccess = LoginSuccess(path+id+".txt", pwd);  
					if(isSuccess.equals("로그인 성공")) {
						scl.setCategory(path, id);
						setVisible(false);//대화상자 없앰
					}   
					else if(isSuccess.equals("로그인 실패")){
						JOptionPane.showMessageDialog(LoginDialog.this,"로그인이 실패하였습니다. 다시 입력하세요","로그인 실패",JOptionPane.INFORMATION_MESSAGE);
						idText.setText(""); 
						pwdText.setText("");
					}
					else if(isSuccess.equals("파일 없음")){//파일 x
						JOptionPane.showMessageDialog(LoginDialog.this,"아이디가 존재하지 않습니다. 다시 입력하세요","로그인 실패",JOptionPane.INFORMATION_MESSAGE);
						idText.setText(""); 
						pwdText.setText("");
					}
				}
			}
		});


		btn_signin.addActionListener(new ActionListener() {//회원가입 버튼을 누르면
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = idText.getText();
				String pwd = new String(pwdText.getPassword()); 
				if(id.equals("") || pwd.equals("")) {
					JOptionPane.showMessageDialog(LoginDialog.this,"아이디와 패스워드를 입력하세요","회원가입",JOptionPane.INFORMATION_MESSAGE);
				} 

				else{//무언가 썼다면
					File file = new File(path+id+".txt");
					File categoryfile = new File(path+id+"Category.txt");
					if(file.exists()) {//파일이 존재하면
						JOptionPane.showMessageDialog(LoginDialog.this,"아이디가 존재합니다. 다시 입력하세요.","회원가입",JOptionPane.INFORMATION_MESSAGE);
						idText.setText("");
						pwdText.setText("");
					}
					else {//파일이 없다면
						try {
							FileWriter fw = new FileWriter(file, true);//id 파일 생성
							FileWriter cf = new FileWriter(categoryfile, true);//idcategory 파일 생성
							fw.write(pwd);//pwd 내용을 저장 
							fw.flush(); 
							fw.close();

							cf.write("일반\r\n");
							cf.write("학교\r\n");
							cf.write("포탈\r\n");
							cf.flush(); 
							cf.close();
							System.out.println("회원가입과 카테고리 파일 생성");
							JOptionPane.showMessageDialog(LoginDialog.this,"축하합니다! 회원가입 되었습니다!","회원가입",JOptionPane.INFORMATION_MESSAGE);
							idText.setText("");
							pwdText.setText("");
						} 
						catch (Exception e1) {
							System.out.println("파일 저장 오류");
						}
					}

				}
			}
		});

		btn_remove.addActionListener(new ActionListener() {//탈퇴버튼을 클릭하면 
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = idText.getText(); 
				String pwd = new String(pwdText.getPassword()); 
				if(id.equals("") || pwd.equals("")) {
					JOptionPane.showMessageDialog(LoginDialog.this,"아이디와 패스워드를 입력하세요","탈퇴 불가",JOptionPane.INFORMATION_MESSAGE);
				} 
				else {
					String loginsuccess = LoginSuccess(LoginDialog.path+id+".txt",pwd);
					if(loginsuccess.equals("로그인 성공")) { 
						//파일 지우기
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
						System.out.println("아이디와 카테고리 파일 삭제"); 
						JOptionPane.showMessageDialog(LoginDialog.this,"탈퇴하였습니다. 감사합니다.","탈퇴",JOptionPane.INFORMATION_MESSAGE);
						idText.setText("");
						pwdText.setText("");
					}
					else if(loginsuccess.equals("로그인 실패")){
						JOptionPane.showMessageDialog(LoginDialog.this,"로그인 실패입니다. 다시 입력하세요.","탈퇴 불가",JOptionPane.INFORMATION_MESSAGE);
						idText.setText("");
						pwdText.setText(""); 
					}
					else if(loginsuccess.equals("파일 없음")){
						JOptionPane.showMessageDialog(LoginDialog.this,"아이디가 없습니다. 다시 입력하세요.","탈퇴 불가",JOptionPane.INFORMATION_MESSAGE);
						idText.setText("");
						pwdText.setText(""); 
					}
				}
			}
		});

	}

	public String LoginSuccess(String fileId, String filepwd) {//로그인 성공 여부 반환 함수
		File file = new File(fileId);
		if(file.exists()) {//파일이 존재한다면
			try {
				Scanner s= new Scanner(file); 
				while(s.hasNextLine()) {
					String file_pwd= s.nextLine();//파일 안의 pwd
					if(file_pwd.equals(filepwd)) {//로그인 성공! 
						return "로그인 성공";
					} 
					else{//로그인 실패
						return "로그인 실패";
					} 
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else {//파일이 존재하지 않을 때
			return "파일 없음";
		}
		return null;
	}

}
