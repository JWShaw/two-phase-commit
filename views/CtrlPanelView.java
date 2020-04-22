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
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SpinnerListModel;
import javax.swing.event.ChangeListener;

/**
 * One of the two views: the control panel on the left of the user-interface.
 */
@SuppressWarnings("serial")
public class CtrlPanelView extends JPanel {
  private JButton btnNewButton;
  private JButton btnAddRm;
  private JButton btnPrepare;
  private JTextPane workingCountPane;
  private JTextPane preparedCountPane;
  private JTextPane committedCountPane;
  private JTextPane abortedCountPane;
  private JTextArea logArea;

  private int workingCount;
  private int preparedCount;
  private int committedCount;
  private int abortedCount;
  private JSpinner abortProbSpinner;

  /**
   * Builds the view, which is displayed in the main window
   */
  public CtrlPanelView() {
    workingCount = 0;
    preparedCount = 0;
    committedCount = 0;
    abortedCount = 0;

    GridBagLayout gbl_ctrlPanel = new GridBagLayout();
    gbl_ctrlPanel.columnWidths = new int[] { 102, 38, 0 };
    gbl_ctrlPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 18, 0, 0, 0 };
    gbl_ctrlPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
    gbl_ctrlPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
    this.setLayout(gbl_ctrlPanel);

    btnNewButton = new JButton("New Simulation");
    GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
    gbc_btnNewButton.gridwidth = 2;
    gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
    gbc_btnNewButton.gridx = 0;
    gbc_btnNewButton.gridy = 0;
    this.add(btnNewButton, gbc_btnNewButton);

    btnAddRm = new JButton("Add RM");
    GridBagConstraints gbc_btnAddRM = new GridBagConstraints();
    gbc_btnAddRM.gridwidth = 2;
    gbc_btnAddRM.insets = new Insets(0, 0, 5, 0);
    gbc_btnAddRM.gridx = 0;
    gbc_btnAddRM.gridy = 1;
    this.add(btnAddRm, gbc_btnAddRM);

    btnPrepare = new JButton("Prepare");
    GridBagConstraints gbc_btnPrepare = new GridBagConstraints();
    gbc_btnPrepare.gridwidth = 2;
    gbc_btnPrepare.insets = new Insets(0, 0, 5, 0);
    gbc_btnPrepare.gridx = 0;
    gbc_btnPrepare.gridy = 2;
    this.add(btnPrepare, gbc_btnPrepare);

    JTextPane txtpnWorking = new JTextPane();
    txtpnWorking.setText("Working:");
    txtpnWorking.setBackground(SystemColor.window);
    GridBagConstraints gbc_txtpnWorking = new GridBagConstraints();
    gbc_txtpnWorking.insets = new Insets(0, 0, 5, 5);
    gbc_txtpnWorking.gridx = 0;
    gbc_txtpnWorking.gridy = 3;
    this.add(txtpnWorking, gbc_txtpnWorking);

    workingCountPane = new JTextPane();
    workingCountPane.setText("0");
    workingCountPane.setEditable(false);
    workingCountPane.setBackground(SystemColor.window);
    GridBagConstraints gbc_workingCount = new GridBagConstraints();
    gbc_workingCount.insets = new Insets(0, 0, 5, 0);
    gbc_workingCount.fill = GridBagConstraints.VERTICAL;
    gbc_workingCount.gridx = 1;
    gbc_workingCount.gridy = 3;
    this.add(workingCountPane, gbc_workingCount);

    JTextPane txtpnPrepared = new JTextPane();
    txtpnPrepared.setText("Prepared:");
    txtpnPrepared.setBackground(SystemColor.window);
    GridBagConstraints gbc_txtpnPrepared = new GridBagConstraints();
    gbc_txtpnPrepared.insets = new Insets(0, 0, 5, 5);
    gbc_txtpnPrepared.gridx = 0;
    gbc_txtpnPrepared.gridy = 4;
    this.add(txtpnPrepared, gbc_txtpnPrepared);

