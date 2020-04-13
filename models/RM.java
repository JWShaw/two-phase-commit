package models;

import views.StateEvent;
import views.StateListener;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class RM implements Runnable {
  private InetAddress host;
  private int id;
  private PState st;
  private StateListener listener;

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
  public RM(int id, int port) throws UnknownHostException {
    this.id = id;
    this.destinationPort = port;
    st = PState.INITIALIZING;

    host = InetAddress.getLocalHost();
  }

  /**
   * Mimicks the RM process that would run on a typical node
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
          ;
        } catch (ClassNotFoundException cnfe) {
          ;
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
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    oos.writeObject(m);
  }

  /**
   * Places the RM in a PREPARED state when appropriate.
   * 
   * @throws IOException
   */
  public void prepare() throws IOException {
    if (st == PState.WORKING || st == PState.PREPARED) {
      changeState(PState.PREPARED);
      sendMessage(Message.PREPARED);
    } else {
      System.err.println("Error: RM" + id + " tried to prepare, " + "but was already committed/aborted!");
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
  public void abort() throws IOException {
    changeState(PState.ABORTED);
    sendMessage(Message.ABORTED);
  }

  /**
   * Adds a state-change listener for the RM so state changes can be observed from
   * outside
   * 
   * @param l The listener to be added
   */
  public void addStateListeneer(StateListener l) {
    listener = l;
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
   * Closes socket and streams.
   */
  public void closeAllConnections() {
    try {
      oos.close();
      ois.close();
      sock.close();
    } catch (Exception e) {
      ;
    }
  }
}
