package models;

/**
 * A message item which can be sent serially between processes.
 */
public enum Message {
  PREPARE, // TM -> RM messages
  COMMIT, ABORT,

  PREPARED, // RM -> RM messages
  COMMITTED, ABORTED;
}
