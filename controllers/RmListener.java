package controllers;

/**
 * Interface for "StateListener", which listens for changes in state in RMs/TM.
 */
public interface RmListener {
  public void stateReceived(StateEvent st);
  public void messageSent(MessageEvent m);
}