    preparedCountPane = new JTextPane();
    preparedCountPane.setEditable(false);
    preparedCountPane.setText("0");
    preparedCountPane.setBackground(SystemColor.window);
    GridBagConstraints gbc_preparedCount = new GridBagConstraints();
    gbc_preparedCount.fill = GridBagConstraints.VERTICAL;
    gbc_preparedCount.insets = new Insets(0, 0, 5, 0);
    gbc_preparedCount.gridx = 1;
    gbc_preparedCount.gridy = 4;
    this.add(preparedCountPane, gbc_preparedCount);

    JTextPane txtpnCommitted = new JTextPane();
    txtpnCommitted.setText("Committed:");
    txtpnCommitted.setBackground(SystemColor.window);
    GridBagConstraints gbc_txtpnCommitted = new GridBagConstraints();
    gbc_txtpnCommitted.insets = new Insets(0, 0, 5, 5);
    gbc_txtpnCommitted.gridx = 0;
    gbc_txtpnCommitted.gridy = 5;
    this.add(txtpnCommitted, gbc_txtpnCommitted);

    committedCountPane = new JTextPane();
    committedCountPane.setEditable(false);
    committedCountPane.setText("0");
    committedCountPane.setBackground(SystemColor.window);
    GridBagConstraints gbc_committedCount = new GridBagConstraints();
    gbc_committedCount.fill = GridBagConstraints.VERTICAL;
    gbc_committedCount.insets = new Insets(0, 0, 5, 0);
    gbc_committedCount.gridx = 1;
    gbc_committedCount.gridy = 5;
    this.add(committedCountPane, gbc_committedCount);

    JTextPane txtpnAborted = new JTextPane();
    txtpnAborted.setText("Aborted:");
    txtpnAborted.setBackground(SystemColor.window);
    GridBagConstraints gbc_txtpnAborted = new GridBagConstraints();
    gbc_txtpnAborted.fill = GridBagConstraints.VERTICAL;
    gbc_txtpnAborted.insets = new Insets(0, 0, 5, 5);
    gbc_txtpnAborted.gridx = 0;
    gbc_txtpnAborted.gridy = 6;
    this.add(txtpnAborted, gbc_txtpnAborted);

    abortedCountPane = new JTextPane();
    abortedCountPane.setEditable(false);
    abortedCountPane.setText("0");
    abortedCountPane.setBackground(SystemColor.window);
    GridBagConstraints gbc_abortedCount = new GridBagConstraints();
    gbc_abortedCount.insets = new Insets(0, 0, 5, 0);
    gbc_abortedCount.fill = GridBagConstraints.VERTICAL;
    gbc_abortedCount.gridx = 1;
    gbc_abortedCount.gridy = 6;
    this.add(abortedCountPane, gbc_abortedCount);

    Component horizontalStrut = Box.createHorizontalStrut(20);
    GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
    gbc_horizontalStrut.gridwidth = 2;
    gbc_horizontalStrut.insets = new Insets(0, 0, 5, 5);
    gbc_horizontalStrut.gridx = 0;
    gbc_horizontalStrut.gridy = 7;
    this.add(horizontalStrut, gbc_horizontalStrut);

    JTextPane txtpnAbortProb = new JTextPane();
    txtpnAbortProb.setToolTipText("The probability that each node,"
        + " when asked to commit, will instead abort the transaction.");
    txtpnAbortProb.setText("p(Abort):");
    txtpnAbortProb.setBackground(SystemColor.window);
    GridBagConstraints gbc_txtpnAbortProb = new GridBagConstraints();
    gbc_txtpnAbortProb.insets = new Insets(0, 0, 5, 5);
    gbc_txtpnAbortProb.fill = GridBagConstraints.VERTICAL;
    gbc_txtpnAbortProb.gridx = 0;
    gbc_txtpnAbortProb.gridy = 7;
    this.add(txtpnAbortProb, gbc_txtpnAbortProb);
    
