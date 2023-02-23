package gui;

import java.lang.reflect.InvocationTargetException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import gui.ui_design.MainPanelDesign;

abstract class GUIContainers{
	//Parent JFrame
	protected JFrame jf;
	//main panel
	protected JPanel pnl_main;
	//scrollpanel of main panel
	protected JScrollPane pane_main;
	//sub panels
	protected JPanel[] subPnls;
	//menu bar
	protected JMenuBar mainFrameMenuBar;
	//menu
	protected JMenu settingsMenu, toolsMenu, editMenu;
	//settings menu items
	protected JMenuItem[] settingsMenuItems;
	//edit menu
	protected JMenuItem[] editMenuItems;
	//tools menu items
	protected JMenuItem[] toolsMenuItems;
	//minimum size and minimum preferred size of pnl_main
	protected Dimension pnlMainMinSize;
	//extra space like jframe decorations, flowlayout gaps and scrollpane insets
	private Dimension extJfSpc;
	//minimum preferred size of pnl_main which is equal to the size of a single sub panel
	//designated in this program
	private int mPnlMinPrefW, mPnlMinPrefH;
	//
	private FlowLayout fl;
	//
	private int vGap, hGap;
	
	GUIContainers(){
		jf = new JFrame("Fmanager by Lazatin");
		pnl_main = new JPanel(new FlowLayout());
		pane_main = new JScrollPane(pnl_main);
		//main panel and it's subpanels minimum preferred size
		mPnlMinPrefW = 400;
		mPnlMinPrefH = 500;
		
		mainFrameMenuBar = new JMenuBar();
		settingsMenu = new JMenu("Settings");
		toolsMenu = new JMenu("Tools");
		editMenu = new JMenu("Edit");
		
		settingsMenuItems = new JMenuItem[2];
		settingsMenuItems[0] = new JMenuItem("Preferences...");
		settingsMenuItems[1] = new JMenuItem("Exit...");
		
		editMenuItems = new JMenuItem[1];
		editMenuItems[0] = new JMenuItem("Post File Editor");
		
		toolsMenuItems = new JMenuItem[2];
		toolsMenuItems[0] = new JMenuItem("File Randomizer");
		toolsMenuItems[1] = new JMenuItem("Link Checker");
		
		for(JMenuItem item : settingsMenuItems)
			settingsMenu.add(item);
		mainFrameMenuBar.add(settingsMenu);
		
		for(JMenuItem item : editMenuItems)
			editMenu.add(item);
		mainFrameMenuBar.add(editMenu);
		
		for(JMenuItem item : toolsMenuItems)
			toolsMenu.add(item);
		mainFrameMenuBar.add(toolsMenu);
		
		BufferedImage subPnlBg = null;
		try{
			subPnlBg = ImageIO.read(new File("src/images/SubPanel-Background1.png"));
		}
		catch(IOException e) {
			e.printStackTrace();
			
		}
		BufferedImage imageExist = subPnlBg;
		
		subPnls = new JPanel[6];
		//sub panel1
		subPnls[0] = new JPanel(new GridBagLayout()){
			@Override
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				if(imageExist != null){
					g.drawImage(imageExist, 0, 0, null);
				}
				else{
					g.setColor(Color.WHITE);
					g.fillRect(0, 0, mPnlMinPrefW, mPnlMinPrefH);
				}
			}
		};
		
