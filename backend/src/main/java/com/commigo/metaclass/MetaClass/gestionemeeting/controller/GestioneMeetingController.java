package com.commigo.metaclass.MetaClass.gestionemeeting.controller;

import com.commigo.metaclass.MetaClass.entity.Meeting;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestionemeeting.service.GestioneMeetingService;
import com.commigo.metaclass.MetaClass.utility.request.RequestUtils;
import com.commigo.metaclass.MetaClass.utility.response.ResponseUtils;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import com.commigo.metaclass.MetaClass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GestioneMeetingController {

    @Autowired
    @Qualifier("GestioneMeetingService")
    private GestioneMeetingService meetingService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ValidationToken validationToken;

    @PostMapping(value = "/schedulingMeeting")
    public ResponseEntity<Response<Boolean>> schedulingMeeting(@Valid @RequestBody Meeting m, BindingResult result) {
        try {

            //controlla se i parametri passati al meeting sono corretti
            if(result.hasErrors())
            {
                return ResponseUtils.getResponseError(HttpStatus.INTERNAL_SERVER_ERROR,RequestUtils.errorsRequest(result));
            }

            if (!meetingService.creaScheduling(m)) {
                throw new RuntimeException("Meeting non effettuato");
            } else {
                return ResponseEntity.ok(new Response<>(true, "Meeting schedulato con successo"));
            }

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(false, "Errore durante la schedulazione del meeting: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/modificaScheduling")
    public ResponseEntity<Response<Meeting>> modificaScheduling
            (@Valid @RequestBody Meeting m, BindingResult result, HttpServletRequest request) {

        try {
            //controllo token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            //controllo errori di validazione
            if(result.hasErrors())
            {
                return ResponseEntity.status(403)
                        .body(new Response<>(null, RequestUtils.errorsRequest(result)));
            }

            Meeting meeting = null;
            if ((meeting=meetingService.modificaScheduling(m))==null) {
                throw new ServerRuntimeException("modifica non effettuata");
            } else {
                return ResponseEntity.ok(new Response<>(meeting, "Meeting schedulato con successo"));
            }

        } catch (RuntimeException403 e) {
            return ResponseEntity.status(403)
                    .body(new Response<>(null, e.getMessage()));
        } catch (ServerRuntimeException se) {
            return ResponseEntity.status(500)
                    .body(new Response<>(null, se.getMessage()));
        }
    }
}

