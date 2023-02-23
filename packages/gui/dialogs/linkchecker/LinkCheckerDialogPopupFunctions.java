package gui.dialogs.linkchecker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPopupMenu;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LinkCheckerDialogPopupFunctions{
	protected static JDialog staticMainFrame;
	
	protected JPopupMenu rClickPopup, rClickTreePopup;
	protected JMenuItem remove, removeAll;
	
	private JDialog mainFrame;
	
	protected LinkCheckerDialogPopupFunctions(){
		rClickPopup = new JPopupMenu();
		remove = new JMenuItem("Remove");
		removeAll = new JMenuItem("Remove All");
		rClickTreePopup = new JPopupMenu();
	}
	
	protected void enableListPopup(JList<Object> list){
		list.addMouseListener(new MouseAdapter(){
		
			@Override
			public void mousePressed(MouseEvent e){
				if(SwingUtilities.isRightMouseButton(e))
					if(e.isPopupTrigger()){
						rClickPopup.show(e.getComponent(),e.getX(),e.getY());
					}
			}
			
			@Override
			public void mouseReleased(MouseEvent e){
				if(SwingUtilities.isRightMouseButton(e))
					if(e.isPopupTrigger()){
						rClickPopup.show(e.getComponent(),e.getX(),e.getY());
					}
			}
		
		});
	}
	
	protected void popupActionSource(ActionEvent e, JList<Object> selectedDirList,
									 JList<Object> searchForList, LinkedHashSet<Object> dirArrList,
									 ArrayList<Object> hostArrList){
		
		if(e.getSource() == remove){
			if(rClickPopup.getInvoker().getName().equals("Directory Selection"))
				removeItems(selectedDirList, dirArrList);
			else if(rClickPopup.getInvoker().getName().equals("Search List"))
				removeItems(searchForList, hostArrList);
		}
		else if(e.getSource() == removeAll){
			String compName = rClickPopup.getInvoker().getName();
			if(rClickPopup.getInvoker().getName().equals("Directory Selection"))
				removeAllItems(selectedDirList, dirArrList);
			else if(rClickPopup.getInvoker().getName().equals("Search List"))
				removeAllItems(searchForList, hostArrList);
		}
	}
	
	protected void removeItems(JList<Object> jlist, Collection<Object> contentList){
		if(jlist.getSelectedIndex() == -1){
			JOptionPane.showMessageDialog(mainFrame,"No selected item/s!",
				"Empty Selection",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		List<Object> list = jlist.getSelectedValuesList();
		contentList.removeAll(list);
		jlist.setListData(contentList.toArray());
	}
	
	protected void removeAllItems(JList<Object> jlist, Collection<Object> contentList){
		if(jlist.getSelectedIndex() == -1){
			JOptionPane.showMessageDialog(mainFrame,"No selected item/s!",
				"Empty Selection",JOptionPane.ERROR_MESSAGE);
			return;
		}
		contentList.clear();
		jlist.setListData(contentList.toArray());
	}
}