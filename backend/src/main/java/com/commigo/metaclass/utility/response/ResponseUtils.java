package com.commigo.metaclass.utility.response;

import com.commigo.metaclass.utility.response.types.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** Classe di utility per le risposte API. */
public class ResponseUtils {

  /**
   * Metodo che restituisce una risposta con status 200.
   *
   * @param message messaggio della risposta
   * @return restituisce una risposta con status 200
   */
  public static ResponseEntity<Response<Boolean>> getResponseOk(String message) {
    return ResponseEntity.ok(new Response<>(true, message));
  }

  /**
   * Metodo che restituisce una risposta con status diverso da 200.
   *
   * @param message messaggio della risposta
   * @return restituisce una risposta con status diverso da 200
   */
  public static ResponseEntity<Response<Boolean>> getResponseError(
      HttpStatus status, String message) {
    return ResponseEntity.status(status).body(new Response<>(false, message));
  }
}
