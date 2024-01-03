package com.commigo.metaclass.MetaClass.gestionemeeting.controller;
import com.commigo.metaclass.MetaClass.utility.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class CustomExceptionHandler {

    //creata per gstire errori di parsing nelle date
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response<Boolean>> handleJsonParseException(HttpMessageNotReadableException ex) {

        // Estrai il messaggio dell'eccezione
        String errorMessage = ex.getMessage();

        // Cerca l'indice della sottostringa "Text"
        int textIndex = errorMessage.indexOf("Text");

        // Estrai la data dalla sottostringa dell'errore
        String dateText = "data non trovata";
        if (textIndex != -1) {
            int startIndex = textIndex + "Text '".length();
            int endIndex = errorMessage.indexOf("'", startIndex);
            if (endIndex != -1) {
                dateText = errorMessage.substring(startIndex, endIndex);
            }
        }

        // Costruisci una risposta personalizzata
        String responseMessage = "Errore durante il parsing JSON: la data '" + dateText +
                "' non è del formato giusto. Formato:  yyyy-MM-dd HH-mm";

        // Restituisci una risposta con uno stato 400 (Bad Request) e il messaggio personalizzato
        return ResponseEntity.badRequest().body(new Response<Boolean>(false,
                responseMessage));
    }
}

