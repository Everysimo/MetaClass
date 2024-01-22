package com.commigo.metaclass.utility.response.types;

import com.commigo.metaclass.utility.response.AbstractResponse;
import lombok.Data;

/**
 * Risposta di login.
 *
 * @param <T> tipo da restiture dal frontend
 */
@Data
public class LoginResponse<T> extends AbstractResponse<T> {

  private String token;
  private Boolean isAdmin;

  /**
   * Costruttore.
   *
   * @param value valore da restituire
   * @param message messagio da restituire
   * @param token token da restituire
   * @param isAdmin booleano da restituire
   */
  public LoginResponse(T value, String message, String token, Boolean isAdmin) {
    super(value, message);
    this.token = token;
    this.isAdmin = isAdmin;
  }
}
