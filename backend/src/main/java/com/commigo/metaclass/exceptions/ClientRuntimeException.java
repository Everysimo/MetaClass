package com.commigo.metaclass.exceptions;

/** Eccezione client. */
public class ClientRuntimeException extends Exception {

  /**
   * Costruttore.
   *
   * @param message messaggio di errore
   */
  public ClientRuntimeException(String message) {
    super(message);
  }
}
