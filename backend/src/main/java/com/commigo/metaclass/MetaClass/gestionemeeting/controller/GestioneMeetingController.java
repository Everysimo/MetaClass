package com.commigo.metaclass.MetaClass.gestionemeeting.controller;

import com.commigo.metaclass.MetaClass.entity.Meeting;
import com.commigo.metaclass.MetaClass.gestionemeeting.service.GestioneMeetingService;
import com.commigo.metaclass.MetaClass.utility.request.RequestUtils;
import com.commigo.metaclass.MetaClass.utility.response.ResponseUtils;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
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
    @PostMapping(value = "/schedulingMeeting")
    public ResponseEntity<Response<Boolean>> schedulingMeeting(@Valid @RequestBody Meeting m, BindingResult result) {
        try {

            //controlla se i parametri passati al meeting sono corretti
            if(result.hasErrors())
            {
                return ResponseUtils.getResponseError(HttpStatus.INTERNAL_SERVER_ERROR, RequestUtils.errorsRequest(result));
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
}

