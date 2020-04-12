package views;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Driver 
{

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
		
		class NewButtonListener implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//Put something here
			}
		}
		
		class SpinnerListener implements ChangeListener
		{
			public void stateChanged(ChangeEvent e)
			{
				if (cp.getRMSpinner() > gv.numRMViews())
				{
					gv.addRMView(new RMView());
				}
				else
				{
					gv.removeRMView();
				}
				
			}
		}

		cp.addNewButtonListener(new NewButtonListener());
		cp.addSpinnerChangeListener(new SpinnerListener());
	}
}