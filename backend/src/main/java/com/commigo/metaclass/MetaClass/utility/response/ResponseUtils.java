package com.commigo.metaclass.MetaClass.utility.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

//MICHELE: voglio valutare l'utilizzo di questa funzione
//         per ora accetta solo parametri, quindi da sostituire con generics
public class ResponseUtils
{

    public static ResponseEntity<Response<Boolean>> getResponseOk(String message)
    {
        return ResponseEntity.ok(new Response<Boolean>(true,message));
    }
    //MICHELE: apporterei una modifica ai parametri del metodo (message, status)
    public static ResponseEntity<Response<Boolean>> getResponseError(String message)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response<Boolean>(false,message));
    }
}
