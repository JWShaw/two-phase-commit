package models;

/**
 * The various states in which a TM or RM can exist
 */
public enum PState {
  WORKING, PREPARED, COMMITTED, ABORTED,

  INITIALIZING, // TM-only states
  PREPARING;
}
