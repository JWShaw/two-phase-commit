package views;

/**
 * Interface for "StateListener", which listens for changes in state in RMs/TM.
 */
public interface StateListener {
  public void stateReceived(StateEvent st);
}
