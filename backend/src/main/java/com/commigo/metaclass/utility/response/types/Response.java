package com.commigo.metaclass.utility.response.types;

import com.commigo.metaclass.utility.response.AbstractResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe di risposta classica.
 *
 * @param <T> tipo di valore da ritornare al frontend
 */
@Getter
@Setter
public class Response<T> extends AbstractResponse<T> {

  public Response(T value, String message) {
    super(value, message);
  }
}
