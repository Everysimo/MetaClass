package com.commigo.metaclass.MetaClass.exceptions;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MismatchJsonProperty.class)
    public ResponseEntity<Response<Boolean>> handleMismatchedInputException(MismatchJsonProperty ex) {

        // Restituisci una risposta con uno stato 400 (Bad Request) e il messaggio personalizzato
        return ResponseEntity.badRequest().body(new Response<>(false,
                "errore nella richiesta: "+ex.getMessage()));
    }

    @ExceptionHandler(DataFormatException.class)
    public ResponseEntity<Response<Boolean>> handleDataFormatException(DataFormatException ex) {
        return ResponseEntity.badRequest().body(new Response<>(false, ex.getMessage()));
    }

    /*@ExceptionHandler(HttpMessageNotReadableException.class)
    @Order(Ordered.LOWEST_PRECEDENCE)
    public ResponseEntity<Response<Boolean>> handleJsonParseException(HttpMessageNotReadableException ex) {

        return ResponseEntity.badRequest().body(new Response<Boolean>(false,
                "Errore nella richiesta"));
    }*/
}

