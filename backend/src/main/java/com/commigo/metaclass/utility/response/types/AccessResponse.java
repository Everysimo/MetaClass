package com.commigo.metaclass.utility.response.types;

import com.commigo.metaclass.utility.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe per una risposta di tipo accesso stanza. Contiene valori necessari al frontend per operare
 *
 * @param <> Tipo di dati da mandare al frontend
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessResponse<T> extends AbstractResponse<T> {

  private boolean isInAttesa;

  public AccessResponse(T value, String message, boolean isInAttesa) {
    super(value, message);
    this.isInAttesa = isInAttesa;
  }
}
