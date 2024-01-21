package com.commigo.metaclass.utility.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Richiesta astratta. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractResponse<T> {
  private T value;
  String message;
}
