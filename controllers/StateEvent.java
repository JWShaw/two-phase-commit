package controllers;

import java.util.EventObject;
import models.PState;


/**
 * An event which represents a change in state in an RM or TM
 */
public class StateEvent extends EventObject {
  private PState newState;
  private PState oldState;
  private int id;

  /**
   * Creates a new state event
   * 
   * @param source   The RM/TM at which the event was created
   * @param new_state The new (current) state of the RM/TM
   * @param old The previous state of the RM/TM
   * @param id       A unique identifier for the RM/TM
   */
  public StateEvent(Object source, PState new_state, PState old, int id) {
    super(source);
    newState = new_state;
    oldState = old;
    this.id = id;
  }

  /**
   * @return The present state of the source RM/TM
   */
  public PState newState() {
    return newState;
  }

  /**
   * @return The previous state of the source RM/TM
   */
  public PState oldState() {
    return oldState;
  }

  /**
   * @return The unique identifier of the source RM/TM
   */
  public int id() {
    return id;
  }
}
