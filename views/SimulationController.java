package views;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import models.PState;
import models.Simulation;

/**
 * The main controller class, which coordinates between the model (Simulation)
 * and views (CtrlPanelView and GraphicalView)
 */
public class SimulationController {
  private CtrlPanelView cv;
  private GraphicalView gv;
  private Simulation sim;

  // Constructor: launches a new simulation
  public SimulationController(CtrlPanelView cv, GraphicalView gv) {
    this.cv = cv;
    this.gv = gv;

    try {
      this.sim = new Simulation();
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    setUpListeners();

    try {
      sim.addTM(5055);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sets up necessary event listeners in the model and views
   */
  public void setUpListeners() {

    /**
     * The listener for the "New Simulation" button
     */
    class NewButtonListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (sim.getTM().getState() == PState.COMMITTED || sim.getTM().getState() == PState.ABORTED) {
          gv.reset();
          cv.reset();
          try {
            sim.reset(5055);
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        }
      }
    }

    /**
     * The listener for the "Prepare" button
     */
    class PrepareListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          sim.prepare();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    }

    /**
     * The listener for the "Add RM" button
     */
    class AddRMListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (sim.getTM().getState() == PState.WORKING) {
          try {
            sim.addRM();
            gv.addRMView();
          } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        }
      }
    }

    /**
     * Listener, which dictates what happens when the TM or RMs change state.
     */
    class StateChangeListener implements StateListener {
      @Override
      public void stateReceived(StateEvent ste) {
        if (ste.id() == -1) {
          gv.colorTMView(stateToColor(ste.newState()));
          cv.logAppend(String.format("TM: %s%n", ste.newState()));
        } else {
          gv.colorRMView(ste.id(), stateToColor(ste.newState()));
          cv.logAppend(String.format("RM%d: %s%n", ste.id(), ste.newState()));

          switch (ste.oldState()) {
          case WORKING:
            cv.decrementWorkingCount();
            break;
          case PREPARED:
            cv.decrementPreparedCount();
            break;
          default:
            break;
          }

          switch (ste.newState()) {
          case WORKING:
            cv.incrementWorkingCount();
            break;
          case PREPARED:
            cv.incrementPreparedCount();
            break;
          case COMMITTED:
            cv.incrementCommittedCount();
            break;
          case ABORTED:
            cv.incrementAbortedCount();
            break;
          default:
            break;
          }
        }
      }
    }
    
    class ProbChangeListener implements ChangeListener {
      @Override
      public void stateChanged(ChangeEvent e) {
        sim.setAbortProb(cv.getAbortProbSpinnerValue());
      }
    }

    // Add appropriate listeners to each component
    cv.addNewButtonListener(new NewButtonListener());
    cv.addAddRMButtonListener(new AddRMListener());
    cv.addPrepareButtonListener(new PrepareListener());
    cv.addProbSpinnerChangeListener(new ProbChangeListener());
    sim.addStateListener(new StateChangeListener());
  }

  /**
   * Helper method which returns a color associates with a given state
   * 
   * @param st A state for which an associated color is desired
   * @return The associated color
   */
  private Color stateToColor(PState st) {
    switch (st) {
    case WORKING:
      return Color.blue;
    case PREPARED:
      return Color.pink;
    case COMMITTED:
      return Color.green;
    case ABORTED:
      return Color.red;
    case INITIALIZING:
      return Color.cyan;
    case PREPARING:
      return Color.pink;
    default:
      return Color.black;
    }
  }
}
