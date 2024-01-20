package com.commigo.metaclass.MetaClass.utility.response;

import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Type;

public class ResponseUtils {

  public static ResponseEntity<Response<Boolean>> getResponseOk(String message) {
    return ResponseEntity.ok(new Response<>(true, message));
  }

  public static ResponseEntity<Response<Boolean>> getResponseError(
      HttpStatus status, String message) {
    return ResponseEntity.status(status).body(new Response<>(false, message));
  }
}
