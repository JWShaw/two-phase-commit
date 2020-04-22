package models;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import controllers.RmListener;

/**
 * The main model of the program, which represents a simulation of an execution
 * of the two-phase commit algorithm.
 */
public class Simulation {
  private ArrayList<Rm> rmList;
  private Tm theManager;
  private RmListener listener;
  private int numRms;
  private double abortProb;

  /**
   * Constructor
   * 
   * @throws IOException
   */
  public Simulation() throws IOException {
    rmList = new ArrayList<>();
    abortProb = 0;
  }

  /**
   * Adds a new RM to this simulation. Starts the RM process on a new thread.
   * 
   * @throws UnknownHostException
   */
  public void addRm() throws UnknownHostException {
    Rm newRM = new Rm(numRms, theManager.getPort(), abortProb);
    rmList.add(newRM);
    if (listener != null) {
      newRM.addRmListener(listener);
    }
    numRms++;
    Thread t = new Thread(newRM, String.format("%d", numRms - 1));
    t.start();
  }

  /**
   * Adds a transaction manager to the simulation. Starts the TM process on new
   * thread.
   * 
   * @param port The port on which the TM is listening
   * @throws IOException
   */
  public void addTm(int port) throws IOException {
    theManager = new Tm(port);
    if (listener != null) {
      theManager.addRmListener(listener);
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
  public Rm getRm(int id) {
    return rmList.get(id);
  }

  /**
   * Returns a reference to the TM
   * 
   * @return The TM for the simulation
   */
  public Tm getTm() {
    return theManager;
  }
  
  /**
   * Sets the probability that any RM will abort when asked to commit
   * @param prob The aforementioned probability
   */
  public void setAbortProb(double prob)
  {
    abortProb = prob;
    for (Rm r : rmList)
    {
      r.setAbortProb(abortProb);
    }
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
  public void addRmListener(RmListener rmlistener) {
    this.listener = rmlistener;
  }

  /**
   * Resets the simulation to its initial conditions so that a new one can occur.
   * 
   * @param port The port on which the TM is to listen
   * @throws IOException
   */
  public void reset(int port) throws IOException {
    addTm(port);
    rmList = new ArrayList<>();
    numRms = 0;
  }
}
