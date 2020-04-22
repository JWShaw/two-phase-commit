package controllers;

import java.util.EventObject;

import models.Message;

/**
 * An event which represents a message sent between RMs/TM
 */
public class MessageEvent extends EventObject {
  private Message messageSent;
  private int id;

  /**
   * Creates a new message event object
   * @param source The RM (or TM) from which the message is sent
   * @param msg The message being sent
   * @param id The unique ID of the RM (or TM) sending the message
   */
  public MessageEvent(Object source, Message msg, int id) {
    super(source);
    messageSent = msg;
    this.id = id;
  }

  /**
   * @return The message being sent
   */
  public Message message() {
    return messageSent;
  }

  /**
   * @return The unique identifier of the source RM/TM
   */
  public int id() {
    return id;
  }
}

