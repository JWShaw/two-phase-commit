package models;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import views.StateListener;

/**
 * The main model of the program, which represents a simulation of an execution
 * of the two-phase commit algorithm.
 */
public class Simulation {
  private ArrayList<RM> listofRMs;
  private TM theManager;
  private StateListener stl;
  private int numRMs;

  /**
   * Constructor
   * 
   * @throws IOException
   */
  public Simulation() throws IOException {
    listofRMs = new ArrayList<>();
  }

  /**
   * Adds a new RM to this simulation. Starts the RM process on a new thread.
   * 
   * @throws UnknownHostException
   */
  public void addRM() throws UnknownHostException {
    RM newRM = new RM(numRMs, theManager.getPort());
    listofRMs.add(newRM);
    if (stl != null) {
      newRM.addStateListeneer(stl);
    }
    numRMs++;
    Thread t = new Thread(newRM, String.format("%d", numRMs - 1));
    t.start();
  }

  /**
   * Adds a transaction manager to the simulation. Starts the TM process on new
   * thread.
   * 
   * @param port The port on which the TM is listening
   * @throws IOException
   */
  public void addTM(int port) throws IOException {
    theManager = new TM(port);
    if (stl != null) {
      theManager.addStateListener(stl);
    }
    Thread man = new Thread(theManager, "TM");
    man.start();
  }

  /**
   * Retrieves a particular RM
   * 
   * @param id The unique ID of the RM
   * @return The requested RM
   */
  public RM getRM(int id) {
    return listofRMs.get(id);
  }

  /**
   * Returns a reference to the TM
   * 
   * @return The TM for the simulation
   */
  public TM getTM() {
    return theManager;
  }

  /**
   * Tells the TM to enter a PREPARING state, thus initiating the two-phase
   * commit.
   * 
   * @throws IOException
   */
  public void prepare() throws IOException {
    theManager.prepare();
  }

  /**
   * Adds a state-change listener to this simulation.
   * 
   * @param stl
   */
  public void addStateListener(StateListener stl) {
    this.stl = stl;
  }

  /**
   * Resets the simulation to its initial conditions so that a new one can occur.
   * 
   * @param port The port on which the TM is to listen
   * @throws IOException
   */
  public void reset(int port) throws IOException {
    addTM(port);
    listofRMs = new ArrayList<>();
    numRMs = 0;
  }
}