		subPnls[0].setBorder(BorderFactory
						     .createTitledBorder(BorderFactory.createLineBorder(new Color(0,0,0,1)),
											  "<html><span style='text-decoration:underline;white-space:nowrap;'>"+
											  "File Generation"+
											  "</span></html>"));
		subPnls[0].setPreferredSize(new Dimension(mPnlMinPrefW,mPnlMinPrefH));
		pnl_main.add(subPnls[0]);
		//sub panel2
		subPnls[1] = new JPanel(new GridBagLayout()){
			@Override
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				if(imageExist != null){
					g.drawImage(imageExist, 0, 0, null);
				}
				else{
					g.setColor(Color.WHITE);
				}
			}
		};
		subPnls[1].setBorder(BorderFactory
						     .createTitledBorder(BorderFactory.createLineBorder(new Color(0,0,0,1)),
											  "<html><span style='text-decoration:underline;white-space:nowrap;'>"+
											  "Logs"+
											  "</span></html>"));
		subPnls[1].setPreferredSize(new Dimension(mPnlMinPrefW,mPnlMinPrefH));
		pnl_main.add(subPnls[1]);
		//sub panel3
		subPnls[2] = new JPanel(new GridBagLayout()){
			@Override
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				if(imageExist != null){
					g.drawImage(imageExist, 0, 0, null);
				}
				else{
					g.setColor(Color.WHITE);
					g.drawRoundRect(0, 0, mPnlMinPrefW, mPnlMinPrefH, 20, 20);
				}
			}
		};
		subPnls[2].setBorder(BorderFactory
						     .createTitledBorder(BorderFactory.createLineBorder(new Color(0,0,0,1)),
											  "<html><span style='text-decoration:underline;white-space:nowrap;'>"+
											  "Link Operations"+
											  "</span></html>"));
		subPnls[2].setPreferredSize(new Dimension(mPnlMinPrefW,mPnlMinPrefH));
		pnl_main.add(subPnls[2]);
		//sub panel4
		subPnls[3] = new JPanel(new GridBagLayout()){
			@Override
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				if(imageExist != null){
					g.drawImage(imageExist, 0, 0, null);
				}
				else{
					g.setColor(Color.WHITE);
					g.drawRoundRect(0, 0, mPnlMinPrefW, mPnlMinPrefH, 20, 20);
				}
			}
		};
		subPnls[3].setBorder(BorderFactory
						     .createTitledBorder(BorderFactory.createLineBorder(new Color(0,0,0,1)),
											  "<html><span style='text-decoration:underline;white-space:nowrap;'>"+
											  "Compress & Decompress"+
											  "</span></html>"));
		subPnls[3].setPreferredSize(new Dimension(mPnlMinPrefW,mPnlMinPrefH));
		pnl_main.add(subPnls[3]);
		//sub panel5
		subPnls[4] = new JPanel(new GridBagLayout()){
			@Override
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				if(imageExist != null){
					g.drawImage(imageExist, 0, 0, null);
				}
				else{
					g.setColor(Color.WHITE);
					g.drawRoundRect(0, 0, mPnlMinPrefW, mPnlMinPrefH, 20, 20);
				}
			}
		};
		subPnls[4].setBorder(BorderFactory
						     .createTitledBorder(BorderFactory.createLineBorder(new Color(0,0,0,1)),
											  "<html><span style='text-decoration:underline;white-space:nowrap;'>"+
											  "Stopwatch"+
											  "</span></html>"));
		subPnls[4].setPreferredSize(new Dimension(mPnlMinPrefW,mPnlMinPrefH));
		pnl_main.add(subPnls[4]);
		//sub panel6
		subPnls[5] = new JPanel(new GridBagLayout()){
			@Override
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				if(imageExist != null){
					g.drawImage(imageExist, 0, 0, null);
				}
				else{
					g.setColor(Color.WHITE);
					g.drawRoundRect(0, 0, mPnlMinPrefW, mPnlMinPrefH, 20, 20);
				}
			}
		};;
		subPnls[5].setBorder(BorderFactory
						     .createTitledBorder(BorderFactory.createLineBorder(new Color(0,0,0,1)),
											  "<html><span style='text-decoration:underline;white-space:nowrap;'>"+
											  "File Viewer"+
											  "</span></html>"));
		subPnls[5].setPreferredSize(new Dimension(mPnlMinPrefW,mPnlMinPrefH));
		pnl_main.add(subPnls[5]);
		
	}
	
	protected void calibrateTopContainers(){
		//set JFrame's GUI appearance
		new MainPanelDesign(pnl_main, subPnls);
		jf.add(pane_main);
		jf.setJMenuBar(mainFrameMenuBar); 
		jf.setResizable(true);
		jf.pack();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
		//set pane_main scrollbars size
		//this code snippet has weird side effects. One of them is a problem
		//when a user click the maximize,minimize button on top of jframe, the
		//lengths of the scrollbars sliders abruptly increase. Leading to
		//awkward scrolling. There's a remedy however, just drag the sliders a
		//bit and their original size will return to its normal size for some time.
		//Another much preferrable fix: repaint() and revalidate the panel where
		//the scrollpane resides everytime a user minimize or maximize the frame.
		pane_main.getVerticalScrollBar().setPreferredSize(new Dimension(10,0));
		pane_main.getHorizontalScrollBar().setPreferredSize(new Dimension(0,10));
		//
		pane_main.getVerticalScrollBar().setUnitIncrement(17);
		//
		//get pnl_main's layout which is a flowlayout. Then, set the hgap and vgap
		//and don't forget to add them when you calculate the total preferred size
		//of a panel with flow layout. In my case, I just need the left and right
		//hgaps and it's alright to ignore the hgaps in between sub panels. So,
		//I just need to multiply the hgap by 2. I think these gaps don't change
		//even a sub component has an ample amount of space due to resizing of the
		//main panel.
		fl = (FlowLayout) pnl_main.getLayout();
		vGap = 10;
		hGap = 10;
		fl.setHgap(hGap);
		fl.setVgap(vGap);
		//minimum width and default preferred size of pnl_main and use them
		//as the base of our program's minimum unscrollable size.
		//This is just my preference. This can be changed.
		pnlMainMinSize = new Dimension(mPnlMinPrefW,mPnlMinPrefH);
		//jframe's minimum size should be more larger than pnl_main's preferred size because
		//of jframe's decorations. We need to do this to maintain a perfect distance between
		//pnl_main preferred size and JFrame's size where the scrollbars won't unnecessarily appear.
		//Get JFrame's decoration sizes by getting its insets and get the insets of JScrollPane.
		Insets jfIns = jf.getInsets();
		Insets pnmain_ins = pane_main.getInsets();
		//There are these default gaps between main panel and the leftmost and rightmost subpanels
		//when your main panel has flow layout and its subpanels have different layout.
		//I think these gaps can't be removed nor get their values. However, there's a trick
		//to get the values of these gaps, just get the distance between the main panel and
		//and sub panel by finding the difference between their top left points. Java creates
		//a rectangular bounds every time we create JPanels(or other components) and the
		//rectangular bounds will start to "spawn" from top left to bottom right. Then the
		//component will be placed in that rectangular bounds.
		int xGap = subPnls[0].getLocation().x - pane_main.getLocation().x;
		int yGap = subPnls[0].getLocation().y - pane_main.getLocation().y;
		//extra space like jframe decorations, flowlayout gaps and scrollpane insets
		extJfSpc = new Dimension(jfIns.left+jfIns.right+pnmain_ins.left+
											pnmain_ins.right+xGap*2,
									 jfIns.top+jfIns.bottom+pnmain_ins.top+
											pnmain_ins.bottom+yGap*2
									);
		//set JFrame's minimum size. Add all necessary insets and multiply xGap and yGap by
		//2 because these default gaps that I explained earlier affect both sides of the
		//panel that are opposite to each other. No need to recalculate the other side, just
		//multiply the first side by 2 and you will get the gap size of the opposite side.
		Dimension jfMinSize = new Dimension(mPnlMinPrefW+(int)extJfSpc.getSize().getWidth(),
										    mPnlMinPrefH+(int)extJfSpc.getSize().getHeight() + fl.getVgap()*3);
		//set jframe's initial size
		//I prefer two columns(panels) to be shown initially in horizontal space
		//and one row.
		//We're changing the size of jframe here so include all necessary insets
		//If the main panel has h or v gaps, add it here and the main panel will adjust
		//its size proportionally to jframe's size to prevent unwanted sub panels alignment.
		//I'm not sure about this but I think main panel adjust its size if jframe contentpane
		//doesn't perfectly wrap the main panel. Increasing jframe size also increases its
		//contenpane size, So, the idea kinda make sense to me 
		Dimension jfInitSize = new Dimension(mPnlMinPrefW*2 + (int)extJfSpc.getSize().getWidth()+fl.getHgap()*2,
										     mPnlMinPrefH + (int)extJfSpc.getSize().getHeight());
		jf.setSize(jfInitSize);
		//setLocationRelativeTo() method should be called after setting jframe's final size
		//to center jframe at the center of the screen
		jf.setLocationRelativeTo(null);
		//set JFrame's minimum size
		jf.setMinimumSize(jfMinSize);
		//calibrate the size of jframe and set the necessary preffered size of pnl_main.
		calibrateMainPnlPrefSize();
	}
	
	private void calibrateMainPnlPrefSize(){
		
		Thread thread = new Thread(new Runnable(){
			
			@Override
			public void run(){
				while(true){
					
				    try{ 
					  EventQueue.invokeAndWait(new pnlMainPrefSizeCalibrator());
					  Thread.sleep(10);
					}
				    catch(InterruptedException | InvocationTargetException e){ e.printStackTrace(); }
				}
			}
			
		});
		
		thread.start();
		
	}
	
	//monitor the size of jframe and set the necessary preffered size of pnl_main.
	//There are two subpanels in this gui and and I want them to realign vertically
	//and this method does the job.
	private class pnlMainPrefSizeCalibrator implements Runnable {
		
		@Override
		public void run(){
			//
			double extSpc = extJfSpc.getSize().getWidth();
			//get jframe's width
			int jfWidth = (int)jf.getSize().getWidth();
			//
			//debugger
			//double width = jf.getSize().getWidth();
			//double width2 = pnl_main.getSize().getWidth();
			//System.out.print("jf width: "+width);
			//System.out.print(" pnl_main width: "+width2);
			//System.out.print("\r");
			//if the horizontal length is not enough to display the two subpanels horizontally
			//then the rightmost panel will be repositioned at the bottom of prior panel on the left,
			//since pnl_main has flowlayout layout and the layout orientation is left-to-right.
			//
			//The problem here is that the pnl_main preferred size won't change when the flowlayout
			//adjusts the subpanel. For the scrollbars to appear, pnl_main where the scrollpane is attached
			//needs to have an actual size that is less than its preferred size. If that's the case then
			//subpanels can be fully scrolled whether they're positioned on top of each other.
			//
			//set computed preferred width to 0. We do this because jframe's width is not 
			//equal to its minimum width. We reduce the sum of sub panels' width everytime
			//jframe overlaps a sub panel preferred width, until it reaches its minimum width
			int prefW = 0;
			//
			int prefH = mPnlMinPrefH;
			int counter = 0;
			
			//This variable is related to the y-gap of our main panel.
			//Our main panel needs to have extra space on its bottom part
			//if our main panel(flowlayout) sets it y-gap. To get this
			//thing right, we need to compute every y-gap in each row.
			//I start at 10 because I set 5 y-gap to my main panel.
			//
			//The starting gap is 10 because I initially computed the
			//y-gaps at top and bottom of our main panel. For computing
			//the y-gaps in-between rows, increment variable will increase
			//by one each time our main panel move down one row.
			int bottomYgap = vGap * 2;
			int increment = 0;
			
			//set the first sub panel's y-coords location
			//as initial value of this variable
			int ycoords = subPnls[0].getLocation().y;
			for(int i = subPnls.length; i > 0 ; i--){
				int totW = mPnlMinPrefW*i;
				
				//This code determines if flowlayout has moved down a panel 
				//in one row. If true, adjust the preferred height of main panel.
				if(ycoords < subPnls[counter].getLocation().y){
					increment++;
					prefH += mPnlMinPrefH;
					ycoords = subPnls[counter].getLocation().y;
				}
				
				//Compare the jframe width with the total preferred width
				//of sub panels. Don't forget to add the extra space
				//of jframe to the total width of subpanels to 
				//prevent jframe from overlapping in sub panels
				//that causes scrollbar to unnecessarily appear.
				if(jfWidth < (totW + extSpc))
					prefW = totW-mPnlMinPrefW;
				counter++;
			}
			
			//Total y-gaps in our main panel. The vGap variable is the gap that
			//I set to my main panel via setVgap() method.
			int spaceForYgap = bottomYgap + (vGap * increment);
			
			Dimension initialSize = new Dimension(prefW,prefH + spaceForYgap);
			pnl_main.setPreferredSize(initialSize);
			
			//repaint and revalidate components when they're being
			//changed during runtime to prevent nasty GUI glitches like scrollbar still present even the 
			//actual pane_main's size is grater then its preferred size.
			pnl_main.repaint();
			pnl_main.revalidate();
		}
		
	}
	
	protected JFrame getMainFrame(){ return jf; }
	
	//
	JMenuItem getSettingsMenuItem(int index){
		return settingsMenuItems[index];
	}
	
	JMenuItem getEditMenuItem(int index){
		return editMenuItems[index];
	}
	
	JMenuItem getToolsMenuItem(int index){
		return toolsMenuItems[index];
	}
}