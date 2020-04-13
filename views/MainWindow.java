package views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * The main window for the application GUI.
 */
public class MainWindow extends JFrame {
  private JPanel contentPane;

  /**
   * Builds the main window
   * 
   * @param cp The "control panel" view at the left of the window
   * @param gv The "graphical" view at the right of the window
   */
  public MainWindow(CtrlPanelView cp, GraphicalView gv) {
    setTitle("Two Phase Commit Simulator");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 450, 300);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    GridBagLayout gbl_contentPane = new GridBagLayout();
    gbl_contentPane.columnWidths = new int[] { 153, 440, 0 };
    gbl_contentPane.rowHeights = new int[] { 268, 0 };
    gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
    gbl_contentPane.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
    contentPane.setLayout(gbl_contentPane);

    GridBagConstraints gbc_ctrlPanel = new GridBagConstraints();
    gbc_ctrlPanel.insets = new Insets(0, 0, 0, 5);
    gbc_ctrlPanel.fill = GridBagConstraints.BOTH;
    gbc_ctrlPanel.gridx = 0;
    gbc_ctrlPanel.gridy = 0;
    contentPane.add(cp, gbc_ctrlPanel);

    GridBagConstraints gbc_simPanel = new GridBagConstraints();
    gbc_simPanel.fill = GridBagConstraints.BOTH;
    gbc_simPanel.gridx = 1;
    gbc_simPanel.gridy = 0;
    contentPane.add(gv, gbc_simPanel);
  }
}
