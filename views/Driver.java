package views;

import java.awt.EventQueue;

public class Driver {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		CtrlPanelView cp = new CtrlPanelView();
		GraphicalView gv = new GraphicalView();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow(cp, gv);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
	}
}