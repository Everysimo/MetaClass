package com.commigo.metaclass.MetaClass.utility.request;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.util.List;

public class RequestUtils {

  public static String errorsRequest(BindingResult result) {
    List<String> errors =
        result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
    return String.join(", ", errors);
  }
}