    abortProbSpinner = new JSpinner();
    Object[] options = {0.00, 0.10, 0.20, 0.30, 0.40, 0.50, 0.60, 0.70, 0.80, 0.90, 1.00};
    SpinnerListModel probabilities = new SpinnerListModel(options);
    abortProbSpinner.setModel(probabilities);
    GridBagConstraints gbc_AbortProb = new GridBagConstraints();
    gbc_AbortProb.insets = new Insets(0, 0, 5, 0);
    gbc_AbortProb.gridx = 1;
    gbc_AbortProb.gridy = 7;
    this.add(abortProbSpinner, gbc_AbortProb);

    logArea = new JTextArea();
    JScrollPane scroll = new JScrollPane(logArea);
    GridBagConstraints gbc_logArea = new GridBagConstraints();
    gbc_logArea.gridwidth = 2;
    gbc_logArea.fill = GridBagConstraints.BOTH;
    gbc_logArea.gridx = 0;
    gbc_logArea.gridy = 8;
    this.add(scroll, gbc_logArea);
  }

  /**
   * Increments the count of working RMs shown in the control panel
   */
  public void incrementWorkingCount() {
    workingCount++;
    workingCountPane.setText(Integer.toString(workingCount));
  }

  /**
   * Decrements the count of working RMs shown in the control panel
   */
  public void decrementWorkingCount() {
    workingCount--;
    workingCountPane.setText(Integer.toString(workingCount));
  }

  /**
   * Increments the count of prepared RMs shown in the control panel
   */
  public void incrementPreparedCount() {
    preparedCount++;
    preparedCountPane.setText(Integer.toString(preparedCount));
  }

  /**
   * Decrements the count of prepared RMs shown in the control panel
   */
  public void decrementPreparedCount() {
    preparedCount--;
    preparedCountPane.setText(Integer.toString(preparedCount));
  }

  /**
   * Increments the count of committed RMs shown in the control panel
   */
  public void incrementCommittedCount() {
    committedCount++;
    committedCountPane.setText(Integer.toString(committedCount));
  }

  /**
   * Increments the count of aborted RMs shown in the control panel
   */
  public void incrementAbortedCount() {
    abortedCount++;
    abortedCountPane.setText(Integer.toString(abortedCount));
  }
  
  public double getAbortProbSpinnerValue() {
    return Double.valueOf((Double) abortProbSpinner.getValue());
  }

  /**
   * Adds an action listener to the "New Simulation" button
   * @param al The action listener
   */
  public void addNewButtonListener(ActionListener al) {
    btnNewButton.addActionListener(al);
  }

  /**
   * Adds an action listener to the "Add RM" button
   * @param al The action listener
   */
  public void addAddRmButtonListener(ActionListener al) {
    btnAddRm.addActionListener(al);
  }

  /**
   * Adds an action listener to the "Prepare" button
   * @param al the action listener
   */
  public void addPrepareButtonListener(ActionListener al) {
    btnPrepare.addActionListener(al);
  }
  
  /**
   * Adds a change listener to the "p(abort)" spinner
   * @param cl the change listener
   */
  public void addProbSpinnerChangeListener(ChangeListener cl) {
    abortProbSpinner.addChangeListener(cl);
  }

  /**
   * Appends a string to the log area in the control panel
   * @param s the string to add
   */
  public void logAppend(String s) {
    logArea.append(s);
  }

  /**
   * Resets all visible features to default so a new simulation can occur
   */
  public void reset() {
    workingCount = 0;
    preparedCount = 0;
    committedCount = 0;
    abortedCount = 0;

    workingCountPane.setText(Integer.toString(workingCount));
    preparedCountPane.setText(Integer.toString(preparedCount));
    committedCountPane.setText(Integer.toString(committedCount));
    abortedCountPane.setText(Integer.toString(abortedCount));
    logArea.setText("");
  }
}
