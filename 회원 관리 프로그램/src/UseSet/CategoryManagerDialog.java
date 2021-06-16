package UseSet;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel; 
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;



public class CategoryManagerDialog extends JDialog{

	private JTextField cate_name;
	private JButton btn_del,btn_add,btn_new,btn_exit;
	private HashSet<SiteCategory> itemsC= new HashSet<SiteCategory>();
	private JList<SiteCategory> list;
	private DefaultListModel<SiteCategory> model;

	private String id;

	public CategoryManagerDialog(JFrame f){
		super(f, "사이트 분류 관리",true);//super(Frame owner,String title,boolean b) 
		ListSet();
		buildGUI(); 
		setLocation(150,200);
		pack();
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	} 

	public void ListSet() {//리스트
		id=SiteCategoryList.getId();
		System.out.println(id); 
		itemsC= SiteCategoryList.getItems();//현재 아이디의 카테고리

		//id에 따른 카테고리 출력
		list = new JList<SiteCategory>(new DefaultListModel<SiteCategory>());
		model = (DefaultListModel<SiteCategory>)list.getModel(); 

		Iterator<SiteCategory> it1 = itemsC.iterator();
		while(it1.hasNext()) {
			model.addElement(it1.next());//해쉬셋에 있는 카테고리를 모델에 적용
		}  
		System.out.println(id+"의 카테고리");
		System.out.println(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//다중 선택 불가능하게
	}

	private void buildGUI() {
		setLayout(new FlowLayout());
		JPanel leftP = new JPanel(new BorderLayout());
		leftP.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),"등록항목"));

		leftP.add(new JScrollPane(list));
		JPanel p1 = new JPanel(new BorderLayout()); 
		p1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),"편집 내용"));
		JPanel p2 = new JPanel();//신규,완료 버튼 패널
		JPanel rightP = new JPanel(new BorderLayout());

		JPanel cate_P= new JPanel();
		cate_name= new JTextField(10);
		cate_P.add(new JLabel("항목 이름"));
		cate_P.add(cate_name);

		JPanel cate_btn_P= new JPanel();
		btn_del= new JButton("삭제(D)");
		btn_add= new JButton("추가(A)");
		btn_del.setMnemonic('d');
		btn_add.setMnemonic('a');
		cate_btn_P.add(btn_del);
		cate_btn_P.add(btn_add);

		JPanel editP = new JPanel(new GridLayout(2,0));
		editP.add(cate_P);
		editP.add(cate_btn_P);

		p1.add(editP,BorderLayout.NORTH);

		btn_new= new JButton("신규(N)");
		btn_exit= new JButton("완료(E)");
		btn_new.setMnemonic('n');
		btn_exit.setMnemonic('e');
		p2.add(btn_new);
		p2.add(btn_exit);

		rightP.add(p1,BorderLayout.CENTER);
		rightP.add(p2,BorderLayout.SOUTH);

		JPanel total = new JPanel(new GridLayout(0,2));
		total.add(leftP);
		total.add(rightP);

		add(total);

		btn_add.addActionListener(new ActionListener() {//추가를 누르면
			@Override
			public void actionPerformed(ActionEvent e) {
				String cate = cate_name.getText(); 
				if(cate.equals("")) {
					JOptionPane.showMessageDialog(CategoryManagerDialog.this,"카테고리를 입력하세요","카테고리 추가 오류",JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					//카테고리 파일에 저장
					BufferedWriter bw;
					try { 
						SiteCategory scg = new SiteCategory(cate);
						if(SiteCategoryList.addInfo(scg)==true) {//중복 안 돼서 포함
							//파일 이어쓰기
							bw = new BufferedWriter(new FileWriter(LoginDialog.path+id+"Category.txt",true));
							PrintWriter pw= new PrintWriter(bw,true);
							pw.write(cate+"\r\n");
							pw.flush(); 
							pw.close();
							model.addElement(scg);
							list.updateUI();
							cate_name.setText("");
							System.out.println(id+"파일에 "+cate+"(을)를 저장하였습니다. ");
						}
						else {//중복이라면
							JOptionPane.showMessageDialog(CategoryManagerDialog.this,"카테고리 중복입니다. 다시 입력하세요.","카테고리 추가 불가",JOptionPane.INFORMATION_MESSAGE);
							cate_name.setText("");
						}

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		btn_exit.addActionListener(new ActionListener() {//대화상자 빠져나가기
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		btn_new.addActionListener(new ActionListener() {//카테고리 수정이 가능하도록
			@Override
			public void actionPerformed(ActionEvent e) {
				SiteCategory s = (SiteCategory)list.getSelectedValue();
				if(s==null) {
					JOptionPane.showMessageDialog(CategoryManagerDialog.this,"카테고리를 선택하세요","카테고리 수정 오류",JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					if(cate_name.getText().trim().equals("")) {
						JOptionPane.showMessageDialog(CategoryManagerDialog.this,"수정할 카테고리를 입력하세요.","카테고리 수정 오류",JOptionPane.INFORMATION_MESSAGE);
						return; 
					}
					String ord_name = s.toString();
					String new_name= cate_name.getText();//수정할 카테고리
					SiteCategory newsc = new SiteCategory();
					newsc.setWebsite(new_name);
					if (new_name==null) //입력이 없다면
						JOptionPane.showMessageDialog(CategoryManagerDialog.this,"값이 없습니다. 다시 입력하세요","수정 실패",JOptionPane.INFORMATION_MESSAGE);

					else {
						if(SiteCategoryList.addInfo(newsc)==false) {//카데고리 모음에도 삭제,추가
							JOptionPane.showMessageDialog(CategoryManagerDialog.this,"카테고리 중복입니다. 다시 입력하세요.","카테고리 추가 불가",JOptionPane.INFORMATION_MESSAGE);
							cate_name.setText("");
						}
						else {
							JOptionPane.showMessageDialog(CategoryManagerDialog.this,"카테고리가 변경되었습니다. ","카테고리 변경 완료",JOptionPane.INFORMATION_MESSAGE);
							SiteCategoryList.deleteInfo(s);
							model.removeElement(s);
							model.addElement(newsc);
							list.updateUI();
							cate_name.setText("");

							String oldFileName = new String(LoginDialog.path+id+"Category.txt");
							String tmpFileName = LoginDialog.path+"tmp_try.txt";
							BufferedReader br = null;
							BufferedWriter bw = null; 

							try {
								br = new BufferedReader(new FileReader(oldFileName));
								bw = new BufferedWriter(new FileWriter(tmpFileName));
								String line; 
								while ((line = br.readLine()) != null) {
									if (line.contains(ord_name)) 
										line = line.replace(ord_name, new_name);
									bw.write(line+"\r\n");
								}

								br.close();
								bw.close();  

								File oldFile = new File(oldFileName);
								if (oldFile.exists()){
									// 파일 삭제 성공시
									if (oldFile.delete()){
										System.out.println("파일을 삭제 성공");
										//파일 삭제 실패시
									}else{
										System.out.println("파일 삭제 실패");
									}
									// 지정한 경로에 파일이 존재안하는 경우 
								}else{
									System.out.println("파일이 없습니다.");
								}
								File newFile = new File(tmpFileName);
								newFile.renameTo(oldFile);
							}
							catch (Exception e1) {
								System.out.println("신규 오류");
							}
						}
					} 

				}
			}
		});


		btn_del.addActionListener(new ActionListener() {//삭제 버튼 클릭
			@Override
			public void actionPerformed(ActionEvent e) {
				SiteCategory removeSC = (SiteCategory)list.getSelectedValue();
				if(removeSC==null) {
					JOptionPane.showMessageDialog(CategoryManagerDialog.this,"카테고리를 선택하세요","카테고리 삭제 오류",JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					String removeCate = removeSC.toString();
					if(SiteCategoryList.deleteInfo(removeSC)) {//삭제 했다면
						JOptionPane.showMessageDialog(CategoryManagerDialog.this,removeCate+" 카테고리를 삭제했습니다. ","카테고리 삭제 완료",JOptionPane.INFORMATION_MESSAGE);
						cate_name.setText("");
						model.removeElement(removeSC);
						list.updateUI();


						//파일 내용도 삭제	
						String oldFileName = new String(LoginDialog.path+id+"Category.txt");
						String tmpFileName = LoginDialog.path+"tmp_try.txt";
						BufferedReader br = null;
						BufferedWriter bw = null; 

						try {
							br = new BufferedReader(new FileReader(oldFileName));
							bw = new BufferedWriter(new FileWriter(tmpFileName));
							String line; 
							while ((line = br.readLine()) != null) {
								if (line.contains(removeCate)) 
									continue;
								bw.write(line+"\r\n");
							}

							br.close();
							bw.close();  

							File oldFile = new File(oldFileName);
							if (oldFile.exists()){
								// 파일 삭제 성공시
								if (oldFile.delete()){
									System.out.println("파일을 삭제 성공");
									//파일 삭제 실패시
								}else{
									System.out.println("파일 삭제 실패");
								}
								// 지정한 경로에 파일이 존재안하는 경우 
							}else{
								System.out.println("파일이 없습니다.");
							}
							File newFile = new File(tmpFileName);
							newFile.renameTo(oldFile);
						}
						catch (Exception e1) {
							System.out.println("신규 오류");
						}
					}

					else {
						JOptionPane.showMessageDialog(CategoryManagerDialog.this,"카테고리가 없습니다. 다시 입력하세요.","카테고리 삭제 불가",JOptionPane.INFORMATION_MESSAGE);
						cate_name.setText("");
					}
				}
			}
		});
	}
	
	

}


