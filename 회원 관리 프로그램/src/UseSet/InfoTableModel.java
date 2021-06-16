package UseSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;


public class InfoTableModel extends AbstractTableModel{
	private Vector<SiteInfo> data = new Vector<SiteInfo>();
	private Vector<String> colNames;

	public InfoTableModel() {
		String[] s = {"�з�","��ȣ��","����Ʈ �̸�", "����Ʈ �ּ�"};
		colNames = new Vector<String>(s.length);

		for(int c=0; c<s.length; c++) {
			colNames.addElement(s[c]);
		}
	}
	public InfoTableModel(SiteInfoList sil){
		String[] s = {"�з�","��ȣ��","����Ʈ �̸�", "����Ʈ �ּ�"};
		colNames = new Vector<String>(s.length);

		for(int c=0; c<s.length; c++) {
			colNames.addElement(s[c]);
		}

		for(int i=0; i<sil.size(); i++)
			data.add(sil.getSiteInfo(i));
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return colNames.size();
	}

	@Override
	public String getColumnName(int c) {
		return colNames.get(c);
	}


	@Override
	public Object getValueAt(int r, int c) {
		SiteInfo sc = data.get(r);

		switch(c) {
		case 0:	return sc.sitecate; // ����Ʈ �з�
		case 1: return sc.prefer; // ����Ʈ ��ȣ��
		case 2: return sc.site; // ����Ʈ �̸�
		case 3: return sc.url; // ����Ʈ �ּ�
		}

		return null;
	}

	public SiteInfo getSelectedCell(int r) {
		SiteInfo sc = data.get(r);
		return sc;
	}

	public void removeRow(SiteInfoList sil, int r) {
		SiteInfo sc = data.get(r);

		data.remove(r);
		sil.removeSiteInto(sc.url);

		fireTableChanged(new TableModelEvent(this));
		System.out.println("������ ���� ��>>>> "+data);
	}

	public void addRow(SiteInfoList sil, SiteInfo rowData){
		sil.showAllSiteInfo();
		
		if(SiteInfoManager.isExcel==true) {
			//data�� rowData�߰�
			data.add(rowData);
		}
		else {
			if(sil.checkSiteInfo(rowData)) {
				SiteInfo tmp;
				int idx=-999; 
				System.out.println("data.size: "+data.size());
				for(int i=0; i<data.size(); i++) {
					tmp = data.get(i);
					if(tmp.url.equals(rowData.url)) {
						idx = i;
						break;
					}
				}
				data.setElementAt(rowData, idx);
			}
			else{ // ���� url ���� X
				data.add(rowData);
			}
		}
		
		sil.addSiteInfo(rowData);
		data.clear();
		for(int i=0; i<sil.size(); i++)
			data.add(sil.getSiteInfo(i));
		fireTableChanged(new TableModelEvent(this));
		System.out.println("������ �߰�>>>> "+data);
	}

}
