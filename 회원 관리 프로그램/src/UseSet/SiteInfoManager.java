package UseSet;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableRowSorter;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SiteInfoManager {
	static JFrame frame;
	private SiteInfoList sil = new SiteInfoList();
	public static boolean isExcel = false;
	private HashSet<SiteCategory> itemsC= new HashSet<SiteCategory>(); // �ش� ���̵��� ī�װ� ����
	private Vector<String> addInfoD = new Vector<String>(); // �߰������� �з� �޺��ڽ� ������
	private Vector<String> searchD = new Vector<String>(); // �˻�/������ �˻� �޺��ڽ� ������
	private Vector<String> sortCateD = new Vector<String>(); // ������ �з� �޺��ڽ� ������
	private Vector<String> sortSiteD = new Vector<String>(); // ������ ����Ʈ �̸� �޺��ڽ� ������

	private JComboBox<String> cateCB; // ���� ȭ�� �з��� �޺��ڽ�
	private JComboBox<String> searchCB, sortCateCB, sortSiteCB, preferCB; // ������ ȭ�� ������ �޺��ڽ� 3��
	private JTable mT;
	private TableRowSorter<InfoTableModel> sorter;

	private JLabel SiteSize = new JLabel();
	// ���� �������
	private JTextField siteF,urlF,idF;
	private JPasswordField pwF;
	private JTextArea memoT;
	private JButton newbtn,inputbtn,editbtn;

	// ������ �������
	private JButton info_del;
	private int viewRow;
	private int modelRow=-999;
	private JTextField info_idL, info_pwL; //�������� ���� text�ʵ�
	private String tmpId=null, tmpPw=null;
	private JCheckBox infoC = new JCheckBox("�������� ����");
	private InfoTableModel model = new InfoTableModel(sil);
	private JTextField filterin; // ���͸�

	private CardLayout card=new CardLayout();
	private JPanel cardP;

	private JCheckBoxMenuItem s_i1 = new JCheckBoxMenuItem("�ڵ� �α���(L)");;
	private JMenuItem m_i1 = new JMenuItem("�����(U)");
	private int[] data; 
	private int[] arcAngle; 
	private Color[] color = {Color.RED, Color.BLUE, 
			Color.MAGENTA, Color.ORANGE,Color.LIGHT_GRAY,Color.PINK,Color.YELLOW}; 

	SiteInfoManager(){ 
		frame = new JFrame("��ü");
		
		//���� ������ؼ� �ڵ��α������� Ȯ��
		File auto_file = new File(LoginDialog.path+"auto_login.txt");
		if(auto_file.exists()) {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(auto_file));
				String line; 
				while ((line = br.readLine()) != null) {
					SiteCategoryList.setId(line);
				}
				br.close();
			}catch(Exception e1) {
				System.out.println("�ű� ����");
			}
			//�ڵ��α��� üũ�ǰ�
			s_i1.setSelected(true); 
			
			//���� ���� üũ ���� �ִٸ�
			File rem_info = new File(LoginDialog.path+"rem_info.txt");
			if(rem_info.exists()) {//���� ���� ���� üũ true�� ��
				System.out.println("rem_info ���� ����");
				infoC.setSelected(true);
			} 
		} 
		//������ �������� �ʴٸ�
		else { 
			LoginDialog dlg = new LoginDialog(frame);
			dlg.setVisible(true);
		}

		//���� ���� ���̱� 
		File f = new File(LoginDialog.path1);  
		File[] fileList = f.listFiles();
		String[] subFName = new String[fileList.length];

		for (int i = 0; i < fileList.length; i++) {
			File subF = fileList[i];
			subFName[i]=subF.getName();//��� ���� �̸� ���
			System.out.println(subFName[i]);
		}  

		//���̵� ���� ���� ���� ã�� 
		sortSiteD.add("����Ʈ �̸�");
		for (int i = 0; i < subFName.length; i++) {
			if(subFName[i].contains(SiteCategoryList.getId()+"Excel.xlsx")) {
				//���� ���� �ִٸ� ��������  
				System.out.println("���� ������ �ֽ��ϴ�. ");
				isExcel=true;
				File id_excel= new File(LoginDialog.path+subFName[i]); 
				FileInputStream fis=null;
				SiteInfo excelinfo; 
				String[] exceldata; 
				try {
					fis = new FileInputStream(id_excel);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
				XSSFWorkbook workbook = null;
				try {
					workbook = new XSSFWorkbook(fis);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int rowindex=0;
				int columnindex=0;
				//��Ʈ �� (ù��°���� �����ϹǷ� 0�� �ش�)
				//���� �� ��Ʈ�� �б����ؼ��� FOR���� �ѹ��� �����ش�
				XSSFSheet sheet=workbook.getSheetAt(0);
				//���� ��
				int rows=sheet.getPhysicalNumberOfRows();
				for(rowindex=1;rowindex<rows;rowindex++){
					//�����д´�
					XSSFRow row=sheet.getRow(rowindex);
					exceldata = new String[7];
					if(row !=null){
						//���� ��
						int cells=row.getPhysicalNumberOfCells();
						for(columnindex=0;columnindex<=cells;columnindex++){
							//������ �д´�
							XSSFCell cell=row.getCell(columnindex);

							//���� ���ϰ�츦 ���� ��üũ
							if(cell==null){
								continue;
							}else{
								//Ÿ�Ժ��� ���� �б�
								switch (cell.getCellType()){
								case FORMULA:
									exceldata[columnindex]=cell.getCellFormula()+"";
									break; 

								case NUMERIC:
									exceldata[columnindex]=cell.getNumericCellValue()+"";
									break;

								case STRING:
									exceldata[columnindex]=cell.getStringCellValue();
									//v = cell.getStringCellValue()+"";
									break;

								case BLANK:
									exceldata[columnindex]=cell.getBooleanCellValue()+"";
									//v = cell.getStringCellValue()+"";
									break;

								case ERROR:
									exceldata[columnindex]=cell.getErrorCellValue()+"";
									break;
								}
							}
							System.out.println("�� �� ���� :"+exceldata[columnindex]); // ���� �ʿ� - ���̺� ��� �� ����
							exceldata[columnindex] = exceldata[columnindex].trim();
						}
					}
					excelinfo = new SiteInfo(exceldata);
					sil.addSiteInfo(excelinfo);//sil�� ���� ���� ���
					model.addRow(sil, excelinfo);
					SiteSize.setText(sil.size()+"���� ����Ʈ�� ��ϵǾ��ֽ��ϴ�.");
					//sortSiteD.add(exceldata[0]); 
					if(!sortSiteD.contains(exceldata[0])) // �����ϰ� ���� ������
						sortSiteD.add(exceldata[0]);    // �߰�
				}
				try {
					fis.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}//if
			else{//���ٸ� 
				isExcel=false;
				continue;
			}
		}

		frame.setTitle(SiteCategoryList.getId()+"(��)�� ���� ���ͳ� �������� v0.51");
		if(SiteCategoryList.getId().equals("manager"))//�Ŵ��� ���� Ȯ��
			m_i1.setEnabled(true);
		else 
			m_i1.setEnabled(false);

		readData();
		buildGUI();
		createMenu();
		registerTableSelectionEventListener();

		frame.setSize(1000,600);
		frame.setLocation(300,50);

		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);//�ݱ⸦ ��ȿȭ
		WindowListener wListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				sureClosing();
			}
		};
		frame.addWindowListener(wListener);
		frame.setVisible(true);
	}

	private JPanel buildGUIL() {
		JPanel left = new JPanel(new BorderLayout());

		JPanel p = new JPanel(new BorderLayout());
		JPanel p2 = new JPanel(new BorderLayout());

		p2.add(createInfoP(),BorderLayout.NORTH); // �߰�����
		p2.add(createbtnP()); // ��ư
		p.add(createDetailInfoP(),BorderLayout.NORTH);
		p.add(p2);

		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JPanel total = new JPanel(new BorderLayout());
		total.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),"�Է�/����"));
		total.add(p,BorderLayout.NORTH);
		left.add(total,BorderLayout.WEST);
		left.add(p1,BorderLayout.SOUTH);
		left.add(new JPanel(),BorderLayout.CENTER);
		return left;
	}

	private void readData() {
		SiteCategoryList s = new SiteCategoryList();  
		s.setCategory(LoginDialog.path,SiteCategoryList.getId()); 
		itemsC= SiteCategoryList.getItems(); //���� ���̵��� ī�װ�

		//id�� ���� ī�װ� ���
		Iterator<SiteCategory> it1 = itemsC.iterator();
		SiteCategory t;
		while(it1.hasNext()) {
			t = it1.next();
			addInfoD.add(t.toString()); // ī�װ��� �𵨿� ����
		}   

		searchD.add("��ü");
		searchD.addAll(addInfoD);

		sortCateD.add("�з�");
		sortCateD.addAll(addInfoD);

		sil.showAllSiteInfo();
	}

	private JPanel createDetailInfoP() {//�⺻ ����
		JPanel sNameP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		siteF= new JTextField(15);
		siteF.addKeyListener(editField);
		urlF= new JTextField(10);
		urlF.addKeyListener(editField);
		idF= new JTextField(8);
		idF.addKeyListener(editField);
		pwF= new JPasswordField(8);
		pwF.addKeyListener(editField);

		sNameP.add(new JLabel("����Ʈ��"));
		sNameP.add(siteF);

		JPanel sURLP= new JPanel(new FlowLayout(FlowLayout.LEFT));
		sURLP.add(new JLabel("�ּ�(URL) http:// "));
		sURLP.add(urlF);

		JPanel IdP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		IdP.add(new JLabel("�� �� ��"));
		IdP.add(idF);

		JPanel pwdP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pwdP.add(new JLabel("��й�ȣ"));
		pwF.setText("");
		pwdP.add(pwF);

		JPanel totalP1 = new JPanel(new GridLayout(4,0));
		totalP1.add(sNameP);
		totalP1.add(sURLP);
		totalP1.add(IdP);
		totalP1.add(pwdP);
		totalP1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),"�⺻ ����"));
		return totalP1;
	}

	// ���� �ؽ�Ʈ�ʵ� �� ���� ��, �����ۼ� ��ư Ȱ��ȭ: ���� �гο��� ��� �׸��̵� ���� �ٲ�ų� �����Ǵ� ���� Ȱ��
	private KeyListener editField = new KeyAdapter(){
		@Override
		public void keyPressed(KeyEvent e) {
			newbtn.setEnabled(true);
		}
	};

	private JPanel createInfoP() {//�߰� ����
		JPanel groupP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		groupP.add(new JLabel("��     ��"));
		cateCB = new JComboBox<String>(addInfoD);
		cateCB.setSelectedIndex(0);
		cateCB.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				newbtn.setEnabled(true);
			}
		});
		groupP.add(cateCB);

		JPanel preferP = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
		preferP.add(new JLabel("�� ȣ ��"));
		Vector<String> v2 = new Vector<String>();
		preferCB = new JComboBox<String>(v2);
		v2.add("������");
		v2.add("�١١١١�");
		v2.add("�١١١�");
		v2.add("�١١�");
		v2.add("�١�");
		v2.add("��");
		preferCB.setSelectedIndex(0);
		preferCB.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				newbtn.setEnabled(true);
			}
		});
		preferP.add(preferCB);

		JPanel memo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		memoT= new JTextArea(5,15);
		memoT.addKeyListener(editField);
		memo.add(new JLabel("��     ��"));
		memo.add(memoT);

		JPanel totalP2 = new JPanel(new GridLayout(2,0));
		totalP2.add(groupP);
		totalP2.add(preferP);

		JPanel t = new JPanel(new BorderLayout());
		t.add(totalP2, BorderLayout.NORTH);
		t.add(memo);

		t.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),"�߰� ����"));

		return t;
	}

	private JPanel createbtnP() {
		JPanel btnP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		cardP = new JPanel(card);
		newbtn= new JButton("���� �ۼ�(N)");
		newbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(modelRow<0)
					urlF.setText("");
				siteF.setText("");
				idF.setText("");
				pwF.setText("");
				memoT.setText("");
				cateCB.setSelectedIndex(0);
				preferCB.setSelectedIndex(0);

			}
		});
		newbtn.setMnemonic('N');

		inputbtn = new JButton("�Է�(I)");
		inputbtn.setMnemonic('I');
		inputbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] tmp = new String[7];
				tmp[0] = siteF.getText();
				tmp[1] = urlF.getText();
				tmp[2] = idF.getText();
				tmp[3] = new String(pwF.getPassword());
				tmp[4] = (String)cateCB.getSelectedItem();
				tmp[5] = (String)preferCB.getSelectedItem();
				tmp[6] = memoT.getText();

				SiteInfo si = new SiteInfo(tmp);
				if(sil.checkSiteInfo(si)){
					JOptionPane.showMessageDialog(frame, "���� ����Ʈ�� �̹� �����մϴ�!", "���", JOptionPane.WARNING_MESSAGE);
				}
				else {
					if(checkSiteName(tmp[0]))
						JOptionPane.showMessageDialog(frame, "������ ����Ʈ���� �̹� �����մϴ�!", "���", JOptionPane.WARNING_MESSAGE);
					else {
						if(!tmp[5].equals("������")) {
							model.addRow(sil, si);

							if(!sortSiteD.contains(tmp[0])) // �����ϰ� ���� ������
								sortSiteD.add(tmp[0]);    // �߰�
							//sortSiteD.add(tmp[0]);

							// �ʱ�ȭ
							siteF.setText("");
							urlF.setText("");
							idF.setText("");
							pwF.setText("");
							memoT.setText("");
							cateCB.setSelectedIndex(0);
							preferCB.setSelectedIndex(0);
							System.out.println("==========================");
							sil.showAllSiteInfo();
							SiteSize.setText(sil.size()+"���� ����Ʈ�� ��ϵǾ��ֽ��ϴ�.");
						}
						else {
							JOptionPane.showMessageDialog(frame, "��ȣ���� �������ּ���!", "���", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
				checkSite();
				scoreplot plot = new scoreplot();
				plot.repaint();
			}
		});

		inputbtn.setEnabled(true);
		btnP.add(newbtn);
		cardP.add("�Է�",inputbtn);

		editbtn = new JButton("����(E)");
		editbtn.setMnemonic('E');
		editbtn.setEnabled(false);
		editbtn.addActionListener(new ActionListener() { // ǥ ���� ����
			@Override
			public void actionPerformed(ActionEvent e) { 
				// siteF,urlF,idF,pwF, memoT + comboB 2��
				String[] tmp = new String[7];
				tmp[0] = siteF.getText();
				tmp[1] = urlF.getText();
				tmp[2] = idF.getText();
				tmp[3] = tmpPw;
				if(new String(pwF.getPassword()).length()!=0) {
					tmp[3] = new String(pwF.getPassword());
				}
				tmp[4] = (String)cateCB.getSelectedItem();
				tmp[5] = (String)preferCB.getSelectedItem();
				tmp[6] = memoT.getText();

				if(!tmp[5].equals("������")) {
					model.addRow(sil, new SiteInfo(tmp));
					//mT.updateUI();
					
					info_idL.setText(tmp[2]); 
					info_pwL.setText(tmp[3]);

					newbtn.setEnabled(false);

					if(!sortSiteD.contains(tmp[0]))
						sortSiteD.add(tmp[0]);

					siteF.setText("");
					urlF.setText("");
					urlF.setEditable(true);
					idF.setText("");
					pwF.setText("");
					memoT.setText("");
					cateCB.setSelectedIndex(0);
					preferCB.setSelectedIndex(0);
					mT.clearSelection();
					card.show(cardP, "�Է�");

					infoC.setSelected(false);
					info_idL.setText("");
					info_pwL.setText("");
					checkSite();
					modelRow = -999; // ǥ ������ �� ���� �Է� ������ url�� ���쵵��
				}
				else {
					JOptionPane.showMessageDialog(frame, "��ȣ���� �������ּ���!", "���", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		cardP.add("����",editbtn);

		btnP.add(cardP);

		return btnP;
	}

	private void buildGUI() {
		JPanel p = new JPanel(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.addTab("����Ʈ ���", createSiteList());
		tabbedPane.addTab("��� ��Ȳ", createRegister());

		p.add(tabbedPane);

		frame.add(buildGUIL(),BorderLayout.WEST);
		frame.add(p);
		frame.add(createBottom(), BorderLayout.SOUTH);
	}

	private JPanel createBottom() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		int siteNum = sil.size();
		SiteSize.setText(siteNum+"���� ����Ʈ�� ��ϵǾ��ֽ��ϴ�.");
		p.add(SiteSize);

		return p;
	}

	private void createMenu() {
		JMenuBar mb = new JMenuBar();
		frame.setJMenuBar(mb);

		// JMenu ����
		JMenu mf = new JMenu("����(F)");
		JMenu mm = new JMenu("����(M)");
		JMenu ms = new JMenu("����(S)");

		mf.setMnemonic('F'); mm.setMnemonic('M'); ms.setMnemonic('S');
		mb.add(mf); mb.add(mm); mb.add(ms);

		// 1. ���� MenuItem ����
		JMenuItem f_i1 = new JMenuItem("�������Ͽ��� ��������(I)");
		f_i1.setMnemonic('I'); f_i1.setEnabled(true);
		f_i1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isSiteName = false; // ���� ����Ʈ���ִ°�
				boolean isSiteCate = false; // ����� �и���Ͽ� �ִ°�
				JFileChooser fileChooser = new JFileChooser(); 

				//�⺻ ��� = ����ȭ��
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "//" + "Desktop")); 

				FileNameExtensionFilter filter = new FileNameExtensionFilter("xlsx ����", "xlsx"); 
				// Ȯ���� �߰� 
				fileChooser.setFileFilter(filter); 

				//���Ͽ��� ���̾�α� �� ��� 
				int result = fileChooser.showOpenDialog(frame); 
				System.out.println(result); 

				if (result == JFileChooser.APPROVE_OPTION) { 
					//������ ������ ��� ��ȯ 
					File selectedFile = fileChooser.getSelectedFile(); 
					FileInputStream fis=null;
					SiteInfo excelinfo;
					String[] exceldata;
					try {
						fis = new FileInputStream(selectedFile);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					XSSFWorkbook workbook = null;
					try {
						workbook = new XSSFWorkbook(fis);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					int rowindex=0;
					int columnindex=0;
					//��Ʈ �� (ù��°���� �����ϹǷ� 0�� �ش�)
					//���� �� ��Ʈ�� �б����ؼ��� FOR���� �ѹ��� �����ش�
					XSSFSheet sheet=workbook.getSheetAt(0);
					//���� ��
					int rows=sheet.getPhysicalNumberOfRows();
					for(rowindex=1;rowindex<rows;rowindex++){
						//�����д´�
						XSSFRow row=sheet.getRow(rowindex);
						exceldata = new String[7];
						if(row !=null){
							//���� ��
							int cells=row.getPhysicalNumberOfCells();
							for(columnindex=0;columnindex<=cells;columnindex++){
								//������ �д´�
								XSSFCell cell=row.getCell(columnindex);

								//���� ���ϰ�츦 ���� ��üũ
								if(cell==null){
									continue;
								}else{
									//Ÿ�Ժ��� ���� �б�
									switch (cell.getCellType()){
									case FORMULA:
										exceldata[columnindex]=cell.getCellFormula()+"";
										break; 

									case NUMERIC:
										exceldata[columnindex]=cell.getNumericCellValue()+"";
										break;

									case STRING:
										exceldata[columnindex]=cell.getStringCellValue();
										//v = cell.getStringCellValue()+"";
										break;

									case BLANK:
										exceldata[columnindex]=cell.getBooleanCellValue()+"";
										//v = cell.getStringCellValue()+"";
										break;

									case ERROR:
										exceldata[columnindex]=cell.getErrorCellValue()+"";
										break;
									}
								}
								System.out.println("�� �� ���� :"+exceldata[columnindex]); // ���� �ʿ� - ���̺� ��� �� ����
								exceldata[columnindex] = exceldata[columnindex].trim();
							}
						}
						excelinfo = new SiteInfo(exceldata);
						if(!checkSiteName(excelinfo.site)) { // ������ ����Ʈ ���� ���� ���
							if(addInfoD.contains(excelinfo.sitecate)) { // ������� ����Ʈ �з���Ͽ� ������ ���� ���
								model.addRow(sil, excelinfo);
								SiteSize.setText(sil.size()+"���� ����Ʈ�� ��ϵǾ��ֽ��ϴ�.");
								if(!sortSiteD.contains(exceldata[0])) // �����ϰ� ���� ������
									sortSiteD.add(exceldata[0]);    // �߰�
							}
							else {
								isSiteCate = true; // ������� ����Ʈ �з���Ͽ� ���� ������
							}
						}
						else {
							isSiteName = true;
						}
					}
					try {
						fis.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					if(isSiteName)
						JOptionPane.showMessageDialog(frame, "������ ����Ʈ ���� �����ϰ� "+selectedFile.getName()+" ������ �����Խ��ϴ�. ", "���� �ҷ�����", JOptionPane.INFORMATION_MESSAGE);
					else
						JOptionPane.showMessageDialog(frame, selectedFile.getName()+" ������ �����Խ��ϴ�. ", "���� �ҷ�����", JOptionPane.INFORMATION_MESSAGE);
					
					if(isSiteCate)
						JOptionPane.showMessageDialog(frame, "������� ����Ʈ �з� ��Ͽ� �������� �ʴ� �����Ͱ� �ֽ��ϴ�. �ش� �����͸� �����ϰ� �����ɴϴ�.", "���", JOptionPane.WARNING_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(frame, "������ �������� �ʾҽ��ϴ�", "���", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
		});

		JMenuItem f_i2 = new JMenuItem("�������Ϸ� ��������(E)");
		f_i2.setMnemonic('E'); f_i2.setEnabled(true);
		f_i2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(); 

				//�⺻ ��� = ����ȭ��
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "//" + "Desktop")); 
				FileNameExtensionFilter filter = new FileNameExtensionFilter("xlsx ����", "xlsx"); 
				// Ȯ���� �߰� 
				fileChooser.setFileFilter(filter); 
				//���Ͽ��� ���̾�α� �� ��� 
				int result = fileChooser.showSaveDialog(frame); 
				File selectedFile;
				
				if (result == JFileChooser.APPROVE_OPTION) { 
					//������ ������ ��� ��ȯ 
					selectedFile = fileChooser.getSelectedFile(); 
					// �������� 
					XSSFWorkbook workbook = new XSSFWorkbook();
					XSSFSheet sheet2 = workbook.createSheet("sheet");
	
					//3.row ����
					XSSFRow row0 = sheet2.createRow(0);
					//4.cell ����
					row0.createCell(0).setCellValue("����Ʈ��");
					row0.createCell(1).setCellValue("�ּ�");
					row0.createCell(2).setCellValue("���̵�");
					row0.createCell(3).setCellValue("��й�ȣ");
					row0.createCell(4).setCellValue("�з�");
					row0.createCell(5).setCellValue("��ȣ��");
					row0.createCell(6).setCellValue("�޸�");
	
					//5.data ����
					XSSFRow[] row = new XSSFRow[mT.getRowCount()];
					SiteInfo tmpinfo;
					for(int k=0; k<mT.getRowCount(); k++) {
						tmpinfo = model.getSelectedCell(k);
						row[k] = sheet2.createRow(k+1);
						row[k].createCell(0).setCellValue(tmpinfo.site);
						row[k].createCell(1).setCellValue(tmpinfo.url);
						row[k].createCell(2).setCellValue(tmpinfo.id);
						row[k].createCell(3).setCellValue(tmpinfo.pwd);
						row[k].createCell(4).setCellValue(tmpinfo.sitecate);
						row[k].createCell(5).setCellValue(tmpinfo.prefer);
						row[k].createCell(6).setCellValue(tmpinfo.memo);
					}
					//6.workbook�� ���Ϸ� ����
					try {
						//�Ű����� ��Ʈ������ "���/����" �־��൵��.
						FileOutputStream fos = new FileOutputStream(selectedFile.getPath()+".xlsx"); //���� ���ϻ���
						workbook.write(fos);//workbook�� fos �� ����.
						fos.flush();
						fos.close();
						JOptionPane.showMessageDialog(frame, "Excel ���Ϸ� �����Ͽ����ϴ�.", "���� ����", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				else{
					selectedFile = null;
					JOptionPane.showMessageDialog(frame, "������ �������� �ʾҽ��ϴ�", "���", JOptionPane.WARNING_MESSAGE);
		        }
			}//�̺�Ʈ
		});

		JMenuItem f_i3 = new JMenuItem("����(S)");
		f_i3.setMnemonic('S');
		f_i3.addActionListener(new ActionListener() {//���� ��ư�� ������
			@Override
			public void actionPerformed(ActionEvent e) {
				//���� ���Ͽ� ���� 
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet2 = workbook.createSheet("sheet");

				//3.row ����
				XSSFRow row0 = sheet2.createRow(0);
				//4.cell ����
				row0.createCell(0).setCellValue("����Ʈ��");
				row0.createCell(1).setCellValue("�ּ�");
				row0.createCell(2).setCellValue("���̵�");
				row0.createCell(3).setCellValue("��й�ȣ");
				row0.createCell(4).setCellValue("�з�");
				row0.createCell(5).setCellValue("��ȣ��");
				row0.createCell(6).setCellValue("�޸�");

				//5.data ����
				XSSFRow[] row = new XSSFRow[mT.getRowCount()];
				SiteInfo tmpinfo;
				for(int k=0; k<mT.getRowCount(); k++) {
					tmpinfo = model.getSelectedCell(k);
					row[k] = sheet2.createRow(k+1);
					row[k].createCell(0).setCellValue(tmpinfo.site);
					row[k].createCell(1).setCellValue(tmpinfo.url);
					row[k].createCell(2).setCellValue(tmpinfo.id);
					row[k].createCell(3).setCellValue(tmpinfo.pwd);
					row[k].createCell(4).setCellValue(tmpinfo.sitecate);
					row[k].createCell(5).setCellValue(tmpinfo.prefer);
					row[k].createCell(6).setCellValue(tmpinfo.memo);
				}
				//6.workbook�� ���Ϸ� ����
				try {
					//�Ű����� ��Ʈ������ "���/����" �־��൵��.
					FileOutputStream fos = new FileOutputStream(LoginDialog.path+"\\"+SiteCategoryList.getId()+"Excel.xlsx"); //���� ���ϻ���
					workbook.write(fos);//workbook�� fos �� ����.
					fos.flush();
					fos.close();
					JOptionPane.showMessageDialog(frame, SiteCategoryList.getId()+"Excel.xlsx ���Ϸ� �����Ͽ����ϴ�.", "���� ����", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		JMenuItem f_i4 = new JMenuItem("�α׾ƿ�(O)");
		f_i4.setMnemonic('O');

		f_i4.addActionListener(new ActionListener() {//�α׾ƿ��� Ŭ���ϸ� 
			@Override
			public void actionPerformed(ActionEvent e) {
				//������ �ڷ� �����ϰ� �α׾ƿ��� �Ұ��� �����.
				int n = JOptionPane.showConfirmDialog(frame,"������ �ڷḦ �����Ͻðڽ��ϱ�?","Ȯ��",JOptionPane.YES_NO_CANCEL_OPTION);
				switch(n) {
				case JOptionPane.YES_OPTION://�ڷ� ����
					//�ڷ� ���� �κ�
					XSSFWorkbook workbook = new XSSFWorkbook();
					XSSFSheet sheet2 = workbook.createSheet("sheet");

					//3.row ����
					XSSFRow row0 = sheet2.createRow(0);
					//4.cell ����
					row0.createCell(0).setCellValue("����Ʈ��");
					row0.createCell(1).setCellValue("�ּ�");
					row0.createCell(2).setCellValue("���̵�");
					row0.createCell(3).setCellValue("��й�ȣ");
					row0.createCell(4).setCellValue("�з�");
					row0.createCell(5).setCellValue("��ȣ��");
					row0.createCell(6).setCellValue("�޸�");

					//5.data ����
					XSSFRow[] row = new XSSFRow[mT.getRowCount()];
					SiteInfo tmpinfo;
					for(int k=0; k<mT.getRowCount(); k++) {
						tmpinfo = model.getSelectedCell(k);
						row[k] = sheet2.createRow(k+1);
						row[k].createCell(0).setCellValue(tmpinfo.site);
						row[k].createCell(1).setCellValue(tmpinfo.url);
						row[k].createCell(2).setCellValue(tmpinfo.id);
						row[k].createCell(3).setCellValue(tmpinfo.pwd);
						row[k].createCell(4).setCellValue(tmpinfo.sitecate);
						row[k].createCell(5).setCellValue(tmpinfo.prefer);
						row[k].createCell(6).setCellValue(tmpinfo.memo);
					}
					//6.workbook�� ���Ϸ� ����
					try {
						//�Ű����� ��Ʈ������ "���/����" �־��൵��.
						FileOutputStream fos = new FileOutputStream(LoginDialog.path+"\\"+SiteCategoryList.getId()+"Excel.xlsx"); //���� ���ϻ���
						workbook.write(fos);//workbook�� fos �� ����.
						fos.flush();
						fos.close();
						JOptionPane.showMessageDialog(frame, SiteCategoryList.getId()+"Excel.xlsx ���Ϸ� �����Ͽ����ϴ�.", "���� ����", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					frame.setVisible(false); 
					new SiteInfoManager();
					break;
				case JOptionPane.NO_OPTION:
					//�׳� �α׾ƿ��� 
					frame.setVisible(false); 
					new SiteInfoManager();
					break;
				case JOptionPane.CANCEL_OPTION:
					break;
				}   
			}
		});

		JMenuItem f_i5 = new JMenuItem("����(X)");
		f_i5.setMnemonic('X');
		f_i5.addActionListener(new ActionListener() {//�����ư�� ������ 
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(frame,"������ �ڷḦ �����Ͻðڽ��ϱ�?","Ȯ��",JOptionPane.YES_NO_CANCEL_OPTION);
				switch(n) {
				case JOptionPane.YES_OPTION://�ڷ� ����
					//�ڷ� ���� �κ�
					XSSFWorkbook workbook = new XSSFWorkbook();
					XSSFSheet sheet2 = workbook.createSheet("sheet");

					//3.row ����
					XSSFRow row0 = sheet2.createRow(0);
					//4.cell ����
					row0.createCell(0).setCellValue("����Ʈ��");
					row0.createCell(1).setCellValue("�ּ�");
					row0.createCell(2).setCellValue("���̵�");
					row0.createCell(3).setCellValue("��й�ȣ");
					row0.createCell(4).setCellValue("�з�");
					row0.createCell(5).setCellValue("��ȣ��");
					row0.createCell(6).setCellValue("�޸�");

					//5.data ����
					XSSFRow[] row = new XSSFRow[mT.getRowCount()];
					SiteInfo tmpinfo;
					for(int k=0; k<mT.getRowCount(); k++) {
						tmpinfo = model.getSelectedCell(k);
						row[k] = sheet2.createRow(k+1);
						row[k].createCell(0).setCellValue(tmpinfo.site);
						row[k].createCell(1).setCellValue(tmpinfo.url);
						row[k].createCell(2).setCellValue(tmpinfo.id);
						row[k].createCell(3).setCellValue(tmpinfo.pwd);
						row[k].createCell(4).setCellValue(tmpinfo.sitecate);
						row[k].createCell(5).setCellValue(tmpinfo.prefer);
						row[k].createCell(6).setCellValue(tmpinfo.memo);
					}
					//6.workbook�� ���Ϸ� ����
					try {
						//�Ű����� ��Ʈ������ "���/����" �־��൵��.
						FileOutputStream fos = new FileOutputStream(LoginDialog.path+"\\"+SiteCategoryList.getId()+"Excel.xlsx"); //���� ���ϻ���
						workbook.write(fos);//workbook�� fos �� ����.
						fos.flush();
						fos.close();
						JOptionPane.showMessageDialog(frame, SiteCategoryList.getId()+"Excel.xlsx ���Ϸ� �����Ͽ����ϴ�.", "���� ����", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e1) {
						e1.printStackTrace();
					} 
					System.exit(0);
					break;
				case JOptionPane.NO_OPTION:
					//�׳� ���Ḹ
					System.exit(0);
					break;
				case JOptionPane.CANCEL_OPTION:
					break;
				}   
			}
		});

		mf.add(f_i1); mf.add(f_i2); mf.add(f_i3);
		mf.addSeparator();
		mf.add(f_i4); mf.add(f_i5);

		// 2. ���� MenuItem ����
		m_i1.setMnemonic('U');
		m_i1.addActionListener(new ActionListener() {//����� ��ư�� Ŭ���ϸ�
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ManagerDialog md = new ManagerDialog(frame);
				md.setVisible(true); 
			}
		});

		JMenuItem m_i2 = new JMenuItem("����Ʈ �з�(C)");
		m_i2.setMnemonic('C');
		mm.add(m_i1); mm.add(m_i2);

		m_i2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CategoryManagerDialog cgm_dlg = new CategoryManagerDialog(frame); 
				cgm_dlg.setVisible(true);
				addInfoD.removeAllElements();
				Iterator<SiteCategory> it1 = itemsC.iterator();//�ٽ� ����
				SiteCategory t;
				while(it1.hasNext()) {
					t = it1.next();
					addInfoD.add(t.toString()); // ī�װ��� �𵨿� ����
				}    

				searchD.removeAllElements();
				searchD.add("��ü");
				searchD.addAll(addInfoD);

				sortCateD.removeAllElements();
				sortCateD.add("�з�");
				sortCateD.addAll(addInfoD);

				cateCB.updateUI();
				searchCB.updateUI();
				sortCateCB.updateUI();  
			}
		});

		// 3. ���� MenuItem ����
		s_i1.setMnemonic('L');

		ItemListener auto_login= new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {//���õ� �����
					//���� ���� ���� ����
					if(infoC.isSelected()) {
						FileWriter fwinfoC;
						try {
							fwinfoC = new FileWriter(LoginDialog.path+"rem_info.txt", true); 
							fwinfoC.write("true");
							fwinfoC.flush(); 
							fwinfoC.close();
							
							FileWriter fw = new FileWriter(LoginDialog.path+"auto_login.txt", true);//������ ���̵� ���� ����
							fw.write(SiteCategoryList.getId());//pwd ������ ���� 
							fw.flush(); 
							fw.close();
							JOptionPane.showMessageDialog(frame,"�ڵ��α��� �����Ͽ����ϴ�.","�ڵ��α��� ����",JOptionPane.INFORMATION_MESSAGE);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else {//���� üũ �� �ߴٸ� 
						try {
							FileWriter fw = new FileWriter(LoginDialog.path+"auto_login.txt", true);//������ ���̵� ���� ����
							fw.write(SiteCategoryList.getId());//pwd ������ ���� 
							fw.flush(); 
							fw.close();
							JOptionPane.showMessageDialog(frame,"�ڵ��α��� �����Ͽ����ϴ�.","�ڵ��α��� ����",JOptionPane.INFORMATION_MESSAGE);
						}
						catch (Exception e1) {
							System.out.println("���� ���� ����");
						}
					}
				}//�ڵ� �α��� �����ߴٸ� 
				else {//������ ����
					File file = new File(LoginDialog.path+"auto_login.txt");
					File file1 = new File(LoginDialog.path+"rem_info.txt");
					if(file.delete()) {
						JOptionPane.showMessageDialog(frame,"�ڵ��α��� �����Ͽ����ϴ�.","�ڵ��α��� ����",JOptionPane.INFORMATION_MESSAGE);
					} 
					if(file1.delete()) {
						System.out.println("������������ ���� ���� ����");
					}
					else {
						System.out.println("���� ���� ����");
					}
				}
			}
		};
		s_i1.addItemListener(auto_login);

		JCheckBoxMenuItem s_i2 = new JCheckBoxMenuItem("�������� ���� ���� �����ϱ�(V)");
		s_i2.setMnemonic('V');
		s_i2.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					infoC.setSelected(true);
					infoC.setEnabled(false);
				}
				else {
					infoC.setEnabled(true);
				}
			}
		});
		ms.add(s_i1); ms.add(s_i2);

	}

	// ����Ʈ ��� ��
	private JPanel createSiteList() {
		JPanel p = new JPanel(new BorderLayout());

		p.add(SearchSort(), BorderLayout.NORTH);
		p.add(new JScrollPane(recordTable()));
		p.add(infoCheck(), BorderLayout.SOUTH);
		return p;
	}

	// ����Ʈ ��� ��-�˻�/����
	private JPanel SearchSort() {
		JPanel p = new JPanel();
		createBorder(p, "�˻�/����");

		JPanel t1 = new JPanel(); t1.add(createSearch());
		JPanel t2 = new JPanel(); t2.add(createSort());
		p.add(t1); p.add(t2);
		return p;
	}

	// ����Ʈ ��� ��-���̺� ���
	private JTable recordTable() {
		mT = new JTable();
		//model = new InfoTableModel(sil);
		mT.setModel(model); 
		mT.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // ���ϼ���

		sorter = new TableRowSorter<InfoTableModel>(model);
		mT.setRowSorter(sorter);

		java.util.List <RowSorter.SortKey> sortKeys = new java.util.ArrayList<RowSorter.SortKey>();
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
		sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
		sorter.setSortKeys(sortKeys); 

		return mT;
	}

	private JPanel infoCheck() {
		JPanel p = new JPanel(new BorderLayout());
 
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JLabel info_id = new JLabel("���̵�");
		info_idL = new JTextField(10);

		JLabel info_pw = new JLabel("��й�ȣ");
		info_pwL = new JTextField(10);

		info_idL.setEditable(false); info_pwL.setEditable(false);

		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		info_del = new JButton("����");
		info_del.setEnabled(false);

		info_del.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//int index = mT.getSelectedRow();

				if(modelRow < 0){
					System.out.println("������ ���� ������ �ּ���.");
				}else{
					model.removeRow(sil, modelRow);
					sil.showAllSiteInfo();

					// ���� �� ���� �г� ��ĭ
					siteF.setText("");
					urlF.setText("");
					idF.setText("");
					pwF.setText("");
					memoT.setText("");
					cateCB.setSelectedIndex(0);
					preferCB.setSelectedIndex(0);
					info_idL.setText("");
					info_pwL.setText("");

					urlF.setEditable(true);
					newbtn.setEnabled(true);
					card.show(cardP, "�Է�");
					SiteSize.setText(sil.size()+"���� ����Ʈ�� ��ϵǾ��ֽ��ϴ�.");

					checkSite();
					infoC.setSelected(false);
					modelRow = -999;
				}
			}
		});
		infoC.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String viewInfo = ((JCheckBox)e.getItem()).getText();

				if(modelRow!=-999) {
					if(e.getStateChange()==ItemEvent.SELECTED) {
						System.out.println(viewInfo+" ����");
						//info_idL.setEditable(true); info_pwL.setEditable(true);
						info_idL.setText(tmpId); 
						info_pwL.setText(tmpPw);
						info_del.setEnabled(true);
						System.out.println(tmpId+"\t"+tmpPw);
					}
					else {
						System.out.println(viewInfo+" ����");
						//info_idL.setEditable(true); info_pwL.setEditable(true);
						info_idL.setText(""); 
						info_pwL.setText("");
						info_del.setEnabled(false);
					}
				}
				else {
					System.out.println("���̺��� Ŭ�����ּ���!");
				}
				//info_idL.setEditable(false); info_pwL.setEditable(false);
			}
		});
		p1.add(infoC); 
		p1.add(info_id); p1.add(info_idL);
		p1.add(info_pw); p1.add(info_pwL);

		p2.add(info_del);

		p.add(p1); p.add(p2, BorderLayout.EAST);

		return p;
	}

	private JPanel createSearch() {
		JPanel searchP = new JPanel();
		createBorder(searchP, "�˻�");

		searchCB = new JComboBox<String>(searchD);
		searchCB.setSelectedIndex(0);
		searchCB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				newFilter();
			}
		});
		searchP.add(searchCB);

		JLabel filter = new JLabel("����:");
		filterin = new JTextField(5);
		filterin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newFilter();

				filterin.setText("");
			}
		});
		searchP.add(filter); searchP.add(filterin);

		return searchP;
	}

	private JPanel createSort() {
		JPanel sortP = new JPanel();
		createBorder(sortP, "����");

		sortCateCB = new JComboBox<String>(sortCateD);
		sortSiteCB = new JComboBox<String>(sortSiteD);

		sortCateCB.setSelectedIndex(0);
		sortSiteCB.setSelectedIndex(0);
		sortP.add(sortCateCB); sortP.add(sortSiteCB);

		JButton sortBtn = new JButton("����");
		sortBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str1 = (String)sortCateCB.getSelectedItem();
				String str2 = (String)sortSiteCB.getSelectedItem();
				RowFilter<InfoTableModel, Object> firstFilter;
				RowFilter<InfoTableModel, Object> secondFilter;

				if(str1.equals("�з�"))
					firstFilter = RowFilter.regexFilter("", 0);
				else firstFilter = RowFilter.regexFilter(str1, 0);
				if(str2.equals("����Ʈ �̸�"))
					secondFilter = RowFilter.regexFilter("", 2);
				else secondFilter = RowFilter.regexFilter(str2, 2);

				ArrayList<RowFilter<InfoTableModel, Object>> filters = new ArrayList<RowFilter<InfoTableModel, Object>>();
				filters.add(firstFilter);
				filters.add(secondFilter);

				RowFilter<InfoTableModel, Object>  compoundRowFilter = RowFilter.andFilter(filters);
				sorter.setRowFilter(compoundRowFilter);

				sortCateCB.setSelectedIndex(0);
				sortSiteCB.setSelectedIndex(0);
			}
		});

		JButton basicBtn = new JButton("�⺻");
		basicBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sorter.setRowFilter(null);
				sortCateCB.setSelectedIndex(0);
				sortSiteCB.setSelectedIndex(0);
			}
		});
		sortP.add(sortBtn); sortP.add(basicBtn);

		return sortP;
	}

	// ���̺� ���� �� �Է�/���� ���� �̺�Ʈ ó��
	private void registerTableSelectionEventListener() {

		mT.getSelectionModel().addListSelectionListener( 
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent e) {
						InfoTableModel itm = (InfoTableModel)mT.getModel();
						viewRow = mT.getSelectedRow(); // ������ row���� �� ��
						System.out.println(viewRow);


						if (viewRow < 0) {
							System.out.println("Selection got filtered away");
						}
						else {
							modelRow = mT.convertRowIndexToModel(viewRow); // ���� �ο찪���� ��ȯ
							System.out.printf("Selected Row in view: %d. ", viewRow);
							System.out.printf("Selected Row in model: %d.\n", modelRow);

							urlF.setEditable(false); // �ּ� �����Ұ�
							SiteInfo selectInfo = itm.getSelectedCell(modelRow);
							siteF.setText(selectInfo.site);
							urlF.setText(selectInfo.url);
							idF.setText(selectInfo.id); 

							// ������������ Ŭ�� �� ����� ������ �ӽ� ����
							tmpId = selectInfo.id;
							tmpPw = selectInfo.pwd;

							cateCB.setSelectedItem(selectInfo.sitecate);
							preferCB.setSelectedItem(selectInfo.prefer);
							memoT.setText(selectInfo.memo);

							editbtn.setEnabled(true);
							info_del.setEnabled(true);
							card.show(cardP, "����");

							System.out.println(infoC.isSelected()+tmpId);
							// �������� ���� �κ�
							if(infoC.isSelected()) {
								info_idL.setText(tmpId); 
								info_pwL.setText(tmpPw);
								info_del.setEnabled(true);
							}
							else {
								info_idL.setText(""); 
								info_pwL.setText("");
								info_del.setEnabled(false);
							}

							newbtn.setEnabled(false);
						}
					}

				}
				);
	}

	private void newFilter() {
		RowFilter<InfoTableModel, Object> firstFilter = null;
		RowFilter<InfoTableModel, Object> secondFilter = null;
		ArrayList<RowFilter<InfoTableModel, Object>> filters = new ArrayList<RowFilter<InfoTableModel, Object>>();
		String selectC = (String)searchCB.getSelectedItem();

		try {
			if(!selectC.equals("��ü")) 
				firstFilter = RowFilter.regexFilter(selectC, 0);
			else  // ��ü�� ���,
				firstFilter = RowFilter.regexFilter("", 0);

			secondFilter = RowFilter.regexFilter(filterin.getText(), 3);
			System.out.println(selectC+"&"+filterin.getText());
		} catch (java.util.regex.PatternSyntaxException e) {
			return;
		}
		filters.add(firstFilter);
		filters.add(secondFilter);

		RowFilter<InfoTableModel, Object>  compoundRowFilter = RowFilter.andFilter(filters);
		sorter.setRowFilter(compoundRowFilter);
	}

	// �����Ȳ ��
	private JPanel createRegister() { 
		scoreplot plot = new scoreplot();
		JPanel p = new JPanel(new BorderLayout());
		p.add(plot);
		return p;
	}

	class scoreplot extends JComponent{
		@Override
		public void paint(Graphics g) { 
			//super.paint(g);
			SiteInfo S_info= new SiteInfo();  
			int size = sil.size(); 
			int sum=0; // �ʱⰪ 0
			HashMap<String,Integer> hm = new HashMap<String,Integer>();
			Vector<String> v = new Vector<String>();
			int name_size=0;

			for (int i = 0; i < size; i++) {
				S_info=sil.getSiteInfo(i);
				int s_prefer = S_info.getPrefer();
				if(hm.containsKey(S_info.getSite())) {//�̸� �ߺ��̶��
					int prefer = hm.get(S_info.getSite());
					prefer+=s_prefer;
					hm.put(S_info.getSite(), prefer);  
				}
				else {//�̸� �ߺ� �ƴ϶�� 
					v.addElement(S_info.getSite());
					hm.put(S_info.getSite(),S_info.getPrefer());
					name_size++;
				}
			} 
			arcAngle = new int[name_size];   
			data = new int[name_size];  
			arcAngle = new int[name_size];    

			int index=0;
			hm.keySet();
			Set<String> keys = hm.keySet();     
			Iterator<String> it = keys.iterator();
			while(it.hasNext()) { 
				String key = it.next();
				data[index]= hm.get(key);
				sum+=data[index];
				index++;
			}

			for(int i=0;i<data.length;i++){ 
				arcAngle[i] = (int)Math.round((double)data[i]/(double)sum*360);
			}

			int startAngle = 0;
			g.setFont(new Font("Arial",Font.BOLD,20));
			g.drawString("<Preference>",250,460);  

			g.setFont(new Font("Arial",Font.ITALIC,13));
			for(int i=0;i<data.length;i++){
				g.setColor(color[i]);
				g.drawString(v.get(i)+"  "+Math.round(arcAngle[i]*100/360)+"%", 50+i*100,30);
				g.fillArc(135,60,360,360,startAngle,arcAngle[i]);
				startAngle = startAngle + arcAngle[i];
			} 
		}  
	} 

	private void createBorder(JPanel p, String str) {
		p.setBorder(new TitledBorder(new LineBorder(Color.gray),str));
	}

	private void sureClosing() {//���� ó��
		int n = JOptionPane.showConfirmDialog(frame, "���� �����Ͻðڽ��ϱ�?","����",JOptionPane.OK_CANCEL_OPTION);//Ȯ��,���
		if(n==JOptionPane.OK_OPTION)
			System.exit(0);
	}

	private void checkSite() { // �����Ͱ� �����Ǹ� �޺��ڽ����� ����
		Vector<String> tmpsite = new Vector<String>(); // table�� ��ϵ� ����Ʈ �̸� ���� ����
		for(int i=0; i<sil.size(); i++) {
			SiteInfo tmps = sil.getSiteInfo(i); // ����Ʈ ���� ������
			tmpsite.add(tmps.site);
		}

		for(int i=1; i<sortSiteD.size(); i++) {
			if(!tmpsite.contains(sortSiteD.get(i))) // '����Ʈ �̸� �޺��ڽ�' �� ���̺� ����� ����Ʈ �̸��� ������ ������
				sortSiteD.remove(i); // ����
		}
	}

	private boolean checkSiteName(String t) { // �����Ϳ� ������ �̸��� ����Ʈ�� ������ ��� ���â
		Vector<String> tmpsite = new Vector<String>(); // table�� ��ϵ� ����Ʈ �̸� ���� ����
		for(int i=0; i<sil.size(); i++) {
			SiteInfo tmps = sil.getSiteInfo(i); // ����Ʈ ���� ������
			tmpsite.add(tmps.site);
		}

		if(tmpsite.contains(t)) { // �̹� ���� �̸� ������ ���,
			return true;
		}
		return false;
	}
}
