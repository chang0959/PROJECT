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
		super(f, "����Ʈ �з� ����",true);//super(Frame owner,String title,boolean b) 
		ListSet();
		buildGUI(); 
		setLocation(150,200);
		pack();
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	} 

	public void ListSet() {//����Ʈ
		id=SiteCategoryList.getId();
		System.out.println(id); 
		itemsC= SiteCategoryList.getItems();//���� ���̵��� ī�װ�

		//id�� ���� ī�װ� ���
		list = new JList<SiteCategory>(new DefaultListModel<SiteCategory>());
		model = (DefaultListModel<SiteCategory>)list.getModel(); 

		Iterator<SiteCategory> it1 = itemsC.iterator();
		while(it1.hasNext()) {
			model.addElement(it1.next());//�ؽ��¿� �ִ� ī�װ��� �𵨿� ����
		}  
		System.out.println(id+"�� ī�װ�");
		System.out.println(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//���� ���� �Ұ����ϰ�
	}

	private void buildGUI() {
		setLayout(new FlowLayout());
		JPanel leftP = new JPanel(new BorderLayout());
		leftP.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),"����׸�"));

		leftP.add(new JScrollPane(list));
		JPanel p1 = new JPanel(new BorderLayout()); 
		p1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),"���� ����"));
		JPanel p2 = new JPanel();//�ű�,�Ϸ� ��ư �г�
		JPanel rightP = new JPanel(new BorderLayout());

		JPanel cate_P= new JPanel();
		cate_name= new JTextField(10);
		cate_P.add(new JLabel("�׸� �̸�"));
		cate_P.add(cate_name);

		JPanel cate_btn_P= new JPanel();
		btn_del= new JButton("����(D)");
		btn_add= new JButton("�߰�(A)");
		btn_del.setMnemonic('d');
		btn_add.setMnemonic('a');
		cate_btn_P.add(btn_del);
		cate_btn_P.add(btn_add);

		JPanel editP = new JPanel(new GridLayout(2,0));
		editP.add(cate_P);
		editP.add(cate_btn_P);

		p1.add(editP,BorderLayout.NORTH);

		btn_new= new JButton("�ű�(N)");
		btn_exit= new JButton("�Ϸ�(E)");
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

		btn_add.addActionListener(new ActionListener() {//�߰��� ������
			@Override
			public void actionPerformed(ActionEvent e) {
				String cate = cate_name.getText(); 
				if(cate.equals("")) {
					JOptionPane.showMessageDialog(CategoryManagerDialog.this,"ī�װ��� �Է��ϼ���","ī�װ� �߰� ����",JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					//ī�װ� ���Ͽ� ����
					BufferedWriter bw;
					try { 
						SiteCategory scg = new SiteCategory(cate);
						if(SiteCategoryList.addInfo(scg)==true) {//�ߺ� �� �ż� ����
							//���� �̾��
							bw = new BufferedWriter(new FileWriter(LoginDialog.path+id+"Category.txt",true));
							PrintWriter pw= new PrintWriter(bw,true);
							pw.write(cate+"\r\n");
							pw.flush(); 
							pw.close();
							model.addElement(scg);
							list.updateUI();
							cate_name.setText("");
							System.out.println(id+"���Ͽ� "+cate+"(��)�� �����Ͽ����ϴ�. ");
						}
						else {//�ߺ��̶��
							JOptionPane.showMessageDialog(CategoryManagerDialog.this,"ī�װ� �ߺ��Դϴ�. �ٽ� �Է��ϼ���.","ī�װ� �߰� �Ұ�",JOptionPane.INFORMATION_MESSAGE);
							cate_name.setText("");
						}

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		btn_exit.addActionListener(new ActionListener() {//��ȭ���� ����������
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		btn_new.addActionListener(new ActionListener() {//ī�װ� ������ �����ϵ���
			@Override
			public void actionPerformed(ActionEvent e) {
				SiteCategory s = (SiteCategory)list.getSelectedValue();
				if(s==null) {
					JOptionPane.showMessageDialog(CategoryManagerDialog.this,"ī�װ��� �����ϼ���","ī�װ� ���� ����",JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					if(cate_name.getText().trim().equals("")) {
						JOptionPane.showMessageDialog(CategoryManagerDialog.this,"������ ī�װ��� �Է��ϼ���.","ī�װ� ���� ����",JOptionPane.INFORMATION_MESSAGE);
						return; 
					}
					String ord_name = s.toString();
					String new_name= cate_name.getText();//������ ī�װ�
					SiteCategory newsc = new SiteCategory();
					newsc.setWebsite(new_name);
					if (new_name==null) //�Է��� ���ٸ�
						JOptionPane.showMessageDialog(CategoryManagerDialog.this,"���� �����ϴ�. �ٽ� �Է��ϼ���","���� ����",JOptionPane.INFORMATION_MESSAGE);

					else {
						if(SiteCategoryList.addInfo(newsc)==false) {//ī���� �������� ����,�߰�
							JOptionPane.showMessageDialog(CategoryManagerDialog.this,"ī�װ� �ߺ��Դϴ�. �ٽ� �Է��ϼ���.","ī�װ� �߰� �Ұ�",JOptionPane.INFORMATION_MESSAGE);
							cate_name.setText("");
						}
						else {
							JOptionPane.showMessageDialog(CategoryManagerDialog.this,"ī�װ��� ����Ǿ����ϴ�. ","ī�װ� ���� �Ϸ�",JOptionPane.INFORMATION_MESSAGE);
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
									// ���� ���� ������
									if (oldFile.delete()){
										System.out.println("������ ���� ����");
										//���� ���� ���н�
									}else{
										System.out.println("���� ���� ����");
									}
									// ������ ��ο� ������ ������ϴ� ��� 
								}else{
									System.out.println("������ �����ϴ�.");
								}
								File newFile = new File(tmpFileName);
								newFile.renameTo(oldFile);
							}
							catch (Exception e1) {
								System.out.println("�ű� ����");
							}
						}
					} 

				}
			}
		});


		btn_del.addActionListener(new ActionListener() {//���� ��ư Ŭ��
			@Override
			public void actionPerformed(ActionEvent e) {
				SiteCategory removeSC = (SiteCategory)list.getSelectedValue();
				if(removeSC==null) {
					JOptionPane.showMessageDialog(CategoryManagerDialog.this,"ī�װ��� �����ϼ���","ī�װ� ���� ����",JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					String removeCate = removeSC.toString();
					if(SiteCategoryList.deleteInfo(removeSC)) {//���� �ߴٸ�
						JOptionPane.showMessageDialog(CategoryManagerDialog.this,removeCate+" ī�װ��� �����߽��ϴ�. ","ī�װ� ���� �Ϸ�",JOptionPane.INFORMATION_MESSAGE);
						cate_name.setText("");
						model.removeElement(removeSC);
						list.updateUI();


						//���� ���뵵 ����	
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
								// ���� ���� ������
								if (oldFile.delete()){
									System.out.println("������ ���� ����");
									//���� ���� ���н�
								}else{
									System.out.println("���� ���� ����");
								}
								// ������ ��ο� ������ ������ϴ� ��� 
							}else{
								System.out.println("������ �����ϴ�.");
							}
							File newFile = new File(tmpFileName);
							newFile.renameTo(oldFile);
						}
						catch (Exception e1) {
							System.out.println("�ű� ����");
						}
					}

					else {
						JOptionPane.showMessageDialog(CategoryManagerDialog.this,"ī�װ��� �����ϴ�. �ٽ� �Է��ϼ���.","ī�װ� ���� �Ұ�",JOptionPane.INFORMATION_MESSAGE);
						cate_name.setText("");
					}
				}
			}
		});
	}
	
	

}


