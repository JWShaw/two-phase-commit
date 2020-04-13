package views;

import java.util.EventObject;
import models.PState;


/**
 * An event which represents a change in state in an RM or TM
 */
public class StateEvent extends EventObject {
  private PState new_st;
  private PState old_st;
  private int id;

  /**
   * Creates a new state event
   * 
   * @param source   The RM/TM at which the event was created
   * @param newState The new (current) state of the RM/TM
   * @param oldState The previous state of the RM/TM
   * @param id       A unique identifier for the RM/TM
   */
  public StateEvent(Object source, PState newState, PState oldState, int id) {
    super(source);
    new_st = newState;
    old_st = oldState;
    this.id = id;
  }

  /**
   * @return The present state of the source RM/TM
   */
  public PState newState() {
    return new_st;
  }

  /**
   * @return The previous state of the source RM/TM
   */
  public PState oldState() {
    return old_st;
  }

  /**
   * @return The unique identifier of the source RM/TM
   */
  public int id() {
    return id;
  }
}
