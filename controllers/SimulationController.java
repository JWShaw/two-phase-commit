package controllers;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import models.PState;
import models.Simulation;
import views.CtrlPanelView;
import views.GraphicalView;

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
      sim.addTm(5055);
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
        if (sim.getTm().getState() == PState.COMMITTED || sim.getTm().getState() == PState.ABORTED) {
          // Resets all views to their default state, and also resets the simulation
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
    class AddRmListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (sim.getTm().getState() == PState.WORKING) {
          try {
            sim.addRm();
            gv.addRmView();
          } catch (UnknownHostException e1) {
            e1.printStackTrace();
          }
        }
      }
    }

    /**
     * Listener, which dictates what happens when the TM or RMs change state
     * or a message is sent between RMs/TM.
     */
    class StateChangeListener implements RmListener {
      @Override
      public void stateReceived(StateEvent ste) {
        if (ste.id() == -1) {
          gv.colorTmView(stateToColor(ste.newState()));
          if (ste.newState() == PState.WORKING) {
            cv.logAppend(String.format("TM: %s%n", ste.newState()));
          }
        } else {
          gv.colorRmView(ste.id(), stateToColor(ste.newState()));
          cv.logAppend(String.format("RM%d: %s%n", ste.id(), ste.newState()));

          // Updates total number of RMs shown in each state on the control panel view
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

      @Override
      public void messageSent(MessageEvent mev) {
        if (mev.id() == -1) {
          cv.logAppend(String.format("TM->ALL: %s%n", mev.message()));
          /* To reduce clutter, only the TM broadcasts are printed in the log.
           * RM -> TM messages were previously shown, but this didn't end up looking good. */
        }
      }
    }

    /**
     * Listener that changes the probability of RM failure
     */
    class ProbChangeListener implements ChangeListener {
      @Override
      public void stateChanged(ChangeEvent e) {
        sim.setAbortProb(cv.getAbortProbSpinnerValue());
      }
    }

    // Add appropriate listeners to each component
    cv.addNewButtonListener(new NewButtonListener());
    cv.addAddRmButtonListener(new AddRmListener());
    cv.addPrepareButtonListener(new PrepareListener());
    cv.addProbSpinnerChangeListener(new ProbChangeListener());
    sim.addRmListener(new StateChangeListener());
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
        return new Color(89, 145, 198);
      case PREPARED:
        return new Color(252, 195, 73);
      case COMMITTED:
        return new Color(67, 204, 71);
      case ABORTED:
        return new Color(204, 67, 67);
      case INITIALIZING:
        return Color.cyan;
      case PREPARING:
        return new Color(252, 195, 73);
      default:
        return Color.black;
    }
  }
}
