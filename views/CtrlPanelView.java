package views;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.ChangeListener;

public class CtrlPanelView extends JPanel
{
	private JButton btnNewButton;
	private JSpinner rmSpinner;
	private JTextPane workingCount;
	private JTextPane preparedCount;
	private JTextPane committedCount;
	private JTextPane abortedCount;
	private JTextPane abortProb;
	private JTextArea logArea;

	public CtrlPanelView()
	{
		GridBagLayout gbl_ctrlPanel = new GridBagLayout();
		gbl_ctrlPanel.columnWidths = new int[]{102, 38, 0};
		gbl_ctrlPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 18, 0, 0, 0};
		gbl_ctrlPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_ctrlPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gbl_ctrlPanel);
		
		btnNewButton = new JButton("New Simulation");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridwidth = 2;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 0;
		this.add(btnNewButton, gbc_btnNewButton);
		
		JTextPane txtpnRms = new JTextPane();
		txtpnRms.setText("RMs:");
		txtpnRms.setBackground(SystemColor.window);
		GridBagConstraints gbc_txtpnRms = new GridBagConstraints();
		gbc_txtpnRms.insets = new Insets(0, 0, 5, 5);
		gbc_txtpnRms.gridx = 0;
		gbc_txtpnRms.gridy = 1;
		this.add(txtpnRms, gbc_txtpnRms);
		
		rmSpinner = new JSpinner();
		GridBagConstraints gbc_rmSpinner = new GridBagConstraints();
		gbc_rmSpinner.insets = new Insets(0, 0, 5, 0);
		gbc_rmSpinner.gridx = 1;
		gbc_rmSpinner.gridy = 1;
		this.add(rmSpinner, gbc_rmSpinner);
		
		JTextPane txtpnWorking = new JTextPane();
		txtpnWorking.setText("Working:");
		txtpnWorking.setBackground(SystemColor.window);
		GridBagConstraints gbc_txtpnWorking = new GridBagConstraints();
		gbc_txtpnWorking.insets = new Insets(0, 0, 5, 5);
		gbc_txtpnWorking.gridx = 0;
		gbc_txtpnWorking.gridy = 2;
		this.add(txtpnWorking, gbc_txtpnWorking);
		
		workingCount = new JTextPane();
		workingCount.setText("0");
		workingCount.setEditable(false);
		workingCount.setBackground(SystemColor.window);
		GridBagConstraints gbc_workingCount = new GridBagConstraints();
		gbc_workingCount.insets = new Insets(0, 0, 5, 0);
		gbc_workingCount.fill = GridBagConstraints.VERTICAL;
		gbc_workingCount.gridx = 1;
		gbc_workingCount.gridy = 2;
		this.add(workingCount, gbc_workingCount);
		
		JTextPane txtpnPrepared = new JTextPane();
		txtpnPrepared.setText("Prepared:");
		txtpnPrepared.setBackground(SystemColor.window);
		GridBagConstraints gbc_txtpnPrepared = new GridBagConstraints();
		gbc_txtpnPrepared.insets = new Insets(0, 0, 5, 5);
		gbc_txtpnPrepared.gridx = 0;
		gbc_txtpnPrepared.gridy = 3;
		this.add(txtpnPrepared, gbc_txtpnPrepared);
		
		preparedCount = new JTextPane();
		preparedCount.setEditable(false);
		preparedCount.setText("0");
		preparedCount.setBackground(SystemColor.window);
		GridBagConstraints gbc_preparedCount = new GridBagConstraints();
		gbc_preparedCount.fill = GridBagConstraints.VERTICAL;
		gbc_preparedCount.insets = new Insets(0, 0, 5, 0);
		gbc_preparedCount.gridx = 1;
		gbc_preparedCount.gridy = 3;
		this.add(preparedCount, gbc_preparedCount);
		
		JTextPane txtpnCommitted = new JTextPane();
		txtpnCommitted.setText("Committed:");
		txtpnCommitted.setBackground(SystemColor.window);
		GridBagConstraints gbc_txtpnCommitted = new GridBagConstraints();
		gbc_txtpnCommitted.insets = new Insets(0, 0, 5, 5);
		gbc_txtpnCommitted.gridx = 0;
		gbc_txtpnCommitted.gridy = 4;
		this.add(txtpnCommitted, gbc_txtpnCommitted);
		
		committedCount = new JTextPane();
		committedCount.setEditable(false);
		committedCount.setText("0");
		committedCount.setBackground(SystemColor.window);
		GridBagConstraints gbc_committedCount = new GridBagConstraints();
		gbc_committedCount.fill = GridBagConstraints.VERTICAL;
		gbc_committedCount.insets = new Insets(0, 0, 5, 0);
		gbc_committedCount.gridx = 1;
		gbc_committedCount.gridy = 4;
		this.add(committedCount, gbc_committedCount);
		
		JTextPane txtpnAborted = new JTextPane();
		txtpnAborted.setText("Aborted:");
		txtpnAborted.setBackground(SystemColor.window);
		GridBagConstraints gbc_txtpnAborted = new GridBagConstraints();
		gbc_txtpnAborted.fill = GridBagConstraints.VERTICAL;
		gbc_txtpnAborted.insets = new Insets(0, 0, 5, 5);
		gbc_txtpnAborted.gridx = 0;
		gbc_txtpnAborted.gridy = 5;
		this.add(txtpnAborted, gbc_txtpnAborted);
		
		abortedCount = new JTextPane();
		abortedCount.setEditable(false);
		abortedCount.setText("0");
		abortedCount.setBackground(SystemColor.window);
		GridBagConstraints gbc_abortedCount = new GridBagConstraints();
		gbc_abortedCount.insets = new Insets(0, 0, 5, 0);
		gbc_abortedCount.fill = GridBagConstraints.VERTICAL;
		gbc_abortedCount.gridx = 1;
		gbc_abortedCount.gridy = 5;
		this.add(abortedCount, gbc_abortedCount);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
		gbc_horizontalStrut.gridwidth = 2;
		gbc_horizontalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut.gridx = 0;
		gbc_horizontalStrut.gridy = 6;
		this.add(horizontalStrut, gbc_horizontalStrut);
		
		JTextPane txtpnAbortProb = new JTextPane();
		txtpnAbortProb.setToolTipText("The probability that each node, when asked to commit, will instead abort the transaction.");
		txtpnAbortProb.setText("p(Abort):");
		txtpnAbortProb.setBackground(SystemColor.window);
		GridBagConstraints gbc_txtpnAbortProb = new GridBagConstraints();
		gbc_txtpnAbortProb.insets = new Insets(0, 0, 5, 5);
		gbc_txtpnAbortProb.fill = GridBagConstraints.VERTICAL;
		gbc_txtpnAbortProb.gridx = 0;
		gbc_txtpnAbortProb.gridy = 7;
		this.add(txtpnAbortProb, gbc_txtpnAbortProb);
		
		abortProb = new JTextPane();
		abortProb.setText("0");
		abortProb.setBackground(SystemColor.window);
		GridBagConstraints gbc_AbortProb = new GridBagConstraints();
		gbc_AbortProb.insets = new Insets(0, 0, 5, 0);
		gbc_AbortProb.gridx = 1;
		gbc_AbortProb.gridy = 7;
		this.add(abortProb, gbc_AbortProb);
		
		logArea = new JTextArea();
		GridBagConstraints gbc_logArea = new GridBagConstraints();
		gbc_logArea.gridwidth = 2;
		gbc_logArea.fill = GridBagConstraints.BOTH;
		gbc_logArea.gridx = 0;
		gbc_logArea.gridy = 8;
		this.add(logArea, gbc_logArea);
	}

	public void setWorkingCount(int wcount)
	{
		workingCount.setText(Integer.toString(wcount));
	}
	
	public void setPreparedCount(int pcount)
	{
		preparedCount.setText(Integer.toString(pcount));
	}
	
	public void setCommittedCount(int ccount)
	{
		preparedCount.setText(Integer.toString(ccount));
	}
	
	public void setAbortedCount(int acount)
	{
		preparedCount.setText(Integer.toString(acount));
	}
	
	public int getRMSpinner()
	{
		return (int) rmSpinner.getValue();
	}
	
	public void addNewButtonListener(ActionListener al)
	{
		btnNewButton.addActionListener(al);
	}
	
	public void addSpinnerChangeListener(ChangeListener cl)
	{
		rmSpinner.addChangeListener(cl);
	}
}
