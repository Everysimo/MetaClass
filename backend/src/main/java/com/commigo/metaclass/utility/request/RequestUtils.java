package com.commigo.metaclass.utility.request;

import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/** Classe utility per le richieste. */
public class RequestUtils {

  /**
   * Ottieni il messaggio di errore.
   *
   * @param result
   * @return
   */
  public static String errorsRequest(BindingResult result) {
    List<String> errors =
        result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
    return String.join(", ", errors);
  }
}
