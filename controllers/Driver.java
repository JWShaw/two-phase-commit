package controllers;

import java.awt.EventQueue;

import views.CtrlPanelView;
import views.GraphicalView;
import views.MainWindow;

public class Driver {
  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    CtrlPanelView cp = new CtrlPanelView();
    GraphicalView gv = new GraphicalView();
    @SuppressWarnings("unused")
    SimulationController sc = new SimulationController(cp, gv);

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

    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}