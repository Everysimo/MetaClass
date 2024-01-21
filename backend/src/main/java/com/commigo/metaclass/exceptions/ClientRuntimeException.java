package com.commigo.metaclass.exceptions;

/** eccezione custom. */
public class ClientRuntimeException extends Exception {

  /**
   * costruttore.
   *
   * @param message
   */
  public ClientRuntimeException(String message) {
    super(message);
  }
}
