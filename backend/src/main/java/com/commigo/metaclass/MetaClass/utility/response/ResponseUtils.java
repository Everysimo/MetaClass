package com.commigo.metaclass.MetaClass.utility.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ResponseUtils
{

    public static ResponseEntity<Response<Boolean>> getResponseOk(String message)
    {
        return ResponseEntity.ok(new Response<Boolean>(true,message));
    }
    public static ResponseEntity<Response<Boolean>> getResponseError(String message)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response<Boolean>(false,message));
    }
}
