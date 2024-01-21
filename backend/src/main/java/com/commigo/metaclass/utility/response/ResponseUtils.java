package com.commigo.metaclass.utility.response;

import com.commigo.metaclass.utility.response.types.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** Classe di utility per le risposte API. */
public class ResponseUtils {

  public static ResponseEntity<Response<Boolean>> getResponseOk(String message) {
    return ResponseEntity.ok(new Response<>(true, message));
  }

  public static ResponseEntity<Response<Boolean>> getResponseError(
      HttpStatus status, String message) {
    return ResponseEntity.status(status).body(new Response<>(false, message));
  }
}
