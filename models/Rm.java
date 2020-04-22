package models;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import controllers.MessageEvent;
import controllers.RmListener;
import controllers.StateEvent;

public class Rm implements Runnable {
  private InetAddress host;
  private int id;
  private PState st;
  private RmListener listener;
  private double abortProb;

  private int destinationPort;
  private Socket sock;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;

  /**
   * Constructor for the RM
   * 
   * @param id  Identification number
   * @param The port on which the TM is listening
   */
  public Rm(int id, int port, double abortProb) throws UnknownHostException {
    this.id = id;
    this.destinationPort = port;
    this.abortProb = abortProb;
    st = PState.INITIALIZING;

    host = InetAddress.getLocalHost();
  }

  /**
   * Mimicks the RM process that would run on a typical node.
   * Reads incoming messages and changes state accordingly.
   */
  public void run() {
    try {
      sock = new Socket(host.getHostName(), destinationPort);
      oos = new ObjectOutputStream(sock.getOutputStream());
      ois = new ObjectInputStream(sock.getInputStream());
  
      changeState(PState.WORKING);
  
      // Continues to poll for new messages while not COMMITTED or ABORTED
      while (st == PState.WORKING || st == PState.PREPARED) {
        Message m = null;
        try {
          m = (Message) ois.readObject();
        } catch (EOFException eofe) {
          ; // No message available?  That's okay; try again.
        } catch (ClassNotFoundException cnfe) {
          cnfe.printStackTrace();
        }
  
        // When a message is recieved, the RM responds appropriately
        if (m == Message.PREPARE)
          prepare();
        else if (m == Message.COMMIT) {
          commit();
        } else if (m == Message.ABORT)
          abort();
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } finally {
      // Closes the connections/streams when done.
      closeAllConnections();
    }
  }

  /**
   * Adds a state-change listener for the RM so state changes can be observed from
   * outside
   * 
   * @param l The listener to be added
   */
  public void addRmListener(RmListener l) {
    listener = l;
  }

  /**
   * Sets the probability that the RM will, when asked to prepare, instead abort.
   * @param prob The aforementioned probability
   */
  public void setAbortProb(double prob)
  {
    abortProb = prob;
  }

  /**
   * Places the RM in a PREPARED state when appropriate.
   * 
   * @throws IOException
   */
  public void prepare() throws IOException {
    Random r = new Random();
    double rand = r.nextDouble();
    
    if (rand > abortProb)
    {
      changeState(PState.PREPARED);
      sendMessage(Message.PREPARED);
    }
    else
    {
      abort();
    }
  }

  /**
   * Closes socket and streams.
   */
  public void closeAllConnections() {
    try {
      oos.close();
      ois.close();
      sock.close();
    } catch (Exception e) {
      // Do nothing, as nothing can be done.
    }
  }

  /**
   * Places the RM in a COMMITTED state when appropriate.
   * 
   * @throws IOException
   */
  private void commit() throws IOException {
    if (st == PState.PREPARED) {
      changeState(PState.COMMITTED);
    } else {
      System.err.println("Error: RM" + id + " tried to commit, " + "but was not prepared!");
    }
  }

  /**
   * Places the RM in an ABORTED state.
   * 
   * @throws IOException
   */
  private void abort() throws IOException {
    changeState(PState.ABORTED);
    sendMessage(Message.ABORTED);
  }

  /**
   * Internal method to change the RM state.
   * 
   * @param s The new RM state
   */
  private void changeState(PState s) {
    PState oldState = st;
    this.st = s;
    fireStateEvent(oldState);
  }

  /**
   * Sends a message to the TM
   * 
   * @param m The message to be sent to the TM
   * @throws IOException
   */
  private void sendMessage(Message m) throws IOException {
    pause();
    // fireMessageEvent(m); // Not being used in this iteration of the program.
    pause();
    oos.writeObject(m);
  }

  /**
   * Triggers an event when a state-change occurs
   * 
   * @param oldState The previous state of the system
   */
  private void fireStateEvent(PState oldState) {
    StateEvent ev = new StateEvent(this, st, oldState, id);
    listener.stateReceived(ev);
  }
  
  /**
   * Triggers an event when a message sent to the TM.
   * Not being used in this iteration of the program, but kept for future use.
   * @param msg The message being broadcast to all RMs.
   */
  private void fireMessageEvent(Message msg) {
    MessageEvent mev = new MessageEvent(this, msg, id);
    listener.messageSent(mev);
  }

  /** 
   *  Pauses the thread for a half second---used to slow down simulation.
   */
  private void pause() {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      // Can't make the thread sleep?  Oh well.  Do nothing.
    }
  }
}
