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
	private HashSet<SiteCategory> itemsC= new HashSet<SiteCategory>(); // 해당 아이디의 카테고리 정보
	private Vector<String> addInfoD = new Vector<String>(); // 추가정보의 분류 콤보박스 데이터
	private Vector<String> searchD = new Vector<String>(); // 검색/정렬의 검색 콤보박스 데이터
	private Vector<String> sortCateD = new Vector<String>(); // 정렬의 분류 콤보박스 데이터
	private Vector<String> sortSiteD = new Vector<String>(); // 정렬의 사이트 이름 콤보박스 데이터

	private JComboBox<String> cateCB; // 왼쪽 화면 분류의 콤보박스
	private JComboBox<String> searchCB, sortCateCB, sortSiteCB, preferCB; // 오른쪽 화면 구성의 콤보박스 3개
	private JTable mT;
	private TableRowSorter<InfoTableModel> sorter;

	private JLabel SiteSize = new JLabel();
	// 왼쪽 멤버변수
	private JTextField siteF,urlF,idF;
	private JPasswordField pwF;
	private JTextArea memoT;
	private JButton newbtn,inputbtn,editbtn;

	// 오른쪽 멤버변수
	private JButton info_del;
	private int viewRow;
	private int modelRow=-999;
	private JTextField info_idL, info_pwL; //계정정보 보기 text필드
	private String tmpId=null, tmpPw=null;
	private JCheckBox infoC = new JCheckBox("계정정보 보기");
	private InfoTableModel model = new InfoTableModel(sil);
	private JTextField filterin; // 필터링

	private CardLayout card=new CardLayout();
	private JPanel cardP;

	private JCheckBoxMenuItem s_i1 = new JCheckBoxMenuItem("자동 로그인(L)");;
	private JMenuItem m_i1 = new JMenuItem("사용자(U)");
	private int[] data; 
	private int[] arcAngle; 
	private Color[] color = {Color.RED, Color.BLUE, 
			Color.MAGENTA, Color.ORANGE,Color.LIGHT_GRAY,Color.PINK,Color.YELLOW}; 

	SiteInfoManager(){ 
		frame = new JFrame("전체");
		
		//파일 입출력해서 자동로그인인지 확인
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
				System.out.println("신규 오류");
			}
			//자동로그인 체크되게
			s_i1.setSelected(true); 
			
			//계정 정보 체크 파일 있다면
			File rem_info = new File(LoginDialog.path+"rem_info.txt");
			if(rem_info.exists()) {//계정 정보 보기 체크 true일 때
				System.out.println("rem_info 파일 있음");
				infoC.setSelected(true);
			} 
		} 
		//파일이 존재하지 않다면
		else { 
			LoginDialog dlg = new LoginDialog(frame);
			dlg.setVisible(true);
		}

		//엑셀 파일 보이기 
		File f = new File(LoginDialog.path1);  
		File[] fileList = f.listFiles();
		String[] subFName = new String[fileList.length];

		for (int i = 0; i < fileList.length; i++) {
			File subF = fileList[i];
			subFName[i]=subF.getName();//모든 파일 이름 담기
			System.out.println(subFName[i]);
		}  

		//아이디에 따른 엑셀 파일 찾기 
		sortSiteD.add("사이트 이름");
		for (int i = 0; i < subFName.length; i++) {
			if(subFName[i].contains(SiteCategoryList.getId()+"Excel.xlsx")) {
				//엑셀 파일 있다면 가져오기  
				System.out.println("엑셀 파일이 있습니다. ");
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
				//시트 수 (첫번째에만 존재하므로 0을 준다)
				//만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
				XSSFSheet sheet=workbook.getSheetAt(0);
				//행의 수
				int rows=sheet.getPhysicalNumberOfRows();
				for(rowindex=1;rowindex<rows;rowindex++){
					//행을읽는다
					XSSFRow row=sheet.getRow(rowindex);
					exceldata = new String[7];
					if(row !=null){
						//셀의 수
						int cells=row.getPhysicalNumberOfCells();
						for(columnindex=0;columnindex<=cells;columnindex++){
							//셀값을 읽는다
							XSSFCell cell=row.getCell(columnindex);

							//셀이 빈값일경우를 위한 널체크
							if(cell==null){
								continue;
							}else{
								//타입별로 내용 읽기
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
							System.out.println("각 셀 내용 :"+exceldata[columnindex]); // 수정 필요 - 테이블 뷰랑 모델 수정
							exceldata[columnindex] = exceldata[columnindex].trim();
						}
					}
					excelinfo = new SiteInfo(exceldata);
					sil.addSiteInfo(excelinfo);//sil에 엑셀 내용 담기
					model.addRow(sil, excelinfo);
					SiteSize.setText(sil.size()+"개의 사이트가 등록되어있습니다.");
					//sortSiteD.add(exceldata[0]); 
					if(!sortSiteD.contains(exceldata[0])) // 포함하고 있지 않으면
						sortSiteD.add(exceldata[0]);    // 추가
				}
				try {
					fis.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}//if
			else{//없다면 
				isExcel=false;
				continue;
			}
		}

		frame.setTitle(SiteCategoryList.getId()+"(이)가 만든 인터넷 계정관리 v0.51");
		if(SiteCategoryList.getId().equals("manager"))//매니저 인지 확인
			m_i1.setEnabled(true);
		else 
			m_i1.setEnabled(false);

		readData();
		buildGUI();
		createMenu();
		registerTableSelectionEventListener();

		frame.setSize(1000,600);
		frame.setLocation(300,50);

		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);//닫기를 무효화
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

		p2.add(createInfoP(),BorderLayout.NORTH); // 추가정보
		p2.add(createbtnP()); // 버튼
		p.add(createDetailInfoP(),BorderLayout.NORTH);
		p.add(p2);

		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JPanel total = new JPanel(new BorderLayout());
		total.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),"입력/수정"));
		total.add(p,BorderLayout.NORTH);
		left.add(total,BorderLayout.WEST);
		left.add(p1,BorderLayout.SOUTH);
		left.add(new JPanel(),BorderLayout.CENTER);
		return left;
	}

	private void readData() {
		SiteCategoryList s = new SiteCategoryList();  
		s.setCategory(LoginDialog.path,SiteCategoryList.getId()); 
		itemsC= SiteCategoryList.getItems(); //현재 아이디의 카테고리

		//id에 따른 카테고리 출력
		Iterator<SiteCategory> it1 = itemsC.iterator();
		SiteCategory t;
		while(it1.hasNext()) {
			t = it1.next();
			addInfoD.add(t.toString()); // 카테고리를 모델에 적용
		}   

		searchD.add("전체");
		searchD.addAll(addInfoD);

		sortCateD.add("분류");
		sortCateD.addAll(addInfoD);

		sil.showAllSiteInfo();
	}

	private JPanel createDetailInfoP() {//기본 정보
		JPanel sNameP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		siteF= new JTextField(15);
		siteF.addKeyListener(editField);
		urlF= new JTextField(10);
		urlF.addKeyListener(editField);
		idF= new JTextField(8);
		idF.addKeyListener(editField);
		pwF= new JPasswordField(8);
		pwF.addKeyListener(editField);

		sNameP.add(new JLabel("사이트명"));
		sNameP.add(siteF);

		JPanel sURLP= new JPanel(new FlowLayout(FlowLayout.LEFT));
		sURLP.add(new JLabel("주소(URL) http:// "));
		sURLP.add(urlF);

		JPanel IdP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		IdP.add(new JLabel("아 이 디"));
		IdP.add(idF);

		JPanel pwdP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pwdP.add(new JLabel("비밀번호"));
		pwF.setText("");
		pwdP.add(pwF);

		JPanel totalP1 = new JPanel(new GridLayout(4,0));
		totalP1.add(sNameP);
		totalP1.add(sURLP);
		totalP1.add(IdP);
		totalP1.add(pwdP);
		totalP1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),"기본 정보"));
		return totalP1;
	}

	// 좌측 텍스트필드 값 수정 시, 새로작성 버튼 활성화: 좌측 패널에서 어느 항목이든 값이 바뀌거나 수정되는 순간 활성
	private KeyListener editField = new KeyAdapter(){
		@Override
		public void keyPressed(KeyEvent e) {
			newbtn.setEnabled(true);
		}
	};

	private JPanel createInfoP() {//추가 정보
		JPanel groupP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		groupP.add(new JLabel("분     류"));
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
		preferP.add(new JLabel("선 호 도"));
		Vector<String> v2 = new Vector<String>();
		preferCB = new JComboBox<String>(v2);
		v2.add("미지정");
		v2.add("☆☆☆☆☆");
		v2.add("☆☆☆☆");
		v2.add("☆☆☆");
		v2.add("☆☆");
		v2.add("☆");
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
		memo.add(new JLabel("메     모"));
		memo.add(memoT);

		JPanel totalP2 = new JPanel(new GridLayout(2,0));
		totalP2.add(groupP);
		totalP2.add(preferP);

		JPanel t = new JPanel(new BorderLayout());
		t.add(totalP2, BorderLayout.NORTH);
		t.add(memo);

		t.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),"추가 정보"));

		return t;
	}

	private JPanel createbtnP() {
		JPanel btnP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		cardP = new JPanel(card);
		newbtn= new JButton("새로 작성(N)");
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

		inputbtn = new JButton("입력(I)");
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
					JOptionPane.showMessageDialog(frame, "같은 사이트가 이미 존재합니다!", "경고", JOptionPane.WARNING_MESSAGE);
				}
				else {
					if(checkSiteName(tmp[0]))
						JOptionPane.showMessageDialog(frame, "동일한 사이트명이 이미 존재합니다!", "경고", JOptionPane.WARNING_MESSAGE);
					else {
						if(!tmp[5].equals("미지정")) {
							model.addRow(sil, si);

							if(!sortSiteD.contains(tmp[0])) // 포함하고 있지 않으면
								sortSiteD.add(tmp[0]);    // 추가
							//sortSiteD.add(tmp[0]);

							// 초기화
							siteF.setText("");
							urlF.setText("");
							idF.setText("");
							pwF.setText("");
							memoT.setText("");
							cateCB.setSelectedIndex(0);
							preferCB.setSelectedIndex(0);
							System.out.println("==========================");
							sil.showAllSiteInfo();
							SiteSize.setText(sil.size()+"개의 사이트가 등록되어있습니다.");
						}
						else {
							JOptionPane.showMessageDialog(frame, "선호도를 선택해주세요!", "경고", JOptionPane.WARNING_MESSAGE);
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
		cardP.add("입력",inputbtn);

		editbtn = new JButton("수정(E)");
		editbtn.setMnemonic('E');
		editbtn.setEnabled(false);
		editbtn.addActionListener(new ActionListener() { // 표 내용 수정
			@Override
			public void actionPerformed(ActionEvent e) { 
				// siteF,urlF,idF,pwF, memoT + comboB 2개
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

				if(!tmp[5].equals("미지정")) {
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
					card.show(cardP, "입력");

					infoC.setSelected(false);
					info_idL.setText("");
					info_pwL.setText("");
					checkSite();
					modelRow = -999; // 표 수정한 뒤 새로 입력 누르면 url도 지우도록
				}
				else {
					JOptionPane.showMessageDialog(frame, "선호도를 선택해주세요!", "경고", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		cardP.add("수정",editbtn);

		btnP.add(cardP);

		return btnP;
	}

	private void buildGUI() {
		JPanel p = new JPanel(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.addTab("사이트 목록", createSiteList());
		tabbedPane.addTab("등록 현황", createRegister());

		p.add(tabbedPane);

		frame.add(buildGUIL(),BorderLayout.WEST);
		frame.add(p);
		frame.add(createBottom(), BorderLayout.SOUTH);
	}

	private JPanel createBottom() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		int siteNum = sil.size();
		SiteSize.setText(siteNum+"개의 사이트가 등록되어있습니다.");
		p.add(SiteSize);

		return p;
	}

	private void createMenu() {
		JMenuBar mb = new JMenuBar();
		frame.setJMenuBar(mb);

		// JMenu 구성
		JMenu mf = new JMenu("파일(F)");
		JMenu mm = new JMenu("관리(M)");
		JMenu ms = new JMenu("설정(S)");

		mf.setMnemonic('F'); mm.setMnemonic('M'); ms.setMnemonic('S');
		mb.add(mf); mb.add(mm); mb.add(ms);

		// 1. 파일 MenuItem 구성
		JMenuItem f_i1 = new JMenuItem("엑셀파일에서 가져오기(I)");
		f_i1.setMnemonic('I'); f_i1.setEnabled(true);
		f_i1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isSiteName = false; // 동일 사이트명있는가
				boolean isSiteCate = false; // 사용자 분리목록에 있는가
				JFileChooser fileChooser = new JFileChooser(); 

				//기본 경로 = 바탕화면
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "//" + "Desktop")); 

				FileNameExtensionFilter filter = new FileNameExtensionFilter("xlsx 파일", "xlsx"); 
				// 확장자 추가 
				fileChooser.setFileFilter(filter); 

				//파일오픈 다이얼로그 를 띄움 
				int result = fileChooser.showOpenDialog(frame); 
				System.out.println(result); 

				if (result == JFileChooser.APPROVE_OPTION) { 
					//선택한 파일의 경로 반환 
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
					//시트 수 (첫번째에만 존재하므로 0을 준다)
					//만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
					XSSFSheet sheet=workbook.getSheetAt(0);
					//행의 수
					int rows=sheet.getPhysicalNumberOfRows();
					for(rowindex=1;rowindex<rows;rowindex++){
						//행을읽는다
						XSSFRow row=sheet.getRow(rowindex);
						exceldata = new String[7];
						if(row !=null){
							//셀의 수
							int cells=row.getPhysicalNumberOfCells();
							for(columnindex=0;columnindex<=cells;columnindex++){
								//셀값을 읽는다
								XSSFCell cell=row.getCell(columnindex);

								//셀이 빈값일경우를 위한 널체크
								if(cell==null){
									continue;
								}else{
									//타입별로 내용 읽기
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
								System.out.println("각 셀 내용 :"+exceldata[columnindex]); // 수정 필요 - 테이블 뷰랑 모델 수정
								exceldata[columnindex] = exceldata[columnindex].trim();
							}
						}
						excelinfo = new SiteInfo(exceldata);
						if(!checkSiteName(excelinfo.site)) { // 동일한 사이트 명이 없을 경우
							if(addInfoD.contains(excelinfo.sitecate)) { // 사용자의 사이트 분류목록에 정보가 있을 경우
								model.addRow(sil, excelinfo);
								SiteSize.setText(sil.size()+"개의 사이트가 등록되어있습니다.");
								if(!sortSiteD.contains(exceldata[0])) // 포함하고 있지 않으면
									sortSiteD.add(exceldata[0]);    // 추가
							}
							else {
								isSiteCate = true; // 사용자의 사이트 분류목록에 없는 데이터
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
						JOptionPane.showMessageDialog(frame, "동일한 사이트 명을 제외하고 "+selectedFile.getName()+" 파일을 가져왔습니다. ", "파일 불러오기", JOptionPane.INFORMATION_MESSAGE);
					else
						JOptionPane.showMessageDialog(frame, selectedFile.getName()+" 파일을 가져왔습니다. ", "파일 불러오기", JOptionPane.INFORMATION_MESSAGE);
					
					if(isSiteCate)
						JOptionPane.showMessageDialog(frame, "사용자의 사이트 분류 목록에 존재하지 않는 데이터가 있습니다. 해당 데이터를 제외하고 가져옵니다.", "경고", JOptionPane.WARNING_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(frame, "파일을 선택하지 않았습니다", "경고", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
		});

		JMenuItem f_i2 = new JMenuItem("엑셀파일로 내보내기(E)");
		f_i2.setMnemonic('E'); f_i2.setEnabled(true);
		f_i2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(); 

				//기본 경로 = 바탕화면
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "//" + "Desktop")); 
				FileNameExtensionFilter filter = new FileNameExtensionFilter("xlsx 파일", "xlsx"); 
				// 확장자 추가 
				fileChooser.setFileFilter(filter); 
				//파일오픈 다이얼로그 를 띄움 
				int result = fileChooser.showSaveDialog(frame); 
				File selectedFile;
				
				if (result == JFileChooser.APPROVE_OPTION) { 
					//선택한 파일의 경로 반환 
					selectedFile = fileChooser.getSelectedFile(); 
					// 엑셀파일 
					XSSFWorkbook workbook = new XSSFWorkbook();
					XSSFSheet sheet2 = workbook.createSheet("sheet");
	
					//3.row 생성
					XSSFRow row0 = sheet2.createRow(0);
					//4.cell 생성
					row0.createCell(0).setCellValue("사이트명");
					row0.createCell(1).setCellValue("주소");
					row0.createCell(2).setCellValue("아이디");
					row0.createCell(3).setCellValue("비밀번호");
					row0.createCell(4).setCellValue("분류");
					row0.createCell(5).setCellValue("선호도");
					row0.createCell(6).setCellValue("메모");
	
					//5.data 셋팅
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
					//6.workbook을 파일로 쓰기
					try {
						//매개변수 스트링으로 "경로/제목" 넣어줘도됨.
						FileOutputStream fos = new FileOutputStream(selectedFile.getPath()+".xlsx"); //엑셀 파일생성
						workbook.write(fos);//workbook을 fos 에 쓴다.
						fos.flush();
						fos.close();
						JOptionPane.showMessageDialog(frame, "Excel 파일로 저장하였습니다.", "파일 저장", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				else{
					selectedFile = null;
					JOptionPane.showMessageDialog(frame, "파일을 저장하지 않았습니다", "경고", JOptionPane.WARNING_MESSAGE);
		        }
			}//이벤트
		});

		JMenuItem f_i3 = new JMenuItem("저장(S)");
		f_i3.setMnemonic('S');
		f_i3.addActionListener(new ActionListener() {//저장 버튼을 누르면
			@Override
			public void actionPerformed(ActionEvent e) {
				//엑셀 파일에 저장 
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet2 = workbook.createSheet("sheet");

				//3.row 생성
				XSSFRow row0 = sheet2.createRow(0);
				//4.cell 생성
				row0.createCell(0).setCellValue("사이트명");
				row0.createCell(1).setCellValue("주소");
				row0.createCell(2).setCellValue("아이디");
				row0.createCell(3).setCellValue("비밀번호");
				row0.createCell(4).setCellValue("분류");
				row0.createCell(5).setCellValue("선호도");
				row0.createCell(6).setCellValue("메모");

				//5.data 셋팅
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
				//6.workbook을 파일로 쓰기
				try {
					//매개변수 스트링으로 "경로/제목" 넣어줘도됨.
					FileOutputStream fos = new FileOutputStream(LoginDialog.path+"\\"+SiteCategoryList.getId()+"Excel.xlsx"); //엑셀 파일생성
					workbook.write(fos);//workbook을 fos 에 쓴다.
					fos.flush();
					fos.close();
					JOptionPane.showMessageDialog(frame, SiteCategoryList.getId()+"Excel.xlsx 파일로 저장하였습니다.", "파일 저장", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		JMenuItem f_i4 = new JMenuItem("로그아웃(O)");
		f_i4.setMnemonic('O');

		f_i4.addActionListener(new ActionListener() {//로그아웃을 클릭하면 
			@Override
			public void actionPerformed(ActionEvent e) {
				//수정된 자료 저장하고 로그아웃을 할건지 물어본다.
				int n = JOptionPane.showConfirmDialog(frame,"수정된 자료를 저장하시겠습니까?","확인",JOptionPane.YES_NO_CANCEL_OPTION);
				switch(n) {
				case JOptionPane.YES_OPTION://자료 저장
					//자료 저장 부분
					XSSFWorkbook workbook = new XSSFWorkbook();
					XSSFSheet sheet2 = workbook.createSheet("sheet");

					//3.row 생성
					XSSFRow row0 = sheet2.createRow(0);
					//4.cell 생성
					row0.createCell(0).setCellValue("사이트명");
					row0.createCell(1).setCellValue("주소");
					row0.createCell(2).setCellValue("아이디");
					row0.createCell(3).setCellValue("비밀번호");
					row0.createCell(4).setCellValue("분류");
					row0.createCell(5).setCellValue("선호도");
					row0.createCell(6).setCellValue("메모");

					//5.data 셋팅
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
					//6.workbook을 파일로 쓰기
					try {
						//매개변수 스트링으로 "경로/제목" 넣어줘도됨.
						FileOutputStream fos = new FileOutputStream(LoginDialog.path+"\\"+SiteCategoryList.getId()+"Excel.xlsx"); //엑셀 파일생성
						workbook.write(fos);//workbook을 fos 에 쓴다.
						fos.flush();
						fos.close();
						JOptionPane.showMessageDialog(frame, SiteCategoryList.getId()+"Excel.xlsx 파일로 저장하였습니다.", "파일 저장", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					frame.setVisible(false); 
					new SiteInfoManager();
					break;
				case JOptionPane.NO_OPTION:
					//그냥 로그아웃만 
					frame.setVisible(false); 
					new SiteInfoManager();
					break;
				case JOptionPane.CANCEL_OPTION:
					break;
				}   
			}
		});

		JMenuItem f_i5 = new JMenuItem("종료(X)");
		f_i5.setMnemonic('X');
		f_i5.addActionListener(new ActionListener() {//종료버튼을 누르면 
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(frame,"수정된 자료를 저장하시겠습니까?","확인",JOptionPane.YES_NO_CANCEL_OPTION);
				switch(n) {
				case JOptionPane.YES_OPTION://자료 저장
					//자료 저장 부분
					XSSFWorkbook workbook = new XSSFWorkbook();
					XSSFSheet sheet2 = workbook.createSheet("sheet");

					//3.row 생성
					XSSFRow row0 = sheet2.createRow(0);
					//4.cell 생성
					row0.createCell(0).setCellValue("사이트명");
					row0.createCell(1).setCellValue("주소");
					row0.createCell(2).setCellValue("아이디");
					row0.createCell(3).setCellValue("비밀번호");
					row0.createCell(4).setCellValue("분류");
					row0.createCell(5).setCellValue("선호도");
					row0.createCell(6).setCellValue("메모");

					//5.data 셋팅
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
					//6.workbook을 파일로 쓰기
					try {
						//매개변수 스트링으로 "경로/제목" 넣어줘도됨.
						FileOutputStream fos = new FileOutputStream(LoginDialog.path+"\\"+SiteCategoryList.getId()+"Excel.xlsx"); //엑셀 파일생성
						workbook.write(fos);//workbook을 fos 에 쓴다.
						fos.flush();
						fos.close();
						JOptionPane.showMessageDialog(frame, SiteCategoryList.getId()+"Excel.xlsx 파일로 저장하였습니다.", "파일 저장", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e1) {
						e1.printStackTrace();
					} 
					System.exit(0);
					break;
				case JOptionPane.NO_OPTION:
					//그냥 종료만
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

		// 2. 관리 MenuItem 구성
		m_i1.setMnemonic('U');
		m_i1.addActionListener(new ActionListener() {//사용자 버튼을 클릭하면
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ManagerDialog md = new ManagerDialog(frame);
				md.setVisible(true); 
			}
		});

		JMenuItem m_i2 = new JMenuItem("사이트 분류(C)");
		m_i2.setMnemonic('C');
		mm.add(m_i1); mm.add(m_i2);

		m_i2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CategoryManagerDialog cgm_dlg = new CategoryManagerDialog(frame); 
				cgm_dlg.setVisible(true);
				addInfoD.removeAllElements();
				Iterator<SiteCategory> it1 = itemsC.iterator();//다시 설정
				SiteCategory t;
				while(it1.hasNext()) {
					t = it1.next();
					addInfoD.add(t.toString()); // 카테고리를 모델에 적용
				}    

				searchD.removeAllElements();
				searchD.add("전체");
				searchD.addAll(addInfoD);

				sortCateD.removeAllElements();
				sortCateD.add("분류");
				sortCateD.addAll(addInfoD);

				cateCB.updateUI();
				searchCB.updateUI();
				sortCateCB.updateUI();  
			}
		});

		// 3. 설정 MenuItem 구성
		s_i1.setMnemonic('L');

		ItemListener auto_login= new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {//선택된 경우라면
					//계정 정보 보기 여부
					if(infoC.isSelected()) {
						FileWriter fwinfoC;
						try {
							fwinfoC = new FileWriter(LoginDialog.path+"rem_info.txt", true); 
							fwinfoC.write("true");
							fwinfoC.flush(); 
							fwinfoC.close();
							
							FileWriter fw = new FileWriter(LoginDialog.path+"auto_login.txt", true);//저장할 아이디 파일 생성
							fw.write(SiteCategoryList.getId());//pwd 내용을 저장 
							fw.flush(); 
							fw.close();
							JOptionPane.showMessageDialog(frame,"자동로그인 설정하였습니다.","자동로그인 설정",JOptionPane.INFORMATION_MESSAGE);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else {//여부 체크 안 했다면 
						try {
							FileWriter fw = new FileWriter(LoginDialog.path+"auto_login.txt", true);//저장할 아이디 파일 생성
							fw.write(SiteCategoryList.getId());//pwd 내용을 저장 
							fw.flush(); 
							fw.close();
							JOptionPane.showMessageDialog(frame,"자동로그인 설정하였습니다.","자동로그인 설정",JOptionPane.INFORMATION_MESSAGE);
						}
						catch (Exception e1) {
							System.out.println("파일 저장 오류");
						}
					}
				}//자동 로그인 선택했다면 
				else {//선택을 해제
					File file = new File(LoginDialog.path+"auto_login.txt");
					File file1 = new File(LoginDialog.path+"rem_info.txt");
					if(file.delete()) {
						JOptionPane.showMessageDialog(frame,"자동로그인 해제하였습니다.","자동로그인 해제",JOptionPane.INFORMATION_MESSAGE);
					} 
					if(file1.delete()) {
						System.out.println("계정정보보기 여부 파일 삭제");
					}
					else {
						System.out.println("파일 삭제 오류");
					}
				}
			}
		};
		s_i1.addItemListener(auto_login);

		JCheckBoxMenuItem s_i2 = new JCheckBoxMenuItem("계정정보 보기 상태 유지하기(V)");
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

	// 사이트 목록 탭
	private JPanel createSiteList() {
		JPanel p = new JPanel(new BorderLayout());

		p.add(SearchSort(), BorderLayout.NORTH);
		p.add(new JScrollPane(recordTable()));
		p.add(infoCheck(), BorderLayout.SOUTH);
		return p;
	}

	// 사이트 목록 탭-검색/정렬
	private JPanel SearchSort() {
		JPanel p = new JPanel();
		createBorder(p, "검색/정렬");

		JPanel t1 = new JPanel(); t1.add(createSearch());
		JPanel t2 = new JPanel(); t2.add(createSort());
		p.add(t1); p.add(t2);
		return p;
	}

	// 사이트 목록 탭-테이블 출력
	private JTable recordTable() {
		mT = new JTable();
		//model = new InfoTableModel(sil);
		mT.setModel(model); 
		mT.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 단일선택

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
		
		JLabel info_id = new JLabel("아이디");
		info_idL = new JTextField(10);

		JLabel info_pw = new JLabel("비밀번호");
		info_pwL = new JTextField(10);

		info_idL.setEditable(false); info_pwL.setEditable(false);

		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		info_del = new JButton("삭제");
		info_del.setEnabled(false);

		info_del.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//int index = mT.getSelectedRow();

				if(modelRow < 0){
					System.out.println("삭제할 행을 선택해 주세요.");
				}else{
					model.removeRow(sil, modelRow);
					sil.showAllSiteInfo();

					// 삭제 후 좌측 패널 빈칸
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
					card.show(cardP, "입력");
					SiteSize.setText(sil.size()+"개의 사이트가 등록되어있습니다.");

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
						System.out.println(viewInfo+" 선택");
						//info_idL.setEditable(true); info_pwL.setEditable(true);
						info_idL.setText(tmpId); 
						info_pwL.setText(tmpPw);
						info_del.setEnabled(true);
						System.out.println(tmpId+"\t"+tmpPw);
					}
					else {
						System.out.println(viewInfo+" 해제");
						//info_idL.setEditable(true); info_pwL.setEditable(true);
						info_idL.setText(""); 
						info_pwL.setText("");
						info_del.setEnabled(false);
					}
				}
				else {
					System.out.println("테이블을 클릭해주세요!");
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
		createBorder(searchP, "검색");

		searchCB = new JComboBox<String>(searchD);
		searchCB.setSelectedIndex(0);
		searchCB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				newFilter();
			}
		});
		searchP.add(searchCB);

		JLabel filter = new JLabel("필터:");
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
		createBorder(sortP, "정렬");

		sortCateCB = new JComboBox<String>(sortCateD);
		sortSiteCB = new JComboBox<String>(sortSiteD);

		sortCateCB.setSelectedIndex(0);
		sortSiteCB.setSelectedIndex(0);
		sortP.add(sortCateCB); sortP.add(sortSiteCB);

		JButton sortBtn = new JButton("정렬");
		sortBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str1 = (String)sortCateCB.getSelectedItem();
				String str2 = (String)sortSiteCB.getSelectedItem();
				RowFilter<InfoTableModel, Object> firstFilter;
				RowFilter<InfoTableModel, Object> secondFilter;

				if(str1.equals("분류"))
					firstFilter = RowFilter.regexFilter("", 0);
				else firstFilter = RowFilter.regexFilter(str1, 0);
				if(str2.equals("사이트 이름"))
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

		JButton basicBtn = new JButton("기본");
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

	// 테이블 선택 시 입력/수정 변경 이벤트 처리
	private void registerTableSelectionEventListener() {

		mT.getSelectionModel().addListSelectionListener( 
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent e) {
						InfoTableModel itm = (InfoTableModel)mT.getModel();
						viewRow = mT.getSelectedRow(); // 선택한 row값을 뷰 상에
						System.out.println(viewRow);


						if (viewRow < 0) {
							System.out.println("Selection got filtered away");
						}
						else {
							modelRow = mT.convertRowIndexToModel(viewRow); // 모델의 로우값으로 전환
							System.out.printf("Selected Row in view: %d. ", viewRow);
							System.out.printf("Selected Row in model: %d.\n", modelRow);

							urlF.setEditable(false); // 주소 수정불가
							SiteInfo selectInfo = itm.getSelectedCell(modelRow);
							siteF.setText(selectInfo.site);
							urlF.setText(selectInfo.url);
							idF.setText(selectInfo.id); 

							// 계정정보보기 클릭 시 사용할 변수에 임시 저장
							tmpId = selectInfo.id;
							tmpPw = selectInfo.pwd;

							cateCB.setSelectedItem(selectInfo.sitecate);
							preferCB.setSelectedItem(selectInfo.prefer);
							memoT.setText(selectInfo.memo);

							editbtn.setEnabled(true);
							info_del.setEnabled(true);
							card.show(cardP, "수정");

							System.out.println(infoC.isSelected()+tmpId);
							// 계정정보 보기 부분
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
			if(!selectC.equals("전체")) 
				firstFilter = RowFilter.regexFilter(selectC, 0);
			else  // 전체일 경우,
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

	// 등록현황 탭
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
			int sum=0; // 초기값 0
			HashMap<String,Integer> hm = new HashMap<String,Integer>();
			Vector<String> v = new Vector<String>();
			int name_size=0;

			for (int i = 0; i < size; i++) {
				S_info=sil.getSiteInfo(i);
				int s_prefer = S_info.getPrefer();
				if(hm.containsKey(S_info.getSite())) {//이름 중복이라면
					int prefer = hm.get(S_info.getSite());
					prefer+=s_prefer;
					hm.put(S_info.getSite(), prefer);  
				}
				else {//이름 중복 아니라면 
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

	private void sureClosing() {//종료 처리
		int n = JOptionPane.showConfirmDialog(frame, "정말 종료하시겠습니까?","종료",JOptionPane.OK_CANCEL_OPTION);//확인,취소
		if(n==JOptionPane.OK_OPTION)
			System.exit(0);
	}

	private void checkSite() { // 데이터가 삭제되면 콤보박스에서 삭제
		Vector<String> tmpsite = new Vector<String>(); // table에 등록된 사이트 이름 저장 벡터
		for(int i=0; i<sil.size(); i++) {
			SiteInfo tmps = sil.getSiteInfo(i); // 사이트 정보 얻어오기
			tmpsite.add(tmps.site);
		}

		for(int i=1; i<sortSiteD.size(); i++) {
			if(!tmpsite.contains(sortSiteD.get(i))) // '사이트 이름 콤보박스' 중 테이블에 저장된 사이트 이름에 속하지 않으면
				sortSiteD.remove(i); // 삭제
		}
	}

	private boolean checkSiteName(String t) { // 데이터에 동일한 이름의 사이트가 존재할 경우 경고창
		Vector<String> tmpsite = new Vector<String>(); // table에 등록된 사이트 이름 저장 벡터
		for(int i=0; i<sil.size(); i++) {
			SiteInfo tmps = sil.getSiteInfo(i); // 사이트 정보 얻어오기
			tmpsite.add(tmps.site);
		}

		if(tmpsite.contains(t)) { // 이미 동일 이름 존재할 경우,
			return true;
		}
		return false;
	}
}
