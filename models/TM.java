package models;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import controllers.MessageEvent;
import controllers.RmListener;
import controllers.StateEvent;

public class Tm implements Runnable {
  private PState st;
  private int port;
  private ServerSocket server;
  private RmListener listener;

  private ArrayList<ClientConnection> clients;
  private LinkedBlockingQueue<Message> msgQueue;

  /**
   * Constructor for TM
   * @param port The port number on which the TM process is listening
   * @throws IOException
   */
  public Tm(int port) throws IOException {
    this.port = port;
    clients = new ArrayList<>();
    msgQueue = new LinkedBlockingQueue<>();
    this.server = new ServerSocket(port);
  }

  /**
   * Mimicks the TM process that would run on a node in the system.
   */
  @Override
  public void run() {
    try {
      // Connection-manager thread handles new incoming connections
      changeState(PState.WORKING);
      dispatchConnectionManager();
  
      // If TM enters PREPARING state, initiate the two-phase commit algorithm
      while (true) {
        Thread.sleep(100);
        if (st == PState.PREPARING) {
          twoPhaseCommit();
          break;
        }
      }
    } catch (InterruptedException ie) {
      ie.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } finally {
      // Ensures that all connections are closed after the simulation is complete.
      closeAllConnections();
    }
  }

  /**
   * Add a state change listener so an outside observer can see state changes
   * @param stl The state-change listener to be added
   */
  public void addRmListener(RmListener rmlistener) {
    this.listener = rmlistener;
  }

  /**
   * @return The port number on which the TM is listening
   */
  public int getPort() {
    return port;
  }

  /**
   * @return The current state of the TM
   */
  public PState getState() {
    return st;
  }

  // Tells the TM to enter a PREPARED state
  public void prepare() {
    if (st == PState.WORKING) {
      changeState(PState.PREPARING);
    }
  }

  /**
   * Closes all connections, allowing for a new simulation to occur.
   */
  public void closeAllConnections() {
    try {
      for (ClientConnection c : clients) {
        c.close();
      }
      server.close();
    } catch (Exception e) {
      // Do nothing, as nothing can be done.
    }
  }

  /**
   * Dispatches a Connection Manager thread to handle incoming RM connections
   */
  private void dispatchConnectionManager() {
    Thread connectionManager = new Thread() {
      @Override
      public void run() {
        while (st == PState.WORKING) {
          try {
            ClientConnection newCon = new ClientConnection(server.accept());
            clients.add(newCon);
          } catch (IOException ioe) {
            break;
          }
        }
      }
    };
    // Ensures that this thread won't keep the JVM running
    connectionManager.setDaemon(true);
    connectionManager.start();
  }

  // The actual two-phase commit algorithm for transaction consensus.
  private void twoPhaseCommit() throws IOException, InterruptedException {
    int preparedRMs = 0;
  
    // Phase 1: Ask all the RMs to prepare
    broadcast(Message.PREPARE);
    while (preparedRMs < clients.size()) {
      try {
        Message m = msgQueue.take();
        if (m == Message.PREPARED) {
          preparedRMs++;
        } else if (m == Message.ABORTED) {
          changeState(PState.ABORTED);
          broadcast(Message.ABORT);
          break;
        }
      } catch (NullPointerException npe) {
        ;
      }
    }
  
    // Phase 2: Once ALL RMs have prepared, ask them to commit.
    if (preparedRMs == clients.size()) {
      changeState(PState.COMMITTED);
      broadcast(Message.COMMIT);
    }
  }

  /**
   * Change the state of the TM
   * 
   * @param s The new state of the TM
   */
  private void changeState(PState s) {
    PState oldState = st;
    this.st = s;
    fireStateEvent(oldState);
  }

  /**
   * Broadcasts a message to every connected RM process
   * 
   * @param m The message to be sent to every RM process
   * @throws IOException
   */
  private void broadcast(Message m) throws IOException {
    fireMessageEvent(m);
    for (ClientConnection c : clients) {
      pause();
      c.send(m);
    }
  }

  /**
   * Triggers an event when a state-change occurs.
   * @param oldState The previous state of the system
   */
  private void fireStateEvent(PState oldState) {
    StateEvent ev = new StateEvent(this, st, oldState, -1);
    listener.stateReceived(ev);
  }
  
  /**
   * Triggers an event when a message is broadcast.
   * @param msg The message being broadcast to all RMs.
   */
  private void fireMessageEvent(Message msg) {
    MessageEvent mev = new MessageEvent(this, msg, -1);
    listener.messageSent(mev);
  }

  /** 
   *  Pauses the thread for a half second---used to slow down simulation.
   */
  private void pause() {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      // Can't make the thread sleep?  Do nothing; it's not the end of the world.
    }
  }

  /**
   * Inner class which represents a connection from TM to RM.
   */
  class ClientConnection {
    Socket sock;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    boolean isOpen;

    /**
     * Establishes a new connection from TM to RM
     * @param sock
     * @throws IOException
     */
    ClientConnection(Socket sock) throws IOException {
      this.sock = sock;
      ois = new ObjectInputStream(sock.getInputStream());
      oos = new ObjectOutputStream(sock.getOutputStream());
      isOpen = true;

      /*
       * A new thread listens for messages from the RM; directs them to the TM's
       * message queue
       */
      Thread listener = new Thread() {
        public void run() {
          while (isOpen) {
            try {
              Message m = (Message) ois.readObject();
              msgQueue.put(m);
            } catch (EOFException eof) {
              ; // Just try again if no message is available.
            } catch (SocketException se) {
              return;
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      };

      // setDaemon(true): ensures that this thread doesn't keep the JVM running
      listener.setDaemon(true);
      listener.start();
    }

    /**
     * Sends a message to the associated RM
     * 
     * @param m The message to be sent to the RM
     * @throws IOException
     */
    void send(Message m) throws IOException {
      oos.writeObject(m);
    }

    /**
     * Closes this connection between TM and RM.
     * @throws IOException
     */
    void close() throws IOException {
      isOpen = false;
      ois.close();
      oos.close();
      sock.close();
    }
  }
}
